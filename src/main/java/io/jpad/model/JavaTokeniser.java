package io.jpad.model;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


public class JavaTokeniser {
    private static final Logger LOG = Logger.getLogger(JavaTokeniser.class.getName());
    private static final char[] NAME_START = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_".toCharArray();
    private static final char[] NAMES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".toCharArray();
    private static final char[] NUMBERS = "0123456789.".toCharArray();
    private static final char[] OPS = "*/%<>!^|&~=?:".toCharArray();
    private static final char[] SPACERS = " \r\n".toCharArray();
    private static final String[] KW = new String[]{"abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"};
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(KW));
    private final Set<String> classNames = new HashSet<>();
    private int p = 0;
    private char[] s;


    public static Set<String> getCodeWords(String code) {

        JavaTokeniser jt = new JavaTokeniser();

        jt.parse(code);

        return jt.classNames;

    }


    private void parse(String code) {

        this.s = code.toCharArray();

        while (this.p < this.s.length) {


            boolean found = (parse(SPACERS) || parse(NUMBERS) || parse(OPS) || parseQuotes() || parseComments() || parseNames());


            if (!found) {

                LOG.fine("nothing found:" + this.s[this.p]);

                this.p++;

            }

        }

    }


    private boolean parseNames() {

        if (contains(NAME_START, this.s[this.p])) {

            int st = this.p++;

            while (this.p < this.s.length && contains(NAMES, this.s[this.p++])) ;

            String name = (new String(this.s, st, this.p - st - 1)).intern();


            if (name.length() > 0 && Character.isUpperCase(name.charAt(0))) {

                LOG.fine("Found name = " + name);

                this.classNames.add(name);

            }

            return true;

        }

        return false;

    }


    private boolean isPosEqual(String txt) {

        if (this.p < this.s.length - txt.length()) {

            int i = 0;
            if (i < txt.length()) {


                return this.s[this.p + i] == txt.charAt(i);


            }

        }

        return false;

    }


    private boolean parseComments() {

        if (isPosEqual("\\\\")) {

            this.p += 2;

            while (this.p < this.s.length && this.s[this.p] != '\n') {

                this.p++;

            }

            return true;

        }

        if (isPosEqual("\\*")) {

            this.p += 2;

            while (this.p < this.s.length && !isPosEqual("*\\")) {

                this.p++;

            }

            if (this.p < this.s.length - 1) {

                this.p += 2;

            }

            return true;

        }

        return false;

    }


    private boolean parseQuotes() {

        if (this.s[this.p] == '"') {

            int st = this.p++;

            while ((this.p < this.s.length && this.s[this.p] != '"') || this.s[this.p - 1] == '\\') {

                this.p++;

            }

            this.p++;

            String quote = (new String(this.s, st, this.p - st)).intern();

            LOG.fine("quote = " + quote);


            return true;

        }

        return false;

    }


    private boolean parse(char[] chars) {

        if (contains(chars, this.s[this.p])) {

            while (this.p < this.s.length && contains(chars, this.s[this.p])) {

                this.p++;

            }

            return true;

        }

        return false;

    }


    private boolean contains(char[] set, char c) {

        for (int i = 0; i < set.length; i++) {

            if (set[i] == c) {

                return true;

            }

        }

        return false;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\JavaTokeniser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */