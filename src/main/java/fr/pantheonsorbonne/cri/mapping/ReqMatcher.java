package fr.pantheonsorbonne.cri.mapping;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ReqMatcher {
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public Set<String> getReq() {
		return req;
	}

	public void setReq(Set<String> req) {
		this.req = req;
	}

	private String className;
	private String methodName;
	private Integer line;
	private Set<String> req = new HashSet<>();
	private ArrayList<String> args = new ArrayList<>();

	public static class ReqMatcherBuilder implements Cloneable {

		ReqMatcher buildee = new ReqMatcher();

		public ReqMatcherBuilder className(String name) {
			this.buildee.setClassName(name);
			return this;
		}

		public ReqMatcherBuilder methodName(String name) {
			this.buildee.setMethodName(name);
			return this;
		}

		public ReqMatcherBuilder args(ArrayList<String> args) {
			this.buildee.setArgs(args);
			return this;
		}
		
		public ReqMatcherBuilder arg(String args) {
			this.buildee.getArgs().add(args);
			return this;
		}

		public ReqMatcher build() {
			return buildee;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object clone() {
			return ReqMatcher.newBuilder().className(this.buildee.getClassName()).methodName(this.buildee.methodName)
					.args((ArrayList<String>) this.buildee.args.clone());
		}
	}

	public static ReqMatcherBuilder newBuilder() {
		return new ReqMatcherBuilder();
	}

	protected ReqMatcher() {

	}

	public ReqMatcher(String className, String methodName, Collection<String> args, Integer line, String... reqs) {
		this.className = className;
		this.methodName = methodName;
		this.setArgs((ArrayList<String>) args);
		this.line = line;
		req.addAll(Arrays.asList(reqs));

	}

	public Collection<String> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<String> args) {
		this.args = args;
	}

}
