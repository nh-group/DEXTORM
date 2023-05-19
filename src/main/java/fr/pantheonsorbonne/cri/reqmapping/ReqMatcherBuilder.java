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
    private final List<String> issueIds = new ArrayList<>();
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
            this.issueIds.add(issue);
        }

        return this;
    }

    public ReqMatch build() {

        if (line == null) {
            return new MethodReqMatchImpl(this.className, this.packageName, this.methodName, this.args, this.issueIds.toArray(new String[0]));
        } else if (this.methodName == null) {
            return new LineReqMatchImpl(this.className, this.packageName, this.line, this.pos,this.len,this.issueIds.toArray(new String[0]));
        } else {
            return new CompositeReqMatchImpl(new MethodReqMatchImpl(this.className, this.packageName, this.methodName, this.args, this.issueIds.toArray(new String[0])), new LineReqMatchImpl(this.className, this.packageName, this.line, this.pos,this.len,this.issueIds.toArray(new String[0])));
        }

    }


    public ReqMatcherBuilder issues(Collection<String> commits) {

        this.issueIds.addAll(
                commits.stream().filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()));
        return this;
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

    public ReqMatcherBuilder getCopy() {
        ReqMatcherBuilder res = new ReqMatcherBuilder();
        res.args.addAll(this.args);
        res.issueIds.addAll(this.issueIds);
        res.className = this.className;
        res.line = this.line;
        res.methodName = this.methodName;
        res.packageName = this.packageName;
        res.pos=this.pos;
        res.len=this.len;
        return res;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<String> getReqs() {
        return issueIds;
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
