package fr.pantheonsorbonne.cri.mapping.impl.diff;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.Position;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.impl.diff.BlameVisitorAdaptor.BlameDataWrapper;

public class ReqMatcherJavaVisitor extends VoidVisitorAdapter<BlameDataWrapper> {

	private String className = "";

	private Set<ReqMatcher> reqMatchers = new HashSet<>();

	@Override
	public void visit(PackageDeclaration pakage, BlameDataWrapper arg) {

		className += pakage.getNameAsString();
		super.visit(pakage, arg);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration cd, BlameDataWrapper arg) {

		className += "." + cd.getNameAsString();
		super.visit(cd, arg);
	}

	@Override
	public void visit(MethodDeclaration md, BlameDataWrapper wraper) {
		super.visit(md, wraper);
		Optional<Position> pos = md.getBegin();
		if ((pos).isPresent()) {
			if (wraper.blameData.containsKey(this.className)) {

				List<String> args = md.getParameters().stream().map((Parameter p) -> p.getTypeAsString())
						.collect(Collectors.toList());
				String commitId = wraper.blameData.get(this.className).get(pos.get().line);
				reqMatchers.add(ReqMatcher.newBuilder().className(this.className).methodName(md.getNameAsString())
						.args(args).commit(commitId).build());

			}

		}

	}

	@Override
	public void visit(BlockStmt n, BlameDataWrapper arg) {
		super.visit(n, arg);
	}

	public Collection<ReqMatcher> getMatchers() {
		return this.reqMatchers;
	}
}
