import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static final Map<String, String> instruct = new HashMap<>();
    public static final Map<String, Integer> fmtMap = Map.of(
            "R", 3,
            "I", 3,
            "S", 3,
            "B", 3,
            "U", 2,
            "J", 2
    );
    public static final Map<String, String> funct3 = new HashMap<>();
    public static final Map<String, String> funct7 = new HashMap<>();
    public static final Map<String, Integer> regMap = new HashMap<>();

    static {
        regMap.put("zero", 0);  // x0
        regMap.put("ra", 1);    // x1
        regMap.put("sp", 2);    // x2
        regMap.put("gp", 3);    // x3
        regMap.put("tp", 4);    // x4
        regMap.put("t0", 5);    // x5
        regMap.put("t1", 6);    // x6
        regMap.put("t2", 7);    // x7
        regMap.put("s0", 8);    // x8
        regMap.put("s1", 9);    // x9
        regMap.put("a0", 10);   // x10
        regMap.put("a1", 11);   // x11
        regMap.put("a2", 12);   // x12
        regMap.put("a3", 13);   // x13
        regMap.put("a4", 14);   // x14
        regMap.put("a5", 15);   // x15
        regMap.put("a6", 16);   // x16
        regMap.put("a7", 17);   // x17
        regMap.put("s2", 18);   // x18
        regMap.put("s3", 19);   // x19
        regMap.put("s4", 20);   // x20
        regMap.put("s5", 21);   // x21
        regMap.put("s6", 22);   // x22
        regMap.put("s7", 23);   // x23
        regMap.put("s8", 24);   // x24
        regMap.put("s9", 25);   // x25
        regMap.put("s10", 26);  // x26
        regMap.put("s11", 27);  // x27
        regMap.put("t3", 28);   // x28
        regMap.put("t4", 29);   // x29
        regMap.put("t5", 30);   // x30
        regMap.put("t6", 31);   // x31
        for (int i = 0; i <= 31; i++) {
            regMap.put("x" + i, i);
        }
        instruct.put("add", "R");
        instruct.put("sub", "R");
        instruct.put("xor", "R");
        instruct.put("or", "R");
        instruct.put("and", "R");
        instruct.put("sll", "R");
        instruct.put("srl", "R");
        instruct.put("sra", "R");
        instruct.put("slt", "R");
        instruct.put("sltu", "R");
        instruct.put("addi", "I");
        instruct.put("xori", "I");
        instruct.put("ori", "I");
        instruct.put("andi", "I");
        instruct.put("slli", "I");
        instruct.put("srli", "I");
        instruct.put("srai", "I");
        instruct.put("slti", "I");
        instruct.put("sltiu", "I");
        instruct.put("lb", "I");
        instruct.put("lh", "I");
        instruct.put("lw", "I");
        instruct.put("lbu", "I");
        instruct.put("lhu", "I");
        instruct.put("sb", "S");
        instruct.put("sh", "S");
        instruct.put("sw", "S");
        instruct.put("beq", "B");
        instruct.put("bne", "B");
        instruct.put("blt", "B");
        instruct.put("bge", "B");
        instruct.put("bltu", "B");
        instruct.put("bgeu", "B");
        instruct.put("jal", "J");
        instruct.put("jalr", "I");
        instruct.put("lui", "U");
        instruct.put("auipc", "U");
        instruct.put("ecall", "I");
        instruct.put("ebreak", "I");
        instruct.put("mul", "R");
        instruct.put("mulh", "R");
        instruct.put("mulhsu", "R");
        instruct.put("mulhu", "R");
        instruct.put("div", "R");
        instruct.put("divu", "R");
        instruct.put("rem", "R");
        instruct.put("remu", "R");
        instruct.put("fence", "fence");
        funct3.put("add", "000");
        funct3.put("sub", "000");
        funct3.put("xor", "100");
        funct3.put("or", "110");
        funct3.put("and", "111");
        funct3.put("sll", "001");
        funct3.put("srl", "101");
        funct3.put("sra", "101");
        funct3.put("slt", "010");
        funct3.put("sltu", "011");
        funct3.put("addi", "000");
        funct3.put("xori", "100");
        funct3.put("ori", "110");
        funct3.put("andi", "111");
        funct3.put("slli", "001");
        funct3.put("srli", "101");
        funct3.put("srai", "101");
        funct3.put("slti", "010");
        funct3.put("sltiu", "011");
        funct3.put("lb", "000");
        funct3.put("lh", "001");
        funct3.put("lw", "010");
        funct3.put("lbu", "100");
        funct3.put("lhu", "101");
        funct3.put("sb", "000");
        funct3.put("sh", "001");
        funct3.put("sw", "010");
        funct3.put("beq", "000");
        funct3.put("bne", "001");
        funct3.put("blt", "100");
        funct3.put("bge", "101");
        funct3.put("bltu", "110");
        funct3.put("bgeu", "111");
        funct3.put("jalr", "000");
        funct3.put("mul", "000");
        funct3.put("mulh", "001");
        funct3.put("mulhsu", "010");
        funct3.put("mulhu", "011");
        funct3.put("div", "100");
        funct3.put("divu", "101");
        funct3.put("rem", "110");
        funct3.put("remu", "111");
        funct7.put("add", "0000000");
        funct7.put("sub", "0100000");
        funct7.put("srl", "0000000");
        funct7.put("sra", "0100000");
        funct7.put("jalr", "0000001");
        funct7.put("mul", "0000001");
        funct7.put("mulh", "0000001");
        funct7.put("mulhsu", "0000001");
        funct7.put("mulhu", "0000001");
        funct7.put("div", "0000001");
        funct7.put("divu", "0000001");
        funct7.put("rem", "0000001");
        funct7.put("remu", "0000001");
    }
}
