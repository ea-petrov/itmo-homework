public class Commands implements MemoryCell {
    private String name;
    private int[] arg;
    private String[] fencearg;

    public Commands(String name, int[] args) {
        this.name = name;
        this.arg = args;
        this.fencearg = new String[0];
    }

    public Commands(String name, String[] args) {
        this.name = name;
        this.fencearg = args;
        this.arg = new int[0];
    }

    public void run(Cache cache, int[] registers, int[] programCounter) {
        switch (name) {
            case "jalr":
                if (arg[0] != 0) registers[arg[0]] = programCounter[0] + 4;
                programCounter[0] = (registers[arg[1]] + arg[2]) & ~1;
                break;
            case "lui":
                if (arg[0] != 0) registers[arg[0]] = arg[1] << 12;
                programCounter[0] += 4;
                break;
            case "auipc":
                if (arg[0] != 0) registers[arg[0]] = programCounter[0] + (arg[1] << 12);
                programCounter[0] += 4;
                break;
            case "ecall":
            case "ebreak":
            case "fence":
                programCounter[0] += 4;
                break;
            case "mul":
                if (arg[0] != 0) registers[arg[0]] = registers[arg[1]] * registers[arg[2]];
                programCounter[0] += 4;
                break;
            case "mulh":
                if (arg[0] != 0) registers[arg[0]] = (int) (((long) registers[arg[1]] * registers[arg[2]]) >> 32);
                programCounter[0] += 4;
                break;
            case "mulhsu":
                if (arg[0] != 0)
                    registers[arg[0]] = (int) (((long) registers[arg[1]] * Integer.toUnsignedLong(registers[arg[2]])) >>> 32);
                programCounter[0] += 4;
                break;
            case "mulhu":
                if (arg[0] != 0)
                    registers[arg[0]] = (int) ((Integer.toUnsignedLong(registers[arg[1]]) * Integer.toUnsignedLong(registers[arg[2]])) >>> 32);
                programCounter[0] += 4;
                break;
            case "div":
                if (arg[0] != 0)
                    registers[arg[0]] = registers[arg[2]] == 0 ? -1 : registers[arg[1]] / registers[arg[2]];
                programCounter[0] += 4;
                break;
            case "divu":
                if (arg[0] != 0)
                    registers[arg[0]] = registers[arg[2]] == 0 ? -1 : Integer.divideUnsigned(registers[arg[1]], registers[arg[2]]);
                programCounter[0] += 4;
                break;
            case "rem":
                if (arg[0] != 0)
                    registers[arg[0]] = registers[arg[2]] == 0 ? registers[arg[1]] : registers[arg[1]] % registers[arg[2]];
                programCounter[0] += 4;
                break;
            case "remu":
                if (arg[0] != 0)
                    registers[arg[0]] = registers[arg[2]] == 0 ? registers[arg[1]] : Integer.remainderUnsigned(registers[arg[1]], registers[arg[2]]);
                programCounter[0] += 4;
                break;
            case "add":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] + registers[arg[2]]);
                programCounter[0] += 4;
                break;
            case "sub":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] - registers[arg[2]]);
                programCounter[0] += 4;
                break;
            case "sll":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] << (registers[arg[2]] & 0x1F));
                programCounter[0] += 4;
                break;
            case "slt":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] < registers[arg[2]]) ? 1 : 0;
                programCounter[0] += 4;
                break;
            case "sltu":
                if (arg[0] != 0)
                    registers[arg[0]] = Integer.compareUnsigned(registers[arg[1]], registers[arg[2]]) < 0 ? 1 : 0;
                programCounter[0] += 4;
                break;
            case "xor":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] ^ registers[arg[2]]);
                programCounter[0] += 4;
                break;
            case "srl":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] >>> (registers[arg[2]] & 0x1F));
                programCounter[0] += 4;
                break;
            case "sra":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] >> (registers[arg[2]] & 0x1F));
                programCounter[0] += 4;
                break;
            case "or":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] | registers[arg[2]]);
                programCounter[0] += 4;
                break;
            case "and":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] & registers[arg[2]]);
                programCounter[0] += 4;
                break;

            case "addi":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] + arg[3]);
                programCounter[0] += 4;
                break;
            case "slti":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] < arg[3]) ? 1 : 0;
                programCounter[0] += 4;
                break;
            case "sltiu":
                if (arg[0] != 0) registers[arg[0]] = Integer.compareUnsigned(registers[arg[1]], arg[3]) < 0 ? 1 : 0;
                programCounter[0] += 4;
                break;
            case "xori":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] ^ arg[3]);
                programCounter[0] += 4;
                break;
            case "ori":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] | arg[3]);
                programCounter[0] += 4;
                break;
            case "andi":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] & arg[3]);
                programCounter[0] += 4;
                break;
            case "slli":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] << (arg[3] & 0x1F));
                programCounter[0] += 4;
                break;
            case "srli":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] >> (arg[3] & 0x1F));
                programCounter[0] += 4;
                break;
            case "srai":
                if (arg[0] != 0) registers[arg[0]] = (registers[arg[1]] / (1 << (arg[3] & 0x1F)));
                programCounter[0] += 4;
                break;
            case "lb", "lh", "lw", "lbu", "lhu":
                if (arg[0] != 0) registers[arg[0]] = cache.load(registers[arg[1]] + arg[2]);
                programCounter[0] += 4;
                break;
            case "sb", "sh", "sw":
                cache.storeByte(registers[arg[0]] + arg[2], registers[arg[1]]);
                programCounter[0] += 4;
                break;
            case "beq":
                if (registers[arg[0]] == registers[arg[1]]) {
                    programCounter[0] += arg[3];
                } else {
                    programCounter[0] += 4;
                }
                break;
            case "bne":
                if (registers[arg[0]] != registers[arg[1]]) {
                    programCounter[0] += arg[3];
                } else {
                    programCounter[0] += 4;
                }
                break;
            case "blt":
                if (registers[arg[0]] < registers[arg[1]]) {
                    programCounter[0] += arg[3];
                } else {
                    programCounter[0] += 4;
                }
                break;
            case "bge":
                if (registers[arg[0]] >= registers[arg[1]]) {
                    programCounter[0] += arg[3];
                } else {
                    programCounter[0] += 4;
                }
                break;
            case "bltu":
                if (Integer.compareUnsigned(registers[arg[0]], registers[arg[1]]) < 0) {
                    programCounter[0] += arg[3];
                } else {
                    programCounter[0] += 4;
                }
                break;
            case "bgeu":
                if (Integer.compareUnsigned(registers[arg[0]], registers[arg[1]]) >= 0) {
                    programCounter[0] += arg[3];
                } else {
                    programCounter[0] += 4;
                }
                break;
            case "jal":
                if (arg[0] != 0) registers[arg[0]] = programCounter[0] + 4;
                programCounter[0] += arg[2];
                break;
            default:
                throw new IllegalArgumentException(name + " not in instruction list");
        }
    }

    public String getNameForValue() {
        return name;
    }

    public int[] getrg() {
        return arg;
    }

    public String[] getrgFence() {
        return fencearg;
    }

    @Override
    public CellTypo getMemmoryCellTypo() {
        return CellTypo.INST;
    }
}