package ru.otus.hw04;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                return addProxyMethod(classfileBuffer);
            }
        });

    }

    private static byte[] addProxyMethod(byte[] originalClass) {
        ClassReader cr=new ClassReader(originalClass);
        ClassNode classNode=new ClassNode();
        cr.accept(classNode, 0);

        for(MethodNode methodNode : classNode.methods){
            boolean hasAnnotation=false;
            if(methodNode.visibleAnnotations!=null){
                for(AnnotationNode annotationNode : methodNode.visibleAnnotations){
                    if(annotationNode.desc.equals("Lru/otus/hw04/Log;")){
                        hasAnnotation=true;
                        break;
                    }
                }
            }
            if(hasAnnotation){
                InsnList beginList=new InsnList();
                beginList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                beginList.add(new LdcInsnNode("executed method: "));
                beginList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V"));
                beginList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                beginList.add(new LdcInsnNode(methodNode.name));
                beginList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V"));
                beginList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                beginList.add(new LdcInsnNode(", params:"));
                beginList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V"));

                char[] arguments = parseMethodArguments(methodNode.desc);

                int index = isStatic(methodNode) ? 0 : 1; //if it is not static, first arg is this

                for(int i = 0; i < arguments.length; i++) {
                    char letter = arguments[i];
                    beginList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    beginList.add(new VarInsnNode(getOpcodeFromLetter(letter), index));
                    beginList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", getDescriptorFromLetter(letter)));

                    if(i != arguments.length -1) {
                        beginList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        beginList.add(new LdcInsnNode(", "));
                        beginList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V"));
                    }
                    else {
                        beginList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        beginList.add(new LdcInsnNode("\n"));
                        beginList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V"));
                    }
                    if(letter=='J' || letter =='D')
                        index++;

                    index++;
                }
                methodNode.instructions.insert(beginList);
            }
        }
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        byte[] finalClass = cw.toByteArray();
        return finalClass;
    }


    //replaces the type descriptors for arrays and objects with a capital L, and leaves the type descriptors for primitives as is
    public static char[] parseMethodArguments(String desc) {
        String[] splitDesc = splitMethodDesc(desc);
        char[] returnChars = new char[splitDesc.length];
        int count = 0;
        for(String type : splitDesc) {
            if(type.startsWith("L") || type.startsWith("[")) {
                returnChars[count] = 'L';
            }
            else {
                if(type.length() > 1) { throw new RuntimeException("Undefined type"); }
                returnChars[count] = type.charAt(0);
            }
            count += 1;
        }
        return returnChars;
    }

    //split to arguments
    public static String[] splitMethodDesc(String desc) {
        int arraylen = desc.length();
        int beginIndex = desc.indexOf('(');
        int endIndex = desc.lastIndexOf(')');
        if((beginIndex == -1 && endIndex != -1) || (beginIndex != -1 && endIndex == -1)) {
            System.err.println(beginIndex);
            System.err.println(endIndex);
            throw new RuntimeException();
        }
        String x0;
        if(beginIndex == -1 && endIndex == -1) {
            x0 = desc;
        }
        else {
            x0 = desc.substring(beginIndex + 1, endIndex);
        }
        Pattern pattern = Pattern.compile("\\[*L[^;]+;|\\[[ZBCSIFDJ]|[ZBCSIFDJ]"); //Regex for desc \[*L[^;]+;|\[[ZBCSIFDJ]|[ZBCSIFDJ]
        Matcher matcher = pattern.matcher(x0);
        String[] listMatches = new String[arraylen];
        int counter = 0;
        while(matcher.find()) {
            listMatches[counter] = matcher.group();
            counter += 1;
        }
        String[] result = new String[counter];
        System.arraycopy(listMatches, 0, result, 0, counter);
        return result;
    }

    private static boolean isStatic (MethodNode methodNode)
    {
        return !methodNode.localVariables.get(0).name.equals("this");
    }

    private static String getDescriptorFromLetter (char c)
    {
        if(c=='L')
            return "(Ljava/lang/Object;)V";
        if(c=='B'||c=='S')
            c = 'I';
        return "("+c+")V";
    }
    private static int getOpcodeFromLetter (char c)
    {
        int opcode = 0;

        switch(c) {
            case 'L':
                opcode = Opcodes.ALOAD;
                break;
            case 'I':
            case 'C':
            case 'B':
            case 'S':
                opcode = Opcodes.ILOAD;
                break;
            case 'J':
                opcode = Opcodes.LLOAD;
                break;
            case 'F':
                opcode = Opcodes.FLOAD;
                break;
            case 'D':
                opcode = Opcodes.DLOAD;
                break;
        }

        return opcode;
    }

}