package expression;

import expression.exceptions.OverflowException;
import expression.exceptions.UnsupportedCharacterError;

import java.util.Map;

public class Variable implements AllExpressions {
    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name.charAt(name.length() - 1)) {
            case 'x' -> x;
            case 'y' -> y;
            case 'z' -> z;
            default -> throw new OverflowException("this variable have not name");
        };
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public long evaluateL(Map<String, Long> values) {
        return values.get(name);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Variable that = (Variable) obj;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public void toMiniStringBuilder(StringBuilder sb) {
        sb.append(toString());
    }
    @Override
    public void toStringStringBuilder(StringBuilder sb){
        sb.append(toString());
    }
}