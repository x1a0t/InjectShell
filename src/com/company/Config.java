package com.company;

public class Config {
    public static String jvmKeyword;
    protected String password;
    protected String className;
    protected String methodName;
    protected String code;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getCode() {
        return code.replace("PASSWORD", getPassword());
    }
}
