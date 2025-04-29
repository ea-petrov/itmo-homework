package expression;

import java.util.Map;

public class Const implements AllExpressions {
    private final Number x;

    public Const(final int x) {
        this.x = x;
    }

    public Const(final long x) {
        this.x = x;
    }

    @Override
    public int evaluate(int x) {
        return this.x.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.x.intValue();
    }

    @Override
    public long evaluateL(Map<String, Long> values) {
        return this.x.longValue();
    }

    @Override
    public String toString() {
        return x.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Const that = (Const) obj;
        return x.equals(that.x);
    }

    @Override
    public int hashCode() {
        return x.hashCode();
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