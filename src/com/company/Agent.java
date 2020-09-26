package com.company;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] strings = agentArgs.split(":");
        String configClass = strings[0];
        String password = strings[1];

        Class clazz = Class.forName(configClass);
        Config config = (Config) clazz.newInstance();
        config.setPassword(password);
        Transformer transformer = new Transformer(config);
        inst.addTransformer(transformer, true);

        Class[] loadedClasses = inst.getAllLoadedClasses();
        for (Class c : loadedClasses) {
            if (c.getName().equals(config.getClassName())) {
                try {
                    inst.retransformClasses(c);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
}