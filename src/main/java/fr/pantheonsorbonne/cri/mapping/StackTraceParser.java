package fr.pantheonsorbonne.cri.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

import com.google.common.base.Strings;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;

public class StackTraceParser {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	private StackTraceElement[] elements;
	private ApplicationParameters vars;
	private Collection<ReqMatcher> reqMatchers;

	public StackTraceParser(StackTraceElement[] elements, DemoApplicationParameters vars,
			Collection<ReqMatcher> reqMatchers) {
		this.elements = elements;
		this.vars = vars;
		this.reqMatchers = reqMatchers;
	}

	private static boolean match(StackTraceElement elt, ReqMatcher m) {
		return m.getFQClassName().equals(elt.getClassName())
				&& m.getMethodName().equals(elt.getMethodName().split("\\$")[0])
				&& m.getReq().stream().filter(Predicate.not(Strings::isNullOrEmpty)).count() > 0;

	}

	public Collection<String> getReqs() {

		Collection<String> res = new HashSet<>();
		for (StackTraceElement elt : elements) {
			if (elt.getClassName().startsWith(this.vars.getInstrumentedPackage())) {

				for (ReqMatcher m : reqMatchers) {
					if (match(elt, m)) {
						res.addAll(m.getReq());
					}
				}

			}
		}
		return res;

	}
}
