import java.util.Arrays;

public class Cache {
    private final MemoryCell[][][] cacheData;
    private final int[][] tags;
    private final int[][] states;
    private final Object[] replacementPolicies;
    private final RAM ram;
    private final int tagLen;
    private final int indexLen;
    private final int offsetLen;
    private final int cacheLineSize;
    private static final int INVALID = 0;
    private static final int VALID = 1;
    private static final int MODIFIED = 2;
    private int hits = 0;
    private int instHits = 0;
    private int dataHits = 0;
    private int tryes = 0;
    private int dataTry = 0;
    private int instTry = 0;

    public Cache(int setsCount, int setsSize, int cacheLineSize, boolean isLRU, RAM ram, int tagLen, int indexLen, int offsetLen) {
        if (setsCount <= 0 || tagLen <= 0 || indexLen <= 0 || offsetLen <= 0) {
            throw new IllegalArgumentException("Invalid cache parameters");
        }
        this.tagLen = tagLen;
        this.indexLen = indexLen;
        this.offsetLen = offsetLen;
        this.cacheLineSize = cacheLineSize;
        this.ram = ram;
        cacheData = new MemoryCell[setsCount][setsSize][cacheLineSize];
        tags = new int[setsCount][setsSize];
        states = new int[setsCount][setsSize];
        replacementPolicies = new Object[setsCount];
        for (int i = 0; i < setsCount; i++) {
            Arrays.fill(states[i], INVALID);
            replacementPolicies[i] = isLRU ? new LRU(setsSize) : new PLRU(setsSize);
        }
    }

    public int load(int addr) {
        CellByte d = (CellByte) get(addr, 1)[0];
        return d.value();
    }

    public int[] doAddr(int addr) {
        int[] result = new int[3];
        result[0] = (addr >> (indexLen + offsetLen)) & ((1 << tagLen) - 1);
        result[1] = (addr >> offsetLen) & ((1 << indexLen) - 1);
        result[2] = (addr & ((1 << offsetLen) - 1));
        return result;
    }

    public MemoryCell[] get(int addr, int count) {
        int[] parsedAddr = doAddr(addr);
        int tag = parsedAddr[0];
        int index = parsedAddr[1];
        int offset = parsedAddr[2];
        tryes++;
        for (int i = 0; i < cacheData[index].length; i++) {
            if (states[index][i] == INVALID || tags[index][i] != tag) {
                continue;
            }
            if (replacementPolicies[index] instanceof LRU) {
                ((LRU) replacementPolicies[index]).use(i);
            } else if (replacementPolicies[index] instanceof PLRU) {
                ((PLRU) replacementPolicies[index]).use(i);
            }
            hits++;
            MemoryCell[] result = Arrays.copyOfRange(cacheData[index][i], offset, offset + count);
            if (result[0].getMemmoryCellTypo() == CellTypo.BYTE) {
                dataHits++;
                dataTry++;
            } else {
                instHits++;
                instTry++;
            }
            return result;
        }
        int replaceIndex;
        if (replacementPolicies[index] instanceof LRU) {
            replaceIndex = ((LRU) replacementPolicies[index]).getNextForReplace();
        } else {
            replaceIndex = ((PLRU) replacementPolicies[index]).getNextForReplace();
        }
        if (states[index][replaceIndex] == MODIFIED) {
            ram.set(tags[index][replaceIndex], index, 0, cacheData[index][replaceIndex]);
        }
        cacheData[index][replaceIndex] = ram.get(tag, index, 0, cacheLineSize);
        tags[index][replaceIndex] = tag;
        states[index][replaceIndex] = VALID;
        MemoryCell[] result = Arrays.copyOfRange(cacheData[index][replaceIndex], offset, offset + count);
        if (result[0].getMemmoryCellTypo() == CellTypo.BYTE) {
            dataTry++;
        } else {
            instTry++;
        }
        return result;
    }

    public void set(int addr, MemoryCell[] cell) {
        int[] parsedAddr = doAddr(addr);
        int tag = parsedAddr[0];
        int index = parsedAddr[1];
        int offset = parsedAddr[2];
        tryes++;
        dataTry++;
        for (int i = 0; i < cacheData[index].length; i++) {
            if (states[index][i] == INVALID || tags[index][i] != tag) {
                continue;
            }
            if (replacementPolicies[index] instanceof LRU) {
                ((LRU) replacementPolicies[index]).use(i);
            } else if (replacementPolicies[index] instanceof PLRU) {
                ((PLRU) replacementPolicies[index]).use(i);
            }
            hits++;
            dataHits++;
            System.arraycopy(cell, 0, cacheData[index][i], offset, cell.length);
            states[index][i] = MODIFIED;
            return;
        }
        int replaceIndex;
        if (replacementPolicies[index] instanceof LRU) {
            replaceIndex = ((LRU) replacementPolicies[index]).getNextForReplace();
        } else {
            replaceIndex = ((PLRU) replacementPolicies[index]).getNextForReplace();
        }
        if (states[index][replaceIndex] == MODIFIED) {
            ram.set(tags[index][replaceIndex], index, 0, cacheData[index][replaceIndex]);
        }
        cacheData[index][replaceIndex] = ram.get(tag, index, 0, cacheLineSize);
        tags[index][replaceIndex] = tag;
        states[index][replaceIndex] = VALID;
        System.arraycopy(cell, 0, cacheData[index][replaceIndex], offset, cell.length);
        states[index][replaceIndex] = MODIFIED;
    }

    public int getTry() {
        return tryes;
    }

    public int getAllHits() {
        return hits;
    }

    public int getDataTry() {
        return dataTry;
    }

    public int getDataHitRate() {
        return dataHits;
    }

    public int getInstTry() {
        return instTry;
    }

    public int getInstHitRate() {
        return instHits;
    }

    public void storeByte(int addr, int value) {
        set(addr, new MemoryCell[]{new CellByte(value)});
    }
}