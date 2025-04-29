package expression.exceptions;

import expression.AllExpressions;
import expression.GeomRectangle;

public class CheckedRectangle extends GeomRectangle {
    public CheckedRectangle(final AllExpressions a, final AllExpressions b) {
        super(a, b);
    }
    @Override
    protected int doOperation(int x, int y) {
        if (x < 0 || y < 0){
            throw new BadValueError("it is impossible for rectangle");
        }
        if (x > Integer.MAX_VALUE / 2 || y > Integer.MAX_VALUE / 2){
            throw new OverflowException("overflow in rectangle");
        }
        return (CheckedAdd.checkedDoOperation(2 * x, 2 * y));
    }
}
