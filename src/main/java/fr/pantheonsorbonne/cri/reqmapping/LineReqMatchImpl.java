package fr.pantheonsorbonne.cri.reqmapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;

import java.util.Objects;
import java.util.function.Predicate;

public class LineReqMatchImpl extends ReqMatchImpl implements Comparable<LineReqMatchImpl> {
    @JsonIgnore
    private final Integer line;
    private final Integer pos;
    private final Integer len;

    public LineReqMatchImpl(String className, String packageName, Integer line, Integer pos, Integer len, String[] reqs) {
        super(className, packageName, reqs);
        this.line = line;
        this.pos = pos;
        this.len = len;
    }

    @Override
    protected boolean isMatchLogged(StackTraceElement elt) {
        return super.isMatchFQClass(elt)
                && (elt.getLine() == this.getLine())
                && this.getRequirementsIds().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public Integer getLine() {
        return line;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineReqMatchImpl)) return false;
        LineReqMatchImpl that = (LineReqMatchImpl) o;
        return Objects.equals(getLine(), that.getLine()) && Objects.equals(pos, that.pos) && Objects.equals(len, that.len);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLine(), pos, len);
    }

    @Override
    public String toString() {
        return "LineReqMatchImpl{" +
                "line=" + line +
                ", pos=" + pos +
                ", len=" + len +
                '}';
    }

    @Override
    public int compareTo(LineReqMatchImpl lineReqMatch) {
        int p = this.packageName.compareTo(lineReqMatch.packageName);
        int c = this.className.compareTo(lineReqMatch.className);
        int l = this.line.compareTo(lineReqMatch.line);
        int pos = this.pos.compareTo(lineReqMatch.pos);
        if (p == 0) {
            if (c == 0) {
                if (l == 0) {
                    return pos;
                } else {
                    return l;
                }
            } else {
                return c;
            }
        } else {
            return p;
        }
    }
}
