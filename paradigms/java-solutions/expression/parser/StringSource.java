package expression.parser;

public class StringSource implements CharSource {
    private final String data;
    private int pos;
    private int ind;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    public void back() {
        pos -= 2;
    }

    @Override
    public String error(final String message) {
        return pos - 1 + " " + message;
    }

}
