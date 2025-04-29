package expression;

import java.util.Map;

public abstract class AbstractBinaryOperation implements AllExpressions {
    private final AllExpressions first;
    private final AllExpressions second;
    private final String operation;
    private final int getPriority;
    private final int operationTypo;

    public AbstractBinaryOperation(final AllExpressions first, final AllExpressions second, final String operation, final int getPriority, final int operationTypo) {
        this.first = first;
        this.second = second;
        this.operation = operation;
        this.getPriority = getPriority;
        this.operationTypo = operationTypo;
    }

    protected int priority() {
        return getPriority;
    }

    protected abstract int doOperation(int x, int y);

    protected abstract long doOperation(long x, long y);

    @Override
    public int evaluate(int x) {
        return doOperation(first.evaluate(x), second.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return doOperation(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public long evaluateL(Map<String, Long> values) {
        return doOperation(first.evaluateL(values), second.evaluateL(values));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractBinaryOperation that = (AbstractBinaryOperation) obj;
        return first.equals(that.first) && second.equals(that.second);
    }

    @Override
    public int hashCode() {
        return 12 * first.hashCode() + 123 * operation.hashCode() + 1234 * second.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringStringBuilder(sb);
        return sb.toString();
    }
    @Override
    public void toStringStringBuilder(StringBuilder sb){
        sb.append("(");
        first.toStringStringBuilder(sb);
        sb.append(" ").append(operation).append(" ");
        second.toStringStringBuilder(sb);
        sb.append(")");
    }


    public String toMiniString(){
        StringBuilder sb = new StringBuilder();
        toMiniStringBuilder(sb);
        return sb.toString();

    }
    public void toMiniStringBuilder(StringBuilder sb) {
        if ((first instanceof AbstractBinaryOperation op) && (op.getPriority < getPriority)) {
            sb.append('(');
            first.toMiniStringBuilder(sb);
            sb.append(')');

        } else {
            first.toMiniStringBuilder(sb);
        }
        sb.append(' ').append(operation).append(' ');
        if ((second instanceof AbstractBinaryOperation op) && (op.getPriority == getPriority && ((this instanceof Divide || this instanceof Subtract || second instanceof Divide || this instanceof Log || this instanceof Pow)
                || ((this instanceof GCD || this instanceof LCM) && op.operationTypo != operationTypo)) || op.getPriority < getPriority)) {
            sb.append('(');
            second.toMiniStringBuilder(sb);
            sb.append(')');

        } else {
            second.toMiniStringBuilder(sb);
        }
    }
}