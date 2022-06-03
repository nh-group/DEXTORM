package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import fr.pantheonsorbonne.cri.reqmapping.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GumTreeFileRequirementMappingProviderTest {

    @Test
    void getIssueIdFromCommitsTest() {
        List<String> res = Utils.getIssueIdFromCommits("salut #1 #2 fixed");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("1", res.get(0));
        Assertions.assertEquals("2", res.get(1));

    }
}