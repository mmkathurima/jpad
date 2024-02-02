package io.jpad.model;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JreClassGrabber {


    private static final ArrayListMultimap<String, String> shortToFullNames = ArrayListMultimap.create();
    private static final Map<String, String> singles = new HashMap<>();
    private static final Map<String, String> guesses = new HashMap<>();

    private static final Set<String> IGNORED_PACKAGES_PREFIXES = Sets.newHashSet("com.timestored", "org.jfree", "com.sun");

    private static final List<String> PREFERRED_PACKAGE_PREFIXES = Lists.newArrayList("lombok", "java.util", "java", "com.google");
    private static final JreClassGrabber INSTANCE = new JreClassGrabber();


    private JreClassGrabber() {

        ClassFinder.findClasses(new Visitor<String>() {

            public boolean visit(String c) {

                if (c != null && !c.contains("$")) {

                    boolean ignore = false;

                    for (String igPack : JreClassGrabber.IGNORED_PACKAGES_PREFIXES) {

                        if (c.startsWith(igPack)) {

                            ignore = true;

                            break;

                        }

                    }

                    if (!ignore) {

                        JreClassGrabber.shortToFullNames.put(JreClassGrabber.getShortName(c), c);

                    }

                }

                return true;

            }

        });


        for (String k : shortToFullNames.keySet()) {

            List<String> fullNames = shortToFullNames.get(k);

            if (fullNames.size() == 1) {

                singles.put(k, fullNames.get(0));

                continue;

            }

            String chosenFN = fullNames.get(0);

            int chosenIdx = findFirstIdxWithMatchingPrefix(chosenFN, PREFERRED_PACKAGE_PREFIXES);


            for (String fn : fullNames) {

                int idx = findFirstIdxWithMatchingPrefix(fn, PREFERRED_PACKAGE_PREFIXES);


                if (idx <= chosenIdx) {

                    if (idx == chosenIdx && fn.length() < chosenFN.length()) {

                        chosenFN = fn;
                        continue;

                    }

                    chosenFN = fn;

                    chosenIdx = idx;

                }

            }


            guesses.put(k, chosenFN);

        }


        guesses.put("List", "java.util.List");

        guesses.put("Date", "java.util.Date");

        guesses.put("JPad", "io.jpad.scratch.JPad");

    }


    private static int findFirstIdxWithMatchingPrefix(String st, List<String> prefixes) {

        for (int i = 0; i < prefixes.size(); i++) {

            if (st.startsWith(prefixes.get(i))) {

                return i;

            }

        }

        return prefixes.size();

    }


    public static JreClassGrabber getInstance() {

        return INSTANCE;

    }


    private static String getPackageName(String fullyQualifiedClassName) {

        String s = fullyQualifiedClassName;

        if (s.contains(".")) {

            return s.substring(0, s.lastIndexOf("."));

        }

        return s;

    }


    public static String getShortName(String fullyQualifiedClassName) {

        String s = fullyQualifiedClassName;

        if (s.contains(".")) {

            return s.substring(1 + s.lastIndexOf("."));

        }

        return s;

    }


    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof JreClassGrabber)) return false;
        JreClassGrabber other = (JreClassGrabber) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof JreClassGrabber;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "JreClassGrabber()";
    }


    public Map<String, String> getOneToOneMappings() {

        return singles;

    }

    public Map<String, String> getGuesses() {

        return guesses;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\JreClassGrabber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */