package io.jpad.model;


import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.beans.ConstructorProperties;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;


public class JPadCode {
    public static final String FULL_CLASS_NAME = "io.jpad.scratch.RunnContainer";
    private static final String NAME = "RunnContainer";
    private static final String PKG = "io.jpad.scratch";
    private static final String DEFAULT_IMPORTS = "import static io.jpad.japl.Japl.*; import static io.jpad.japl.Predef.*;";
    private static final String PACKAGE_LINE = "package io.jpad.scratch;";
    private static final String CLASS_START = "public class RunnContainer {";
    private static final String MAIN_START = "public static void main(String... args) throws Exception {";
    private static final String N = "\r\n";
    private static final Logger log = Logger.getLogger(JPadCode.class.getName());
    private static final String IMP = "import ";
    private final String rawCode;

    private final String gradleCode;

    private final Set<String> userImports;

    private final Set<String> smartImports;
    private final String userImportString;
    private final int offsetToOriginalUserImports;
    private final int offsetToOriginalMainCode;
    private final String mainCode;
    private final GenerateResult generateResult;


    private JPadCode(String rawCode) {

        this.rawCode = Preconditions.checkNotNull(rawCode);


        int p = rawCode.indexOf("// JAVA");

        if (p == -1) {

            if (rawCode.startsWith("import ")) {

                p = 0;

            } else {

                p = rawCode.indexOf("\nimport ") + 1;

                p = Math.min(p, rawCode.indexOf("\timport ") + 1);

                p = Math.min(p, rawCode.indexOf("\rimport ") + 1);

                p = Math.min(p, rawCode.indexOf(" import ") + 1);

                p = Math.min(p, rawCode.indexOf(";import ") + 1);

                p = Math.max(0, p);

            }

        }

        String jCode = rawCode.substring(p);

        this.offsetToOriginalUserImports = p;

        int offsetToMainCode = p;


        String mCode = jCode;

        this.gradleCode = rawCode.substring(0, p);


        Set<String> userImports = Sets.newHashSet();

        String userImportString = "";

        if (jCode.contains("import ")) {

            int lastImportPos = jCode.lastIndexOf("import ");

            int breakPos = jCode.indexOf(";", lastImportPos);

            mCode = jCode.substring(breakPos + 1);

            offsetToMainCode += breakPos + 1;


            userImportString = jCode.substring(0, breakPos + 1);

            String[] importStrings = userImportString.split(";");

            for (String s : importStrings) {

                userImports.add(s.trim().replace("import ", ""));

            }

        }


        this.mainCode = mCode;

        this.userImportString = userImportString;

        this.userImports = userImports;

        this.offsetToOriginalMainCode = offsetToMainCode;


        this.smartImports = guessImports(jCode, userImports);


        this.generateResult = generateJavaFileSource();

    }


    public static JPadCode generate(String jpadCode) {

        return new JPadCode(jpadCode);

    }


    private static Set<String> guessImports(String javaCode, Set<String> userImports) {

        JreClassGrabber jreClassGrabber = JreClassGrabber.getInstance();


        Set<String> smartImports = new HashSet<>();

        Set<String> classNames = JavaTokeniser.getCodeWords(javaCode);

        for (String cn : classNames) {


            boolean userAlreadyImported = false;

            for (String uimp : userImports) {

                if (uimp.endsWith("." + cn)) {

                    userAlreadyImported = true;

                    break;

                }

            }

            if (!userAlreadyImported) {

                String fullClassName = jreClassGrabber.getOneToOneMappings().get(cn);

                if (fullClassName == null) {

                    fullClassName = jreClassGrabber.getGuesses().get(cn);

                }

                if (fullClassName != null) {

                    try {

                        Class<?> cls = Class.forName(fullClassName);

                        if (!Modifier.isPublic(cls.getModifiers())) {

                            fullClassName = null;

                        }

                    } catch (ClassNotFoundException e) {

                        fullClassName = null;

                    } catch (NoClassDefFoundError e) {

                        fullClassName = null;

                    }

                }


                if (fullClassName == null) {

                    log.fine("Couldn't find import for " + cn);
                    continue;

                }

                int p = fullClassName.lastIndexOf(".");

                String packageName = fullClassName.substring(0, p);


                if (!packageName.equals("java.lang")) {

                    smartImports.add(fullClassName);

                }

            }

        }


        return smartImports;

    }


    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof JPadCode)) return false;
        JPadCode other = (JPadCode) o;
        if (!other.canEqual(this)) return false;
        Object this$rawCode = getRawCode(), other$rawCode = other.getRawCode();
        if (!Objects.equals(this$rawCode, other$rawCode)) return false;
        Object this$gradleCode = getGradleCode(), other$gradleCode = other.getGradleCode();
        if (!Objects.equals(this$gradleCode, other$gradleCode))
            return false;
        Set<String> this$userImports = getUserImports(), other$userImports = other.getUserImports();
        if (!Objects.equals(this$userImports, other$userImports))
            return false;
        Set<String> this$smartImports = getSmartImports(), other$smartImports = other.getSmartImports();
        if (!Objects.equals(this$smartImports, other$smartImports))
            return false;
        if (!Objects.equals(this.userImportString, other.userImportString))
            return false;
        if (this.offsetToOriginalUserImports != other.offsetToOriginalUserImports) return false;
        if (this.offsetToOriginalMainCode != other.offsetToOriginalMainCode) return false;
        if (!Objects.equals(this.mainCode, other.mainCode)) return false;
        return Objects.equals(this.generateResult, other.generateResult);
    }

    protected boolean canEqual(Object other) {
        return other instanceof JPadCode;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $rawCode = getRawCode();
        result = result * 59 + (($rawCode == null) ? 0 : $rawCode.hashCode());
        Object $gradleCode = getGradleCode();
        result = result * 59 + (($gradleCode == null) ? 0 : $gradleCode.hashCode());
        Set<String> $userImports = getUserImports();
        result = result * 59 + (($userImports == null) ? 0 : $userImports.hashCode());
        Set<String> $smartImports = getSmartImports();
        result = result * 59 + (($smartImports == null) ? 0 : $smartImports.hashCode());
        Object $userImportString = this.userImportString;
        result = result * 59 + (($userImportString == null) ? 0 : $userImportString.hashCode());
        result = result * 59 + this.offsetToOriginalUserImports;
        result = result * 59 + this.offsetToOriginalMainCode;
        Object $mainCode = this.mainCode;
        result = result * 59 + (($mainCode == null) ? 0 : $mainCode.hashCode());
        Object $generateResult = this.generateResult;
        return result * 59 + (($generateResult == null) ? 0 : $generateResult.hashCode());
    }

    public String toString() {
        return "JPadCode(rawCode=" + getRawCode() + ", gradleCode=" + getGradleCode() + ", userImports=" + getUserImports() + ", smartImports=" + getSmartImports() + ", userImportString=" + this.userImportString + ", offsetToOriginalUserImports=" + this.offsetToOriginalUserImports + ", offsetToOriginalMainCode=" + this.offsetToOriginalMainCode + ", mainCode=" + this.mainCode + ", generateResult=" + this.generateResult + ")";
    }


    public String getRawCode() {

        return this.rawCode;
    }


    public String getGradleCode() {
        return this.gradleCode;
    }


    public Set<String> getUserImports() {
        return this.userImports;
    }

    public Set<String> getSmartImports() {

        return this.smartImports;

    }


    public String getGeneratedCode() {

        return this.generateResult.getGeneratedCode();

    }


    public int translateOffset(int newOffsetPosition) {

        int newMainO = this.generateResult.getOffsetToMainInGeneratedCode();

        int newImportO = this.generateResult.getOffsetToNewUserImports();

        int nop = newOffsetPosition;

        if (nop >= newMainO && nop < newMainO + this.mainCode.length())
            return nop - newMainO - this.offsetToOriginalMainCode;

        if (nop >= newImportO && nop < newImportO + this.userImportString.length()) {

            return nop - newImportO - this.offsetToOriginalUserImports;

        }

        throw new IllegalArgumentException("position is before any original import location");

    }


    private GenerateResult generateJavaFileSource() {

        StringBuilder s = new StringBuilder();


        s.append("package io.jpad.scratch;").append("\r\n");


        if (!this.smartImports.isEmpty()) {

            s.append("// Smart Imports").append("\r\n");

            this.smartImports.forEach(imp -> s.append("import ").append(imp).append(";").append("\r\n"));

        }


        int offsetToNewUserImports = s.length();

        if (this.userImports.isEmpty()) {

            s.append("import static io.jpad.japl.Japl.*; import static io.jpad.japl.Predef.*;");

            offsetToNewUserImports = s.length();

        } else {

            s.append("// User Imports").append("\r\n");

            offsetToNewUserImports = s.length();

            s.append(this.userImportString);

        }

        s.append("\r\n");

        s.append("import static io.jpad.scratch.Dumper.*;");

        s.append("\r\n");

        s.append("public class RunnContainer {");


        int newOffsetToMainCode = s.length();


        if (this.mainCode.contains("void main(String")) {

            s.append(this.mainCode).append("\r\n");

        } else {

            s.append("public static void main(String... args) throws Exception {").append("\r\n");

            newOffsetToMainCode = s.length();

            if (this.mainCode.contains(";")) {

                s.append(this.mainCode);

            } else {

                s.append("Dump(");

                newOffsetToMainCode = s.length();

                s.append(this.mainCode + ");");

            }

            s.append("\r\n").append("}");

        }

        s.append("}");

        return new GenerateResult(s.toString(), newOffsetToMainCode, offsetToNewUserImports);

    }

    private static class GenerateResult {
        private final String generatedCode;
        private final int offsetToMainInGeneratedCode;
        private final int offsetToNewUserImports;


        @ConstructorProperties({"generatedCode", "offsetToMainInGeneratedCode", "offsetToNewUserImports"})
        public GenerateResult(String generatedCode, int offsetToMainInGeneratedCode, int offsetToNewUserImports) {
            this.generatedCode = generatedCode;
            this.offsetToMainInGeneratedCode = offsetToMainInGeneratedCode;
            this.offsetToNewUserImports = offsetToNewUserImports;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof GenerateResult)) return false;
            GenerateResult other = (GenerateResult) o;
            if (!other.canEqual(this)) return false;
            Object this$generatedCode = getGeneratedCode(), other$generatedCode = other.getGeneratedCode();
            return Objects.equals(this$generatedCode, other$generatedCode) && (getOffsetToMainInGeneratedCode() == other.getOffsetToMainInGeneratedCode() && (getOffsetToNewUserImports() == other.getOffsetToNewUserImports()));
        }

        protected boolean canEqual(Object other) {
            return other instanceof GenerateResult;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Object $generatedCode = getGeneratedCode();
            result = result * 59 + (($generatedCode == null) ? 0 : $generatedCode.hashCode());
            result = result * 59 + getOffsetToMainInGeneratedCode();
            return result * 59 + getOffsetToNewUserImports();
        }

        public String toString() {
            return "JPadCode.GenerateResult(generatedCode=" + getGeneratedCode() + ", offsetToMainInGeneratedCode=" + getOffsetToMainInGeneratedCode() + ", offsetToNewUserImports=" + getOffsetToNewUserImports() + ")";
        }


        public String getGeneratedCode() {
            return this.generatedCode;
        }


        public int getOffsetToMainInGeneratedCode() {
            return this.offsetToMainInGeneratedCode;
        }

        public int getOffsetToNewUserImports() {

            return this.offsetToNewUserImports;

        }
    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\JPadCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */