package expression;


public class GeomRectangle extends AbstractBinaryOperation {
    public GeomRectangle(AllExpressions first, AllExpressions second) {
        super(first, second, "perimeter", 1, 0);
    }

    @Override
    protected int doOperation(int x, int y){
        return 2 * (x + y);

    }

    @Override
    protected long doOperation(long x, long y) {
        return 0;
    }
}
