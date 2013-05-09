package info.archinnov.achilles.demo.twitter.service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class TweetParsingService {

    private static final Pattern TAG_PATTERN = Pattern.compile("#(\\w+)");
    private static final Pattern USER_PATTERN = Pattern.compile("@(\\w+)");

    public Set<String> extractTags(String content) {
        Set<String> tagSet = new HashSet<String>();

        Matcher matcher = TAG_PATTERN.matcher(content);
        while (matcher.find()) {
            String tag = matcher.group(1);
            assert tag != null && !tag.isEmpty() && !tag.contains("#");
            if (!tagSet.contains(tag)) {
                tagSet.add(tag);
            }
        }
        return tagSet;
    }

    public Set<String> extractLogins(String content) {
        Set<String> loginSet = new HashSet<String>();

        Matcher matcher = USER_PATTERN.matcher(content);
        while (matcher.find()) {
            String tag = matcher.group(1);
            assert tag != null && !tag.isEmpty() && !tag.contains("#");
            if (!loginSet.contains(tag)) {
                loginSet.add(tag);
            }
        }
        return loginSet;
    }
}
