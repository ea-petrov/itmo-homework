package expression;

public class Divide extends AbstractBinaryOperation {
    public Divide(final AllExpressions a, final AllExpressions b) {
        super(a, b, "/", 2, 0);
    }

    @Override
    protected int doOperation(int x, int y) {
        return x / y;
    }

    @Override
    protected long doOperation(long x, long y) {
        return x / y;
    }

}