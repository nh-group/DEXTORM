package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        //method args in jacoco report includes the FQName of the class, we don't have that in GumTree, so let's remove it
        //it could be a problem for homonymous classes
        List<String> argsAsList = ReqMatcherBuilder.strArgsToList(args);
        argsAsList = argsAsList.stream().map(arg -> arg.charAt(0) + Arrays.stream(arg.split("/")).reduce((f, s) -> s).orElse("")).collect(Collectors.toList());
        this.methodArgs = cleanupStringArgs(args);
        this.sourceFileName = sourceFileName;
        this.line = line;
    }

    private static String cleanupStringArgs(String args) {

        StringBuilder sb = new StringBuilder("(");
        Pattern pattern = Pattern.compile("\\((.*)\\).*");
        Matcher matcher = pattern.matcher(args);
        if (matcher.matches()) {
            args = matcher.group(1);
            for (String arg : args.split(";")) {
                Pattern patternArg = Pattern.compile("(\\[*)([ZBCSIJFDL])(.*)");
                Matcher matcher1 = patternArg.matcher(arg);
                if (matcher1.matches()) {
                    if (matcher1.group(1) != null) {
                        sb.append(matcher1.group(1));
                    }
                    if (matcher1.group(2) != null) {
                        sb.append(matcher1.group(2));
                    }
                    if (matcher1.group(3) != null) {
                        sb.append(Arrays.stream(matcher1.group(3).split("/")).reduce((f, s) -> s).get());
                    }
                    sb.append(";");
                }
            }

        }
        sb.substring(0, sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }

    public static StackTraceElement build(java.lang.StackTraceElement e) {
        return new StackTraceElement(e.getFileName(), e.getModuleName(), e.getClassName(), e.getMethodName(), "", e.getLineNumber());
    }

    public String getFQClassName() {
        return this.getPackageName() + "." + this.getClassName();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClassName(), getPackageName(), getMethodName(), getSourceFileName(), getMethodArgs(), getLine());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StackTraceElement)) return false;
        StackTraceElement that = (StackTraceElement) o;
        return getClassName().equals(that.getClassName()) && getPackageName().equals(that.getPackageName()) && getMethodName().equals(that.getMethodName()) && getSourceFileName().equals(that.getSourceFileName()) && getMethodArgs().equals(that.getMethodArgs()) && getLine().equals(that.getLine());
    }

    @Override
    public String toString() {
        return packageName + "." + className + "." + methodName + "(" + line + "): ";
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
