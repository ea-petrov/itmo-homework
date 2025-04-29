package expression;

public class Main {
    public static void main(String[] args) {
        System.out.println((new Multiply(new Variable("x"), new Add(new Const(1), new Const(3)))).evaluate(4));
        System.out.println("prekolchikk");
    }
}