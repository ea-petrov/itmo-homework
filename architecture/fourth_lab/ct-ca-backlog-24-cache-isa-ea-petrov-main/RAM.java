public class RAM {
    private final MemoryCell[] RAM;
    private final int indlen;
    private final int offsetlen;

    public RAM(int size, int cacheIndexLen, int offsetLen) {
        this.indlen = cacheIndexLen;
        this.offsetlen = offsetLen;
        if (size < 0) {
            throw new NegativeArraySizeException();
        }
        RAM = new MemoryCell[size];
        for (int i = 0; i < size; i++){
            RAM[i] = new CellByte(0);
        }
    }

    public boolean isValid(int start, int offset) {
        return start >= 0 && start + offset <= RAM.length;
    }

    public MemoryCell[] get(int t, int index, int offset, int count) {
        int start = (t << (indlen + offsetlen)) + (index << (offsetlen)) + offset;
        if (!isValid(start, count - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        MemoryCell[] result = new MemoryCell[count];
        int srcPos = (t << (indlen + offsetlen)) + (index << (offsetlen)) + offset;
        System.arraycopy(RAM, srcPos, result, 0, count);
        return result;
    }
    public void set(int tag, int ind, int offset, MemoryCell[] values) {
        int start = (tag << (indlen + offsetlen)) + (ind << (offsetlen)) + offset;
        if (!isValid(start, values.length - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(values, 0, RAM, start, values.length);
    }
    public void set(int addr, MemoryCell[] values) {
        if (!isValid(addr, values.length - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(values, 0, RAM, addr, values.length);
    }
}
