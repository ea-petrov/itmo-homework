package expression;

public class Negative extends AbstractUnaryOperation {
    public Negative(AllExpressions a) {
        super("-", 3, a);
    }

    @Override
    protected int doOperation(int x) {
        return -x;
    }

    @Override
    protected long doOperation(long x) {
        return -x;
    }

    @Override
    protected boolean isItTetra() {
        return false;
    }

}
