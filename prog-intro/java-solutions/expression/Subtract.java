package expression;

public class Subtract extends AbstractBinaryOperation {
    public Subtract(final AllExpressions a, final AllExpressions b) {
        super(a, b, "-", 1, 0);
    }

    @Override
    protected int doOperation(int x, int y) {
        return x - y;
    }

    @Override
    protected long doOperation(long x, long y) {
        return x - y;
    }
}