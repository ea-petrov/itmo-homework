import java.util.Scanner;

public class J {
    public static void main(final String[] args) {
        final Scanner sc = new Scanner(System.in); // :NOTE: no ,close()
        final int n = sc.nextInt();
        final int[][] arr = new int[n][n];

        sc.nextLine();
        for (int i = 0; i < n; i++) {
            final String str = sc.nextLine();
            for (int j = 0; j < n; j++) {
                arr[i][j] = str.charAt(j) - '0';
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i][j] == 0) {
                    continue;
                }
                arr[i][j] = 1;
                for (int k = j + 1; k < n; k++) {
                    arr[i][k] = arr[i][k] < 0
                            ? arr[i][k] - arr[j][k] + 10
                            : arr[i][k] - arr[j][k];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }
}
