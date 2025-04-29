import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;

public class I {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int xl = Integer.MAX_VALUE;
        int xr = Integer.MIN_VALUE;
        int yl = Integer.MAX_VALUE;
        int yr = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int h = sc.nextInt();
            xl = min(xl, x - h);
            xr = max(xr, x + h);
            yl = min(yl, y - h);
            yr = max(yr, y + h);
        }
        double resH = (double) max(abs(xr - xl), abs(yr - yl)) / 2;
        int answerH = (int) Math.ceil(resH);
        int answerX = (xl + xr) / 2;
        int answerY = (yl + yr) / 2;
        System.out.println(answerX + " " + answerY + " " + answerH);
    }
}
