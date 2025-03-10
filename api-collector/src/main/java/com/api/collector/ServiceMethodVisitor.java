//package com.api.collector;
//
//public class ServiceMethodVisitor {
//}

package com.api.collector;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ServiceMethodVisitor extends MethodVisitor {
    public ServiceMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @Override
    public void visitCode() {
        mv.visitVarInsn(Opcodes.ALOAD, 1); // HttpServletRequest
        mv.visitVarInsn(Opcodes.ALOAD, 2); // HttpServletResponse
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "com/api/collector/ParamCollector",
                "captureRequest",
                "(Ljavax/servlet/http/HttpServletRequest;" +
                        "Ljavax/servlet/http/HttpServletResponse;)V",
                false);
        super.visitCode();
    }
}