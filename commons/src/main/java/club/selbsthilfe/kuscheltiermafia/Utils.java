package club.selbsthilfe.kuscheltiermafia;

import java.util.Set;

public class Utils {

    public static String getTag(Set<String> tags, String key){
        return tags.stream()
                .filter(tag -> tag.startsWith(key + ":"))
                .map(tag -> tag.substring((key + ":").length()))
                .findFirst()
                .orElseThrow();
    }

}
