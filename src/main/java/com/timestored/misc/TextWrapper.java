package com.timestored.misc;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.Iterator;

public final class TextWrapper {
    private final WrapStrategy strategy;
    private final CharMatcher delimiter;
    private final int width;

    TextWrapper(WrapStrategy strategy, CharMatcher delimiter, int width) {
        this.strategy = strategy;
        this.delimiter = delimiter;
        this.width = width;
    }

    public static TextWrapper forWidth(int i) {
        return new TextWrapper(Strategy.SOFT, CharMatcher.whitespace(), i);
    }

    public TextWrapper hard() {
        return new TextWrapper(Strategy.HARD, this.delimiter, this.width);
    }

    public TextWrapper respectExistingBreaks() {
        return new TextWrapper(this.strategy, CharMatcher.anyOf(" \t"), this.width);
    }

    public String wrap(String text) {
        return this.strategy.wrap(Splitter.on(this.delimiter).split(text), this.width);
    }

    enum Strategy implements WrapStrategy {
        HARD {
            public String wrap(Iterable<String> words, int width) {
                return Joiner.on('\n').join(Splitter.fixedLength(width).split(Joiner.on(' ').join(words)));
            }
        },

        SOFT {
            public String wrap(Iterable<String> words, int width) {
                StringBuilder sb = new StringBuilder();
                int lineLength = 0;
                Iterator<String> iterator = words.iterator();
                if (iterator.hasNext()) {
                    sb.append(iterator.next());
                    lineLength = sb.length();
                    while (iterator.hasNext()) {
                        String word = iterator.next();
                        if (word.length() + 1 + lineLength > width) {
                            sb.append('\n');
                            lineLength = 0;
                        } else {
                            lineLength++;
                            sb.append(' ');
                        }
                        sb.append(word);
                        lineLength += word.length();
                    }
                }
                return sb.toString();
            }
        }
    }

    interface WrapStrategy {
        String wrap(Iterable<String> param1Iterable, int param1Int);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\TextWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */