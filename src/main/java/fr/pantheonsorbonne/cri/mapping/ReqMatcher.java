package fr.pantheonsorbonne.cri.mapping;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

public class ReqMatcher {

	@Override
	public String toString() {
		Optional<String> params = this.args.stream().reduce((String a, String b) -> a + "," + b);
		String commits = this.commits.stream().filter((String s) -> s != null)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
				.map((Entry<String, Long> entry) -> entry.getKey() + ":" + entry.getValue())
				.collect(Collectors.joining(" ,"));

		return new StringBuilder().append(this.getFQClassName()).append("::").append(this.methodName).append(" (")
				.append(params.isPresent() ? params.get() : " void ").append(" )").append("//").append(commits)
				.toString();
	}

	public String getClassName() {
		return this.className;
	}

	public String getFQClassName() {
		return this.packageName.isEmpty() ? className : this.packageName + "." + className;
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

	public java.util.List<String> getReq() {
		return commits;
	}

	public void setReq(java.util.List<String> req) {
		this.commits.clear();
		this.commits.addAll(req);
	}

	private String className;
	private String methodName;
	private Integer line;
	private ArrayList<String> commits = new ArrayList();
	private ArrayList<String> args = new ArrayList<>();
	private String packageName = "";

	public static class ReqMatcherBuilder implements Cloneable {

		private ReqMatcher buildee = new ReqMatcher();

		public ReqMatcherBuilder className(String name) {
			this.buildee.setClassName(name);
			return this;
		}

		public ReqMatcherBuilder methodName(String name) {
			this.buildee.setMethodName(name);
			return this;
		}

		public ReqMatcherBuilder args(java.util.List<String> args) {
			this.buildee.setArgs(args);
			return this;
		}

		public ReqMatcherBuilder arg(String args) {
			this.buildee.getArgs().add(args);
			return this;
		}

		public ReqMatcherBuilder commit(String commit) {
			if (!Strings.isNullOrEmpty(commit)) {
				this.buildee.getReq().add(commit);
			}

			return this;
		}

		public ReqMatcher build() {
			return buildee;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object clone() {
			return ReqMatcher.newBuilder().className(this.buildee.getClassName()).methodName(this.buildee.methodName)
					.packageName(this.buildee.packageName).args((ArrayList<String>) this.buildee.args.clone())
					.commits((ArrayList<String>) this.buildee.commits.clone());
		}

		public ReqMatcherBuilder commits(Collection<String> commits) {

			this.buildee.getReq().addAll(
					commits.stream().filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()));
			return this;
		}

		public ReqMatcherBuilder packageName(String label) {
			this.buildee.packageName = label;
			return this;

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
		this.setArgs((java.util.List<String>) args);
		this.line = line;
		commits.addAll(Arrays.asList(reqs));

	}

	public Collection<String> getArgs() {
		return args;
	}

	public void setArgs(java.util.List<String> args) {
		this.args.clear();
		this.args.addAll(args);

	}

}
