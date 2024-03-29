package fr.pantheonsorbonne.cri.reqmapping.impl.blame;

import com.github.javaparser.Position;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatchImpl;
import fr.pantheonsorbonne.cri.reqmapping.impl.Utils;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor.CantComputeParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BlameMethodDeclarationVisitor extends VoidVisitorAdapter<BlameDataWrapper> {

    private static Logger LOGGER = LoggerFactory.getLogger("BlameMethodDeclarationVisitor");
    public static final String INIT = "<init>";
    private final Set<ReqMatch> reqMatcherImpls = new HashSet<>();
    private String className = "";
    private String packageName = "";

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, BlameDataWrapper arg) {

        className = cd.getNameAsString();
        super.visit(cd, arg);
    }

    @Override
    public void visit(final ConstructorDeclaration cd, BlameDataWrapper wraper) {
        super.visit(cd, wraper);
        Optional<Position> pos = cd.getBegin();
        String fqClassName = getFqClassName();
        if ((pos).isPresent()) {
            if (wraper.blameData.containsKey(fqClassName)) {

                List<String> args = null;
                try {
                    args = extractArgsAsStringList(cd);

                    Collection<String> commitId = wraper.blameData.get(fqClassName).get(pos.get().line);
                    if (commitId != null) {
                        reqMatcherImpls.add(ReqMatchImpl.newBuilder()
                                .className(this.className)
                                .packageName(this.packageName)
                                .methodName(INIT)
                                .args(args)
                                .commits(commitId).build());
                    }
                } catch (CantComputeParameterException e) {
                    LOGGER.error("failed to compute method signature {}. Method was ignored", e.getMessage());
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public void visit(MethodDeclaration md, BlameDataWrapper wraper) {
        super.visit(md, wraper);
        Optional<Position> pos = md.getBegin();
        String fqClassName = getFqClassName();
        if ((pos).isPresent()) {
            if (wraper.blameData.containsKey(fqClassName)) {

                List<String> args = null;
                try {
                    args = extractArgsAsStringList(md);

                    Collection<String> commitId = wraper.blameData.get(fqClassName).get(pos.get().line);
                    if (commitId != null) {
                        reqMatcherImpls.add(ReqMatchImpl.newBuilder()
                                .className(this.className)
                                .packageName(this.packageName)
                                .methodName(md.getNameAsString())
                                //.line(pos.get().line) //no line info required for method declaration
                                .args(args)
                                .commits(commitId).build());
                    }
                } catch (CantComputeParameterException e) {
                    LOGGER.error("failed to compute method signature {}. Method was ignored", e.getMessage());
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void visit(PackageDeclaration pakage, BlameDataWrapper arg) {
        this.packageName = pakage.getNameAsString();
        super.visit(pakage, arg);
    }

    private String getFqClassName() {
        return this.packageName + "." + this.className;
    }

    private static List<String> extractArgsAsStringList(CallableDeclaration<?> md) throws CantComputeParameterException {
        try {
            return md.getParameters().stream().map(parameter -> {
                        Type type = null;
                        StringBuilder sb = new StringBuilder();
                        if (parameter.getType().isArrayType()) {
                            sb.append("[");
                            type = parameter.getType().getElementType();
                        } else {
                            type = parameter.getType();
                        }

                        if (type.isClassOrInterfaceType()) {
                            sb.append("L");

                            sb.append(((ClassOrInterfaceType) type).getName());
                        } else {
                            try {
                                sb.append(Utils.typeToCodeJVM(type.toString()));
                            } catch (CantComputeParameterException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return sb.toString();
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new CantComputeParameterException(e);
        }
    }

    public Collection<ReqMatch> getMatchers() {
        return this.reqMatcherImpls;
    }
}
