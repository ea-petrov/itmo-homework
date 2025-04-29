import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<ArrayList<Integer>> n = new ArrayList<>();
        while (sc.hasNextLine()) {
            String[] str = sc.nextLine().split(" ");
            ArrayList<Integer> ans = new ArrayList<>();
            for (String s : str) {
                if (!s.isEmpty()) {
                    ans.add(Integer.parseInt(s));
                }
            }
            n.add(ans);
        }
        for (int i = n.size() - 1; i >= 0; --i) {
            for (int j = n.get(i).size() - 1; j >= 0; j--) {
                System.out.print(n.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}