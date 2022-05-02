package fr.pantheonsorbonne.cri.reqmapping;

public class StackTraceElement {
    private final String className;
    private final String packageName;
    private final String methodName;
    private final String sourceFileName;
    private final String methodArgs;
    private final Integer line;

    public StackTraceElement(String sourceFileName, String packageName, String className, String methodName, String args, Integer line) {
        this.className = className;
        this.packageName = packageName;
        this.methodName = methodName;
        this.methodArgs = args;
        this.sourceFileName = sourceFileName;
        this.line = line;
    }

    public static StackTraceElement build(java.lang.StackTraceElement e) {
        return new StackTraceElement(e.getFileName(), e.getModuleName(), e.getClassName(), e.getMethodName(), "", e.getLineNumber());
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public Integer getLine() {
        return line;
    }
}
