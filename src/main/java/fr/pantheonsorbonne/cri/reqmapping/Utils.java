package fr.pantheonsorbonne.cri.reqmapping;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static List<String> getIssueIdFromCommits(String message) {
        List<String> res = new ArrayList<>();
        final Pattern p = Pattern.compile("#(\\d+)");
        Matcher m = p.matcher(message);
        while (m.find()) {
            res.add(m.group(1));
        }
        return res;
    }
}
