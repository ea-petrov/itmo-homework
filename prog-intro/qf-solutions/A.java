import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double a = sc.nextInt();
        double b = sc.nextInt();
        double n = sc.nextInt();
        int answer = (int) (2 * (Math.ceil((n - b) / (b - a))) + 1);
        System.out.println(answer);
    }
}
