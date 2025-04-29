import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int start = 25000 * -710;
        for (int i = 0; i < n; i++) {
            System.out.println(start);
            start -= 710;
        }
    }
}