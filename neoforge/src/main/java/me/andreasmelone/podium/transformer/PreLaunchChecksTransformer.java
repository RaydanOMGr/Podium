package me.andreasmelone.podium.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.RETURN;

public class PreLaunchChecksTransformer {
    public static final String CLASSNAME_DOTTED = "net.caffeinemc.mods.sodium.client.compatibility.checks.PreLaunchChecks";
    public static final String CLASSNAME_SLASHED = CLASSNAME_DOTTED.replace(".", "/");

    public byte[] transform(byte[] classfileBuffer) {
        System.out.println("[Transformer] Transforming " + CLASSNAME_DOTTED);

        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ReturnInjector injector = new ReturnInjector(writer, "checkLwjglRuntimeVersion", "()V");
        reader.accept(injector, 0);

        return writer.toByteArray();
    }

    public class ReturnInjector extends ClassVisitor {
        private final String methodName;
        private final String methodDesc;

        public ReturnInjector(ClassVisitor cv, String methodName, String methodDesc) {
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
                        super.visitInsn(RETURN);
                    }
                };
            }
            return mv;
        }
    }
}
