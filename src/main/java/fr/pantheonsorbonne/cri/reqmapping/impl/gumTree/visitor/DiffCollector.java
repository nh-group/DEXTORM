package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RenameCallback;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DiffCollector extends RenameCallback {
    List<DiffEntry> diffs = new ArrayList<>();

    @Override
    public void renamed(DiffEntry diff) {
        diffs.add(diff);
    }

    public DiffCollector(Repository repo) {
        this.repo = repo;
    }

    Repository repo;

    public void loadFileHistory(String filepath,List<RevCommit> revCommits, Deque<String> names) {

        Config config = repo.getConfig();
        config.setBoolean("diff", null, "renames", true);

        RevWalk rw = new RevWalk(repo);

        try {
            org.eclipse.jgit.diff.DiffConfig dc = config.get(DiffConfig.KEY);
            FollowFilter followFilter =
                    FollowFilter.create(filepath, dc);
            followFilter.setRenameCallback(this);
            rw.setTreeFilter(followFilter);
            rw.markStart(rw.parseCommit(repo.resolve(Constants.HEAD)));

            revCommits.addAll(StreamSupport.stream(Spliterators.spliteratorUnknownSize(rw.iterator(), Spliterator.DISTINCT), false).collect(Collectors.toList()));
            names.add(filepath);
            names.addAll(this.diffs.stream().map(d->d.getOldPath()).collect(Collectors.toList()));

        } catch (AmbiguousObjectException e) {
            throw new RuntimeException(e);
        } catch (IncorrectObjectTypeException e) {
            throw new RuntimeException(e);
        } catch (MissingObjectException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            rw.dispose();
        }

    }
}
