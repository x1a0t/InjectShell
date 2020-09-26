package com.company;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
    Config config;
    public Transformer(Config config) {
        this.config = config;
    }

    @Override
    public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) {
        if (config.getClassName().replace(".", "/").equals(s)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                if (aClass != null) {
                    ClassClassPath classPath = new ClassClassPath(aClass);  //get current class's classpath
                    cp.insertClassPath(classPath);  //get current class's classpath
                }
                CtClass cc = cp.get(config.getClassName());
                CtMethod m = cc.getDeclaredMethod(config.getMethodName());
//                m.addLocalVariable("elapsedTime", CtClass.longType);
                m.insertBefore(config.getCode());
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