package expression;


public class GeomTriangle extends AbstractBinaryOperation{
    public GeomTriangle(AllExpressions first, AllExpressions second){
        super(first, second, "area", 1, 0);
    }


    @Override
    protected int doOperation(int x, int y) {
        return x * y / 2;
    }

    @Override
    protected long doOperation(long x, long y) {
        return 0;
    }
}
