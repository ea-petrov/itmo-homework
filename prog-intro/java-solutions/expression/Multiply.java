package expression;

public class Multiply extends AbstractBinaryOperation {
    public Multiply(final AllExpressions a, final AllExpressions b) {
        super(a, b, "*", 2, 0);
    }

    @Override
    protected int doOperation(int x, int y) {
        return x * y;
    }

    @Override
    protected long doOperation(long x, long y) {
        return x * y;
    }
}
