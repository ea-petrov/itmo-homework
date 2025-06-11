public class PLRU {
    private final int count;
    private final int[] bits;

    public PLRU(int count) {
        this.count = count;
        this.bits = new int[count];
        for (int i = 0; i < count; i++) {
            bits[i] = 0;
        }
    }

    public void use(int line) {
        if (line < 0 || line >= count) {
            throw new IllegalArgumentException("Invalid line index: " + line);
        }
        bits[line] = 1;
        boolean flag = true;
        for (int bit : bits) {
            if (bit == 0) {
                flag = false;
                break;
            }
        }
        if (flag) {
            for (int i = 0; i < count; i++) {
                if (i != line) {
                    bits[i] = 0;
                }
            }
        }
    }

    public int getNextForReplace() {
        for (int i = 0; i < count; i++) {
            if (bits[i] == 0) {
                use(i);
                return i;
            }
        }
        use(0);
        return 0;
    }
}