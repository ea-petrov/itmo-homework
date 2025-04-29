package expression;

import java.util.Map;

public abstract class AbstractUnaryOperation implements AllExpressions {
    private final String operation;
    private final int priority;
    private final AllExpressions value;

    protected AbstractUnaryOperation(final String operation, final int priority, final AllExpressions value) {
        this.operation = operation;
        this.priority = priority;
        this.value = value;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        toMiniStringBuilder(sb);
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringStringBuilder(sb);
        return sb.toString();
    }

    @Override
    public void toStringStringBuilder(StringBuilder sb) {
        if (operation.equals("²") && !this.isItTetra()) {
            sb.append("(");
            value.toStringStringBuilder(sb);
            sb.append(")²");
            return;
        }
        sb.append(operation).append("(");
        value.toStringStringBuilder(sb);
        sb.append(")");
    }

    protected abstract int doOperation(int x);

    protected abstract long doOperation(long x);

    protected abstract boolean isItTetra();

    @Override
    public int evaluate(int x) {
        return doOperation(value.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return doOperation(value.evaluate(x, y, z));
    }

    @Override
    public long evaluateL(Map<String, Long> values) {
        return doOperation(value.evaluateL(values));
    }

    @Override
    public void toMiniStringBuilder(StringBuilder sb) {
        if (operation.equals("²") && !this.isItTetra()) {
            if (value instanceof AbstractBinaryOperation op && op.priority() < priority) {
                sb.append("(");
                value.toMiniStringBuilder(sb);
                sb.append(")").append(operation);
                return;

            }
            if (value instanceof AbstractUnaryOperation ow && ow.getPriority() < priority) {
                sb.append("(");
                value.toMiniStringBuilder(sb);
                sb.append(")").append(operation);
                return;

            }
            value.toMiniStringBuilder(sb);
            sb.append(operation);
            return;
        }
        if (value instanceof AbstractBinaryOperation) {
            sb.append(operation).append("(");
            value.toMiniStringBuilder(sb);
            sb.append(")");
            return;
        } else if (this.isItTetra()) {
            sb.append(operation);
            value.toMiniStringBuilder(sb);
            return;

        }
        sb.append(operation).append(" ");
        value.toMiniStringBuilder(sb);
    }
}
