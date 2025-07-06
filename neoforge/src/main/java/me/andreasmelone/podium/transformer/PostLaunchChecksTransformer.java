package me.andreasmelone.podium.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class PostLaunchChecksTransformer {
    public static final String CLASSNAME_DOTTED = "net.caffeinemc.mods.sodium.client.compatibility.checks.PostLaunchChecks";
    public static final String CLASSNAME_SLASHED = CLASSNAME_DOTTED.replace(".", "/");

    public byte[] transform(byte[] classfileBuffer) {
        System.out.println("[Transformer] Transforming " + CLASSNAME_DOTTED);

        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ReturnFalseInjector injector = new ReturnFalseInjector(writer, "isUsingPojavLauncher", "()Z");
        reader.accept(injector, 0);

        return writer.toByteArray();
    }

    public static class ReturnFalseInjector extends ClassVisitor {
        private final String methodName;
        private final String methodDesc;

        public ReturnFalseInjector(ClassVisitor cv, String methodName, String methodDesc) {
            super(ASM9, cv);
            this.methodName = methodName;
            this.methodDesc = methodDesc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals(methodName) && desc.equals(methodDesc)) {
                return new MethodVisitor(ASM9, mv) {
                    @Override
                    public void visitCode() {
                        super.visitCode();
                        super.visitInsn(ICONST_0);
                        super.visitInsn(IRETURN);
                    }
                };
            }
            return mv;
        }
    }
}
