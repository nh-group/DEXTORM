package fr.pantheonsorbonne.cri.reqmapping.impl.blame;

import com.github.javaparser.Position;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.GitBlameFileRequirementProvider.BlameDataWrapper;

import java.util.*;
import java.util.stream.Collectors;

public class ReqMatcherJavaVisitor extends VoidVisitorAdapter<BlameDataWrapper> {

    private final Set<ReqMatch> reqMatchers = new HashSet<>();
    private String className = "";
    private String packageName = "";

    @Override
    public void visit(BlockStmt n, BlameDataWrapper arg) {
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, BlameDataWrapper arg) {

        className = cd.getNameAsString();
        super.visit(cd, arg);
    }

    @Override
    public void visit(MethodDeclaration md, BlameDataWrapper wraper) {
        super.visit(md, wraper);
        Optional<Position> pos = md.getBegin();
        String fqClassName = this.packageName + "." + this.className;
        if ((pos).isPresent()) {
            if (wraper.blameData.containsKey(fqClassName)) {

                List<String> args = md.getParameters().stream().map((Parameter p) -> p.getTypeAsString())
                        .collect(Collectors.toList());
                Collection<String> commitId = wraper.blameData.get(fqClassName).get(pos.get().line);
                if (commitId != null) {
                    reqMatchers.add(ReqMatch.newBuilder()
                            .className(this.className)
                            .packageName(this.packageName)
                            .methodName(md.getNameAsString())
                            .line(pos.get().line)
                            .args(args).commits(commitId).build());
                }

            }

        }

    }

    @Override
    public void visit(PackageDeclaration pakage, BlameDataWrapper arg) {
        this.packageName = pakage.getNameAsString();
        super.visit(pakage, arg);
    }

    public Collection<ReqMatch> getMatchers() {
        return this.reqMatchers;
    }
}
