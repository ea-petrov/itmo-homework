public class LRU {
    private final int[] lruArray;

    public LRU(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        lruArray = new int[count];
        for (int i = 0; i < count; i++) {
            lruArray[i] = i;
        }
    }

    public void use(int line) {
        if (0 <= line && line < lruArray.length) {
            int temp = lruArray[line];
            for (int i = line; i > 0; i--) {
                lruArray[i] = lruArray[i - 1];
            }
            lruArray[0] = temp;
        } else {
            throw new IllegalArgumentException("Invalid line index: " + line);
        }
    }

    public int getNextForReplace() {
        int last = lruArray[lruArray.length - 1];
        use(lruArray.length - 1);
        return last;
    }
}