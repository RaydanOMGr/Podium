package me.andreasmelone.podium;

import com.google.gson.Gson;
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class Util {
    public static final Gson GSON = new Gson();

    public static Optional<byte[]> tryFindClass(String className) {
        File modsDir = new File("mods");
        File[] files = modsDir.listFiles();
        if(files == null) return Optional.empty();

        for (File mod : files) {
            if(!mod.getName().endsWith(".jar")) continue;
            // this JarFile is created to ensure the jar is a valid jar
            try(JarFile jar = new JarFile(mod)) {
                Optional<byte[]> result = scanJar(Files.readAllBytes(mod.toPath()), className);
                if(result.isPresent()) return result;
            } catch (IOException e) {
                System.out.println("[Podium] Failed to read jar file " + mod.getAbsolutePath());
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private static Optional<byte[]> scanJar(byte[] jar, String className) {
        String metadataJson = null;

        try (JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jar))) {
            JarEntry entry;

            while ((entry = jis.getNextJarEntry()) != null) {
                if (entry.getName().equals(className + ".class")) {
                    return Optional.of(jis.readAllBytes());
                }
                if (entry.getName().equals("META-INF/jarjar/metadata.json")) {
                    metadataJson = new String(jis.readAllBytes(), StandardCharsets.UTF_8);
                }
                jis.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }


        if (metadataJson != null) {
            MetadataJars jars = GSON.fromJson(metadataJson, MetadataJars.class);
            try(JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jar))) {
                JarEntry entry;
                while ((entry = jis.getNextJarEntry()) != null) {
                    for (MetadataJars.MetadataJar metadataJar : jars.jars) {
                        if (metadataJar.path.equals(entry.getName())) {
                            Optional<byte[]> result = scanJar(jis.readAllBytes(), className);
                            if (result.isPresent()) return result;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    // ClassLoader#defineClass is unaccessible due to the module system, and we need to register the class
    // on a specific ClassLoader which MethodHandles.Lookup does not let us do
    // https://stackoverflow.com/questions/55918972/unable-to-find-method-sun-misc-unsafe-defineclass
    public static Class<?> defineClass(ClassLoader loader, String className, byte[] bytecode) {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe"),
                    f1 = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            f1.setAccessible(false);
            Unsafe unsafe = (Unsafe) f.get(null);
            int i;//override boolean byte offset. should result in 12 for java 17
            for (i = 0; unsafe.getBoolean(f, i) == unsafe.getBoolean(f1, i); i++);
            Field f2 = Unsafe.class.getDeclaredField("theInternalUnsafe");
            unsafe.putBoolean(f2, i, true);//write directly into override to bypass perms
            Object internalUnsafe = f2.get(null);

            Method defineClass = internalUnsafe.getClass().getDeclaredMethod("defineClass",
                    String.class, byte[].class, int.class, int.class,
                    ClassLoader.class, ProtectionDomain.class);
            unsafe.putBoolean(defineClass, i, true);

            return (Class<?>) defineClass.invoke(internalUnsafe,
                    className, bytecode, 0, bytecode.length,
                    loader, null);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("[Podium] Unable to define class " + className);
            e.printStackTrace();
            return null;
        }
    }

    static class MetadataJars {
        public List<MetadataJar> jars;

        static class MetadataJar {
            public String path;
        }
    }
}
