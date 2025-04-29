import java.util.Scanner;
import java.util.Arrays;

import static java.lang.Math.abs;

public class ReverseSumAbs {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] arr = new int[1][1];
        int len = 0;
        while (sc.hasNextLine()) {
            if (len >= arr.length) {
                arr = Arrays.copyOf(arr, arr.length * 2);
            }
            arr[len] = Line(new Scanner(sc.nextLine()));
            len++;
        }
        if (len!=arr.length) {
            arr = Arrays.copyOf(arr, len);
        }
        int[] line = new int[arr.length];
        int mxln = -10000000;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length > mxln) {
                mxln = arr[i].length;
            }
            for (int j = 0; j < arr[i].length; j++) {
                line[i] += abs(arr[i][j]);
            }
        }
        int[] column = new int[mxln];
        for (int i = 0; i < mxln; i++) {
            for (int[] qw : arr) {
                if (qw.length > i) {
                    column[i] += abs(qw[i]);
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length != 0) {
                for (int j = 0; j < arr[i].length; j++) {
                    System.out.print(line[i] + column[j] - abs(arr[i][j]) + " ");
                }
            }
            System.out.println();
        }
    }

    private static int[] Line(Scanner sc) {
        int[] arr = new int[1];
        int len = 0;
        while (sc.hasNextInt()) {
            if (len >= arr.length) {
                arr = Arrays.copyOf(arr, arr.length * 2);
            }
            arr[len] = sc.nextInt();
            len++;
        }
        if (len!=arr.length) {
            arr = Arrays.copyOf(arr, len);
        }
        return arr;
    }
}
