package expression;


public class Square extends AbstractUnaryOperation {
    public Square(AllExpressions a) {
        super("²", 5, a);
    }

    @Override
    protected int doOperation(int x) {
        return x * x;
    }

    @Override
    protected long doOperation(long x) {
        return x * x;
    }

    @Override
    protected boolean isItTetra() {
        return false;
    }

}
