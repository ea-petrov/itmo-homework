package expression.parser;

import expression.exceptions.*;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void back() {
        source.back();
        ch = source.next();
    }

    protected void expect() {
        if (!take(')')) {
            throw new BadBracketsError("have not closing brackets");
        }
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }


    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            ch = source.hasNext() ? source.next() : END;
        }
    }

    public boolean eof() {
        return ch == END;
    }
}