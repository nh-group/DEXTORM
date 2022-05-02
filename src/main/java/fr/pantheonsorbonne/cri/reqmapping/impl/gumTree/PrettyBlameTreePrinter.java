package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PrettyBlameTreePrinter implements TreeVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrettyBlameTreePrinter.class);
    private final TreeContext ctx;
    String tabs = "";

    public PrettyBlameTreePrinter(TreeContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void startTree(Tree tree) {
        tabs += "\t";
        LOGGER.info(tabs + tree.toTreeString() + tree.getMetadata(GumTreeFacade.BLAME_ID));

    }

    @Override
    public void endTree(Tree tree) {
        tabs = tabs.substring(0, tabs.length() - 1);

    }
}