import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] arguments) {
        String assemblyFile = null;
        String binaryFile = null;
        for (int i = 0; i < arguments.length; i++) {
            switch (arguments[i]) {
                case "--asm":
                    if (i + 1 < arguments.length) {
                        assemblyFile = arguments[i + 1];
                        i++;
                    } else {
                        System.err.println("error");
                        return;
                    }
                    break;
                case "--bin":
                    if (i + 1 < arguments.length) {
                        binaryFile = arguments[i + 1];
                        i++;
                    } else {
                        System.err.println("error");
                        return;
                    }
                    break;
                default:
                    System.err.println("error");
                    return;
            }
        }
        final int memorySize = 256 * 1024;
        final int cacheAssociativity = 4;
        final int cacheTagBits = 8;
        final int cacheIndexBits = 5;
        final int cacheOffsetBits = 5;
        final int cacheTotalSize = 4096;
        final int cacheLineBytes = 32;
        final int cacheLineCount = 128;
        final int cacheSetCount = 32;
        try {
            List<Commands> instructionList = new ArrayList<>();
            if (!Parser.parse(assemblyFile, instructionList)) {
                System.err.println("error");
                return;
            }
            if (binaryFile != null) {
                Parser.getMashCode(instructionList, binaryFile);
            }
            RAM lruMemory = new RAM(memorySize, cacheIndexBits, cacheOffsetBits);
            for (int i = 0; i < instructionList.size(); i++) {
                lruMemory.set(65536 + 4 * i, new MemoryCell[]{instructionList.get(i)});
            }
            Cache lruCache = new Cache(cacheSetCount, cacheAssociativity,
                    cacheLineBytes, true, lruMemory, cacheTagBits,
                    cacheIndexBits, cacheOffsetBits);
            int[] currentProgramCounter = {65536};
            int[] cpuRegisters = new int[32];
            while (currentProgramCounter[0] >= 65536 && currentProgramCounter[0] < 65536 + (instructionList.size() * 4)) {
                MemoryCell fetchedData = lruCache.get(currentProgramCounter[0], 1)[0];
                if (fetchedData.getMemmoryCellTypo() != CellTypo.INST) {
                    return;
                }
                Commands currentInstruction = (Commands) fetchedData;
                currentInstruction.run(lruCache, cpuRegisters, currentProgramCounter);
            }
            int lruTotalHits = lruCache.getAllHits();
            int lruInstructionHits = lruCache.getInstHitRate();
            int lruDataHits = lruCache.getDataHitRate();
            int lruTotalAttempts = lruCache.getTry();
            int lruDataAttempts = lruCache.getDataTry();
            int lruInstructionAttempts = lruCache.getInstTry();
            System.out.println("replacement\thit rate\thit rate (inst)\thit rate (data)");
            printStatistics("LRU", lruTotalHits, lruInstructionHits, lruDataHits, lruTotalAttempts, lruDataAttempts, lruInstructionAttempts);
            RAM plruMemory = new RAM(memorySize, cacheIndexBits, cacheOffsetBits);
            for (int i = 0; i < instructionList.size(); i++) {
                plruMemory.set(65536 + 4 * i, new MemoryCell[]{instructionList.get(i)});
            }
            Cache plruCache = new Cache(cacheSetCount, cacheAssociativity,
                    cacheLineBytes, false, plruMemory, cacheTagBits,
                    cacheIndexBits, cacheOffsetBits);
            currentProgramCounter[0] = 65536;
            cpuRegisters = new int[32];
            while (currentProgramCounter[0] >= 65536 && currentProgramCounter[0] < 65536 + (instructionList.size() * 4)) {
                MemoryCell fetchedData = plruCache.get(currentProgramCounter[0], 1)[0];
                if (fetchedData.getMemmoryCellTypo() != CellTypo.INST) {
                    return;
                }
                Commands currentInstruction = (Commands) fetchedData;
                currentInstruction.run(plruCache, cpuRegisters, currentProgramCounter);
            }
            int plruTotalHits = plruCache.getAllHits();
            int plruInstructionHits = plruCache.getInstHitRate();
            int plruDataHits = plruCache.getDataHitRate();
            int plruTotalAttempts = plruCache.getTry();
            int plruDataAttempts = plruCache.getDataTry();
            int plruInstructionAttempts = plruCache.getInstTry();
            printStatistics("pLRU", plruTotalHits, plruInstructionHits, plruDataHits, plruTotalAttempts, plruDataAttempts, plruInstructionAttempts);
        } catch (Exception e) {
            System.err.println("error");
        }
    }
    public static void printStatistics(String replacementPolicy, int totalHits, int instructionHits, int dataHits, int totalAttempts, int dataAttempts, int instructionAttempts) {
        if (replacementPolicy.equals("LRU")) {
            replacementPolicy = "        " + replacementPolicy;
        } else {
            replacementPolicy = "       " + replacementPolicy;
        }
        if (totalAttempts == 0) {
            System.out.printf(replacementPolicy + "\tnan%%\tnan%%\tnan%%\n");
        } else {
            double totalHitRate = (double) totalHits / totalAttempts * 100;
            double instructionHitRate = instructionAttempts == 0 ? Double.NaN : (double) instructionHits / instructionAttempts * 100;
            double dataHitRate = dataAttempts == 0 ? Double.NaN : (double) dataHits / dataAttempts * 100;
            System.out.print(replacementPolicy + String.format("\t%3.5f%%\t%3.5f%%\t%3.5f%%\n", totalHitRate, instructionHitRate, dataHitRate).toLowerCase());
        }
    }
}