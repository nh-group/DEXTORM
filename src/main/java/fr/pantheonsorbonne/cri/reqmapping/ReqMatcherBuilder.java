package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReqMatcherBuilder implements Cloneable {

    private static final Pattern methodSignaturePattern = Pattern.compile("\\(((?:[^;]*;)*)\\)(\\[?[ZBCSIJFDLV].*)");
    private static final Pattern methodParamsPattern = Pattern.compile("(\\[)?([ZBCSIJFDL])?([^;]*)?;");
    private final List<String> args = new ArrayList<>();
    private final List<String> reqs = new ArrayList<>();

    private final List<String> issues = new ArrayList<>();
    private String packageName = null;
    private String methodName = null;
    private String className = null;
    private Integer line;

    public Integer getLen() {
        return len;
    }

    public Integer getPos() {
        return pos;
    }

    private Integer len;
    private Integer pos;

    protected ReqMatcherBuilder() {

    }

    public ReqMatcherBuilder arg(String arg) {
        this.args.add(arg);
        return this;
    }

    public ReqMatcherBuilder args(List<String> args) {
        this.args.addAll(args);
        return this;
    }

    public ReqMatcherBuilder args(String argsList) {
        this.args.addAll(strArgsToList(argsList));
        return this;

    }

    public static List<String> strArgsToList(String argsList) {
        //https://stackoverflow.com/questions/8066253/compute-a-java-functions-signature
        List<String> res = new ArrayList<>();
        Matcher matcher = methodSignaturePattern.matcher(argsList);
        if (matcher.matches()) {
            String params = matcher.group(1);
            Matcher matcher2 = methodParamsPattern.matcher(params);
            while (matcher2.find()) {
                StringBuilder sb = new StringBuilder();
                var array = matcher2.group(1);
                var type = matcher2.group(2);
                var classType = matcher2.group(3);
                if (array != null) {
                    sb.append(array);
                }
                sb.append(type);
                if (classType != null) {
                    sb.append(classType);
                }
                res.add(sb.toString());
            }
        }
        return res;
    }

    public ReqMatcherBuilder issue(String issue) {
        if (!Strings.isNullOrEmpty(issue)) {
            this.issues.add(issue);
        }

        return this;
    }

    public ReqMatch build() {
        if (this.className == null) {
            return new PackageReqMatcher("", this.packageName, this.issues.toArray(new String[0]), this.reqs.toArray(new String[0]));
        } else if (line == null && this.methodName != null) {
            return new MethodReqMatchImpl(this.className, this.packageName, this.methodName, this.args, this.issues.toArray(new String[0]), this.reqs.toArray(new String[0]));
        } else if (line != null ) {
            return new LineReqMatchImpl(this.className, this.packageName, this.line, this.pos, this.len, this.issues.toArray(new String[0]), this.reqs.toArray(new String[0]));
        }
        throw new RuntimeException("can't build an illegal matcher");

    }


    public ReqMatcherBuilder packageName(String label) {
        this.packageName = label;
        return this;

    }

    public ReqMatcherBuilder methodName(String name) {
        this.methodName = name;
        return this;
    }

    public ReqMatcherBuilder fQClassName(String name) {
        String[] bits = name.split("\\.");
        this.className = bits[bits.length - 1];
        this.packageName = Arrays.stream(Arrays.copyOf(bits, bits.length - 1)).collect(Collectors.joining("."));
        return this;
    }

    public ReqMatcherBuilder className(String name) {
        this.className = name;
        return this;
    }

    public ReqMatcherBuilder line(int lineNumber) {
        this.line = lineNumber;
        return this;
    }

    public ReqMatcherBuilder pos(int position) {
        this.pos = position;
        return this;
    }

    public ReqMatcherBuilder len(int length) {
        this.len = length;
        return this;
    }

    public ReqMatcherBuilder issues(String issueId) {
        if (!Strings.isNullOrEmpty(issueId)) {
            this.issues.add(issueId);
        }

        return this;
    }

    public ReqMatcherBuilder issues(Collection<String> issueId) {
        if (issueId != null) {
            this.issues.addAll(
                    issueId.stream().filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()));
        }
        return this;
    }


    public ReqMatcherBuilder commits(Collection<String> commits) {

        this.reqs.addAll(
                commits.stream().filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()));
        return this;
    }


    public ReqMatcherBuilder getCopy() {
        ReqMatcherBuilder res = new ReqMatcherBuilder();
        res.args.addAll(this.args);
        res.issues.addAll(this.issues);
        res.className = this.className;
        res.line = this.line;
        res.methodName = this.methodName;
        res.packageName = this.packageName;
        res.pos = this.pos;
        res.len = this.len;
        res.issues.addAll(this.issues);
        return res;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<String> getReqs() {
        return issues;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }
}
