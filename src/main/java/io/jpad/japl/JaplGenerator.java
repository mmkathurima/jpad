package io.jpad.japl;

public class JaplGenerator {
    private static final String XY_ONE_TYPE_TEMPLATE = "\r\n/** FFF(x,y) two arrays **/\r\npublic static double FFF(double x, double y) {  return x OPP y; };\r\npublic static double[] FFF(double[] x, double y) { \r\n\tdouble[] r = new double[x.length];\r\n\tfor(int i=0; i<x.length; i++) { r[i]=x[i] OPP y; } return r; }\r\npublic static double[] FFF(double x, double[] y) { \r\n\tdouble[] r = new double[y.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]=x OPP y[i]; } return r; }\r\npublic static double[] FFF(double[] x, double[] y) {\r\n\tdouble[] r = new double[x.length];\r\n\tcl(x,y); for(int i=0; i<x.length; i++) { r[i]=x[i] OPP y[i]; } return r; }";
    private static final String XY_DIFF_TYPE_TEMPLATE = "\r\npublic static double[] FFF(double x, int[] y) { return FFF(x,cD(y));} \r\npublic static double[] FFF(int[] x, double y) { return FFF(cD(x),y);} ";
    private static final String X_TEMPLATE = "/** perform OPP on every elements of array and return new array with result. **/\r\npublic static double[] FFF(double[] x) {\r\n\tdouble[] r = new double[x.length];\r\n\tfor(int i=0; i<x.length; i++) { r[i] = OPP(x[i]); } return r; }\r\npublic static double[] FFF(int[] x) { return FFF(cD(x)); }\r\npublic static double FFF(double x) { return OPP(x); }\r\npublic static double FFF(int x) { return OPP(x); }";
    private static final String BOOLEAN_LOGIC_TEMPLATE = "/**  @return boolean logic OPP  */\r\npublic static boolean FFF(boolean x, boolean y) { return x OPP y; }\r\npublic static boolean[] FFF(boolean[] x, boolean[] y) {\r\n\tcl(x,y); boolean[] r = new boolean[x.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]|=y[i] OPP x[i];}; return r; }\r\npublic static boolean[] FFF(int[] x, int[] y) {\r\n\tcl(x,y); boolean[] r = new boolean[x.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]=y[i]>0 OPP x[i]>0;}; return r; }\r\npublic static boolean[] FFF(double[] x, double[] y) {\r\n\tcl(x,y); boolean[] r = new boolean[x.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]=y[i]>0 OPP x[i]>0;}; return r; }\r\npublic static boolean[] FFF(double[] x, int[] y) { return FFF(cB(x),cB(y)); }\r\npublic static boolean[] FFF(int[] x, double[] y) { return FFF(cB(x),cB(y)); }\r\npublic static boolean[] FFF(boolean[] x, int[] y) { return FFF(x,cB(y)); }\r\npublic static boolean[] FFF(int[] x, boolean[] y) { return FFF(cB(x),y); }\r\npublic static boolean[] FFF(boolean[] x, double[] y) { return FFF(x,cB(y)); }\r\npublic static boolean[] FFF(double[] x, boolean[] y) { return FFF(cB(x),y); }";

    private static String genXY(String name, String op) {

        StringBuilder sb = new StringBuilder();

        for (JType t : JType.values()) {

            if (!t.equals(JType.Boolean)) {

                String s = "\r\n/** FFF(x,y) two arrays **/\r\npublic static double FFF(double x, double y) {  return x OPP y; };\r\npublic static double[] FFF(double[] x, double y) { \r\n\tdouble[] r = new double[x.length];\r\n\tfor(int i=0; i<x.length; i++) { r[i]=x[i] OPP y; } return r; }\r\npublic static double[] FFF(double x, double[] y) { \r\n\tdouble[] r = new double[y.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]=x OPP y[i]; } return r; }\r\npublic static double[] FFF(double[] x, double[] y) {\r\n\tdouble[] r = new double[x.length];\r\n\tcl(x,y); for(int i=0; i<x.length; i++) { r[i]=x[i] OPP y[i]; } return r; }".replace("FFF", name);

                s = s.replace("static double", "static " + t.resultType.name);

                s = s.replace("double[] r", t.resultType.name + "[] r");

                s = s.replace("new double", "new " + t.resultType.name);

                s = s.replace("double", t.name);

                s = s.replace("x[i] OPP y[i]", t.castResult("x[i] " + op + " y[i]"));

                s = s.replace("x[i] OPP y", t.castResult("x[i] " + op + " y"));

                s = s.replace("x OPP y[i]", t.castResult("x " + op + " y[i]"));

                s = s.replace("x OPP y", t.castResult("x " + op + " y"));

                sb.append(s);
            }
        }
        sb.append("\r\npublic static double[] FFF(double x, int[] y) { return FFF(x,cD(y));} \r\npublic static double[] FFF(int[] x, double y) { return FFF(cD(x),y);} ".replace("FFF", name).replace("OPP", op));

        return sb.toString();
    }

    private static String genX(String name, String op) {

        return "/** perform OPP on every elements of array and return new array with result. **/\r\npublic static double[] FFF(double[] x) {\r\n\tdouble[] r = new double[x.length];\r\n\tfor(int i=0; i<x.length; i++) { r[i] = OPP(x[i]); } return r; }\r\npublic static double[] FFF(int[] x) { return FFF(cD(x)); }\r\npublic static double FFF(double x) { return OPP(x); }\r\npublic static double FFF(int x) { return OPP(x); }".replace("FFF", name).replace("OPP", op);
    }

    private static String genBooleanLogic(String name, String op) {

        return "/**  @return boolean logic OPP  */\r\npublic static boolean FFF(boolean x, boolean y) { return x OPP y; }\r\npublic static boolean[] FFF(boolean[] x, boolean[] y) {\r\n\tcl(x,y); boolean[] r = new boolean[x.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]|=y[i] OPP x[i];}; return r; }\r\npublic static boolean[] FFF(int[] x, int[] y) {\r\n\tcl(x,y); boolean[] r = new boolean[x.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]=y[i]>0 OPP x[i]>0;}; return r; }\r\npublic static boolean[] FFF(double[] x, double[] y) {\r\n\tcl(x,y); boolean[] r = new boolean[x.length];\r\n\tfor(int i=0; i<y.length; i++) { r[i]=y[i]>0 OPP x[i]>0;}; return r; }\r\npublic static boolean[] FFF(double[] x, int[] y) { return FFF(cB(x),cB(y)); }\r\npublic static boolean[] FFF(int[] x, double[] y) { return FFF(cB(x),cB(y)); }\r\npublic static boolean[] FFF(boolean[] x, int[] y) { return FFF(x,cB(y)); }\r\npublic static boolean[] FFF(int[] x, boolean[] y) { return FFF(cB(x),y); }\r\npublic static boolean[] FFF(boolean[] x, double[] y) { return FFF(x,cB(y)); }\r\npublic static boolean[] FFF(double[] x, boolean[] y) { return FFF(cB(x),y); }".replace("FFF", name).replace("OPP", op);
    }

    public static void main(String... args) {

        SpacingStringBuilder sb = new SpacingStringBuilder();

        sb.increaseIndent();

        sb.append("/** This code was generated by JaplGenerator **/");

        sb.append(genXY("mul", "*"));

        sb.append(genXY("add", "+"));

        sb.append(genXY("sub", "-"));

        sb.append(genXY("div", "/"));

        sb.append(genX("sin", "Math.sin"));

        sb.append(genX("cos", "Math.cos"));

        sb.append(genX("tan", "Math.tan"));

        sb.append(genX("asin", "Math.asin"));

        sb.append(genX("acos", "Math.acos"));

        sb.append(genX("atan", "Math.atan"));

        sb.append(genX("sqrt", "Math.sqrt"));

        sb.append(genX("abs", "Math.abs"));

        sb.append(genBooleanLogic("and", "&&"));

        sb.append(genBooleanLogic("or", "||"));

        sb.append(genBooleanLogic("xor", "^"));

        System.out.println(sb);
    }

    private enum JType {
        Int(Integer.class, "int", "i", null),
        Byte(Byte.class, "byte", "x", Int),
        Short(Short.class, "short", "h", Int),
        Long(Long.class, "long", "l", null),
        Float(Float.class, "float", "f", null),
        Double(Double.class, "double", "d", null),
        Boolean(Boolean.class, "boolean", "b", null);

        private final Class<?> cls;

        private final String name;

        private final String letter;

        private final JType resultType;

        JType(Class<?> cls, String name, String letter, JType resultType) {

            this.cls = cls;

            this.name = name;

            this.letter = letter;

            this.resultType = (resultType == null) ? this : resultType;
        }

        String castResult(String varName) {

            if (this.equals(this.resultType)) {

                return varName;
            }

            return "((" + this.resultType.name + ")" + varName + ")";
        }
    }

    private static class SpacingStringBuilder {
        private final StringBuilder sb = new StringBuilder(1000);
        private String indent = "";

        private SpacingStringBuilder() {
        }

        private void increaseIndent() {

            this.indent += "\t";
        }

        private void decreaseIndent() {

            if (this.indent.length() > 0) {

                this.indent = this.indent.substring(1);
            }
        }

        void append(Object o) {

            String s = o.toString().replace("\n", "\n" + this.indent);

            this.sb.append(this.indent).append(s).append("\r\n\r\n");
        }

        public String toString() {

            return this.sb.toString();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\japl\JaplGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */