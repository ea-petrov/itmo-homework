package expression.exceptions;

import expression.AllExpressions;
import expression.GeomTriangle;

public class CheckedTriangle extends GeomTriangle {
    public CheckedTriangle(AllExpressions a, AllExpressions b) {
        super(a, b);
    }

    @Override
    protected int doOperation(int x, int y) {
        if (x < 0 || y < 0) {
            throw new BadValueError("it is impossible for triangle");
        }
        if (x % 2 == 0) {
            return CheckedMultiply.checkedDoOperation(x / 2, y);
        }
        if (y % 2 == 0) {
            return CheckedMultiply.checkedDoOperation(x, y / 2);
        }
        return CheckedMultiply.checkedDoOperation(x, y) / 2;
    }
}
