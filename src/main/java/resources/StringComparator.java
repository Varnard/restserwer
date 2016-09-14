package resources;

import java.util.Comparator;

/**
 * Created by buzalskim on 2016-09-14.
 */
public class StringComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {

        if (s1 == s2) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }

        String obj1 = s1.toLowerCase();
        String obj2 = s2.toLowerCase();
        
        return obj1.compareTo(obj2);
    }
}

