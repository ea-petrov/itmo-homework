package search;

public class BinarySearch3233 {
    /*
    Define: (a <-> array, x <-> search value, args <-> program arguments)
    P*: forall i1..a.length - 1 if i < j: a[i] >= a[j]), x = args[0], a = args[1:] (args without first value(format_typo = string),
    i0 - first i : a[i] <= x
    Q*: find and return i0 if a[i0] != x else insert in the format Arrays.binarySearch
     */
    private static int iterativeBinarySearch(final int[] a, final int x){
        // Pre: left = -1, right = a.length
        // Inv: a[left] > x &&  a[right] <= x && left >= left1  && right1 <= right && -1 <= left1 < right1 <= a.length
        int left = -1;
        int right = a.length;
        while (right > left + 1){
            //Pre1: Inv
            int middle = (left + right) / 2;
            //Q1: middle1 = (left1 + right1) / 2 && left1 + 1 < right1 && left1 < m < right1
            if (a[middle] > x){
                //Pre2 : Pre1 && a[middle1] > x
                left = middle;
                //Q2 : middle2 = left && a[left] > x
            } else {
                //Pre3 : Pre1 && Inv && a[middle1] <= x
                right = middle;
                //Q3 :  middle2 = right && a[right] <= x
            }
        }
        if (right < a.length && a[right] == x){
            // left + 1 = right; -1 <= left < i0 <= right <= a.length ==> right <= i0 <= right ==>
            // right = i0
            //Pre4: a[i0] != null && a[i0] == x
            return right;
        }
        //do insert in the format Arrays.binarySearch
        return -right - 1;
        /*<<<COMMENT>>>: Why cycle is end?
        in each iteration, we change the observed boundaries of the array by half
        until we reach the situation that left + 1 = right, at which point our cycle ends.
         */
    }

    /*
    Define: (a <-> array, x <-> search value, args <-> program arguments)
    P': forall i1..a.length - 1 if i < j: a[i] >= a[j]), x = args[0], a = args[1:] (args without first value(format_typo = string),
    i0 - first i : a[i] <= x
    Q': find and return i0 if a[i0] != x else insert in the format Arrays.binarySearch
     */
    private static int recursiveBinarySearch(final int[] a, final int x, int left, int right){
        // Pre: left = -1, right = a.length
        // Inv: a[left] > x &&  a[right] <= x && left >= left1  && right1 <= right && -1 <= left1 < right1 <= a.length
        if (left + 1 == right){
            //Pre4: a[i0] != null && a[i0] == x
            if (right < a.length && a[right] == x) {
                // left + 1 = right(end of recursion); -1 <= left < i0 <= right <= a.length ==> right <= i0 <= right ==>
                // right = i0
                return right;
            }
            //do insert in the format Arrays.binarySearch
            return -right - 1;
        }
        //Pre1: Inv
        int middle = (left + right) / 2;
        //Q1: middle1 = (left1 + right1) / 2 && left1 + 1 < right1 && left1 < m < right1
        if (a[middle] > x) {
            //Pre2 : a[middle1] > x && Pre1
            return recursiveBinarySearch(a, x, middle, right);
            //Q2 : middle2 = left && a[left] > x || Q'
        } else {
            // Pre3 : a[middle1] <= x && Pre1
            return recursiveBinarySearch(a, x, left, middle);
            //Q3 : middle2 = right && a[right] <= x || Q'
        }

    }
    /*
    <<<COMMENT>>> Why recursion is end?
    in each new step of the recursion, we narrow the boundaries by half
    until left + 1 becomes equal to right. In this case, we have written a way out of the recursion.
     */

    // P : args.length  = 1 + a.length, func = psvm(can compil)
    // Q: iterative/recursive binary search result
    public static void main(String[] args) {
        // Pre: P && args - is string and have Whitespace between them; x =(int)args[0], a = array(integer typo)args[1:]
        final int x = Integer.parseInt(args[0]);
        final int[] a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++){
            a[i] = Integer.parseInt(args[i + 1]);
        }
        //Q1: x = Integer.parseInt(args[0]), a = array<int> = args[1:]
        //Pre1: P' , binarySearch.type = recursive
        //System.out.println(recursiveBinarySearch(a, x, -1, a.length));
        //Q1: Q'

        // Pre2: P* , binarySearch.type = iterative
        System.out.println(iterativeBinarySearch(a, x));
        // Q2: Q*
    }
}