import java.io.*;
import java.util.*;

public class Parser {

    public static String getR(String name, int[] params) {
        int rd = params[0], rs1 = params[1], rs2 = params[2];
        String funct3 = Maps.funct3.getOrDefault(name, "000");
        String funct7 = Maps.funct7.getOrDefault(name, "0000000");
        String opcode = "0110011";
        return funct7 +
                toBin(rs2, 5) +
                toBin(rs1, 5) +
                funct3 +
                toBin(rd, 5) +
                opcode;
    }
    public static String getI(String name, int[] params) {
        if (params.length == 0) {
            if (name.equals("ecall")) {
                return "00000000000000000000000001110011";
            } else {
                return "00000000000100000000000001110011";
            }
        }
        int rd = params[0], rs1 = params[1], imm = params[2];
        String funct3 = Maps.funct3.getOrDefault(name, "000");
        String opcode = "0010011";
        if (name.equals("jalr")) {
            opcode = "1100111";
        }
        if (name.startsWith("l")) {
            opcode = "0000011";
        } else if (name.startsWith("e")) {
            opcode = "1110011";
        } else if (name.equals("slli") || name.equals("srai") || name.equals("srli")) {
            imm = imm & (31);
            if (name.equals("srai")) {
                imm |= (1 << 10);
            }
        }
        return toBin(imm, 12) +
                toBin(rs1, 5) +
                funct3 +
                toBin(rd, 5) +
                opcode;
    }

    public static String getS(String name, int[] params) {
        if (params.length != 3) {
            throw new IllegalArgumentException("S-type instructions require 3 parameters: rs1, rs2, imm.");
        }
        int rs1 = params[1], rs2 = params[0], imm = params[2];
        String funct3 = Maps.funct3.getOrDefault(name, "000");
        String opcode = "0100011";
        int immHigh = (imm >> 5) & ((1 << 11) - 1);
        int immLow = imm & 31;
        return toBin(immHigh, 7) +
                toBin(rs2, 5) +
                toBin(rs1, 5) +
                funct3 +
                toBin(immLow, 5) +
                opcode;
    }

    public static String getB(String name, int[] params) {
        int imm = params[2];
        return ((imm >> 12) & 0x1) +
                toBin((imm >> 5) & 0x3F, 6) +
                toBin(params[1], 5) +
                toBin(params[0], 5) +
                Maps.funct3.getOrDefault(name, "000") +
                toBin((imm >> 1) & 0xF, 4) +
                ((imm >> 11) & 0x1) +
                "1100011";
    }

    public static String getU(String name, int[] params) {
        return toBin(params[1], 20) +
                toBin(params[0], 5) +
                (name.equals("lui") ? "0110111" : "0010111");
    }

    public static String getJ(String name, int[] params) {
        int rd = params[0], imm = params[1];
        String opcode = "1101111";

        int imm1 = (imm >> 20) & 0x1;
        int imm2 = (imm >> 1) & 0x3FF;
        int imm3 = (imm >> 11) & 0x1;
        int imm4 = (imm >> 12) & 0xFF;

        return imm1 +
                toBin(imm2, 10) +
                imm3 +
                toBin(imm4, 8) +
                toBin(rd, 5) +
                opcode;
    }

    public static String getFence(String[] s) {
        String pred = s.length > 0 ? s[0] : "1111";
        String succ = s.length > 1 ? s[1] : "1111";
        String opcode = "0001111";
        String funct3 = "000";
        String rs1 = "00000";
        String rd = "00000";
        String predBinary = toBin(pred);
        String succBinary = toBin(succ);
        String imm = "0000" + predBinary + succBinary;
        return imm + rs1 + funct3 + rd + opcode;
    }

    private static String toBin(int value, int bits) {
        String binary = Integer.toBinaryString(value & ((1 << bits) - 1));
        return String.format("%" + bits + "s", binary).replace(' ', '0');
    }

    public static byte[] formatting(String s) {
        byte[] byteArray = new byte[4];
        for (int i = 0; i < 32; i++) {
            int byteIndex = i / 8;
            byteArray[byteIndex] <<= 1;
            if (s.charAt(i) == '1') {
                byteArray[byteIndex] |= 1;
            }
        }
        for (int i = 0; i < 2; i++) {
            byte temp = byteArray[i];
            byteArray[i] = byteArray[3 - i];
            byteArray[3 - i] = temp;
        }
        return byteArray;
    }
    private static String toBin(String s) {
        if (s.matches("[01]+")) {
            return padLeft(s);
        } else if (s.matches("[iorw]+")) {
            int value = 0;
            if (s.contains("i")) value |= 0b1000;
            if (s.contains("o")) value |= 0b0100;
            if (s.contains("r")) value |= 0b0010;
            if (s.contains("w")) value |= 0b0001;
            return padLeft(Integer.toBinaryString(value));
        } else {
            throw new IllegalArgumentException("Invalid pred/succ format: " + s);
        }
    }
    private static String padLeft(String s) {
        StringBuilder binaryBuilder = new StringBuilder(s);
        while (binaryBuilder.length() < 4) {
            binaryBuilder.insert(0, "0");
        }
        s = binaryBuilder.toString();
        return s;
    }
    public static int getImm(int i, int pos) {
        int sign = 1;
        if ((i & (1 << (pos - 1))) == 1) {
            i -= 1;
            i = (~i) & ((1 << pos) - 1);
            sign = -1;
        }
        return i * sign;
    }
    public static Commands getRType(String[] s) {
        int rd = Maps.regMap.get(s[1]);
        int rs1 = Maps.regMap.get(s[2]);
        int rs2 = Maps.regMap.get(s[3]);
        return new Commands(s[0], new int[]{rd, rs1, rs2});

    }
    public static Commands getIType(String[] s) {
        if (s[0].startsWith("l")) {
            int rd = Maps.regMap.get(s[1]);
            int rs1 = Maps.regMap.get(s[3]);
            int imm = getImm(Integer.decode(s[2]) & ((1 << 12) - 1), 12);
            return new Commands(s[0], new int[]{rd, rs1, imm, Integer.decode(s[2])});
        } else if (s[0].startsWith("e")) {
            return new Commands(s[0], new int[0]);
        }
        int rd = Maps.regMap.get(s[1]);
        int rs1 = Maps.regMap.get(s[2]);
        int imm = getImm(Integer.decode(s[3]) & ((1 << 12) - 1), 12);
        if (s[0].equals("srai")) imm |= (1 << 10);
        return new Commands(s[0], new int[]{rd, rs1, imm, Integer.decode(s[3])});

    }
    public static Commands getSType(String[] s) {
        int rs1 = Maps.regMap.get(s[1]);
        int rs2 = Maps.regMap.get(s[3]);
        int imm = getImm(Integer.decode(s[2]) & ((1 << 12) - 1), 12);
        return new Commands(s[0], new int[]{rs1, rs2, imm});

    }
    public static Commands getBType(String[] s) {
        int rs1 = Maps.regMap.get(s[1]);
        int rs2 = Maps.regMap.get(s[2]);

        int imm = getImm(Integer.decode(s[3]) & ((1 << 13) - 1), 13);
        return new Commands(s[0], new int[]{rs1, rs2, imm, Integer.decode(s[3])});

    }

    public static Commands getUType(String[] s) {
        int rd = Maps.regMap.get(s[1]);
        int imm = Integer.decode(s[2]);
        return new Commands(s[0], new int[]{rd, imm});
    }

    public static Commands getJType(String[] tokens) {
        int rd = Maps.regMap.get(tokens[1]);
        int imm = getImm((Integer.decode(tokens[2])) & ((1 << 21) - 1), 21);
        return new Commands(tokens[0], new int[]{rd, imm - imm % 2, Integer.decode(tokens[2])});
    }

    private static int countChar(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    private static Commands getFenceType(String[] tokens) {
        for (int i = 1; i < tokens.length; i++) {
            String a = tokens[i];
            if (a.length() > 4) {
                throw new IllegalArgumentException("error");
            }
            boolean flag = false;
            if (countChar(a, 'r') + countChar(a, 'i') + countChar(a, 'o') + countChar(a, 'w') == a.length()) {
                flag = true;
                tokens[i] = (a.contains("i") ? "1" : "0") + (a.contains("o") ? "1" : "0") + (a.contains("r") ? "1" : "0") + (a.contains("w") ? "1" : "0");
            }
            if (!flag) {
                int v;
                try {
                    v = Integer.decode(a);
                } catch (Exception e) {
                    throw new IllegalArgumentException("error");
                }
                tokens[i] = Integer.toBinaryString(v & 15);
            }
        }
        String[] args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, args.length);
        return new Commands(tokens[0], args);
    }

    public static Commands doCommands(String[] tokens) {
        String instruction = tokens[0].toLowerCase();
        String type = Maps.instruct.get(instruction);
        try {
            return switch (type) {
                case "R" -> getRType(tokens);
                case "I" -> getIType(tokens);
                case "S" -> getSType(tokens);
                case "B" -> getBType(tokens);
                case "U" -> getUType(tokens);
                case "J" -> getJType(tokens);
                case "fence" -> getFenceType(tokens);
                default -> throw new IllegalStateException("Unsupported type: " + type);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("error");
        }
    }

    public static String doMashineCode(Commands inst) throws IOException {
        String type = Maps.instruct.get(inst.getNameForValue());
        return switch (type) {
            case "R" -> getR(inst.getNameForValue(), inst.getrg());
            case "I" -> getI(inst.getNameForValue(), inst.getrg());
            case "S" -> getS(inst.getNameForValue(), inst.getrg());
            case "B" -> getB(inst.getNameForValue(), inst.getrg());
            case "U" -> getU(inst.getNameForValue(), inst.getrg());
            case "J" -> getJ(inst.getNameForValue(), inst.getrg());
            case "fence" -> getFence(inst.getrgFence());
            default -> throw new IllegalStateException("Unsupported type: " + type);
        };
    }

    public static void getMashCode(List<Commands> instructionList, String outputFile) {
        try {
            FileOutputStream writer = new FileOutputStream(outputFile);
            try {
                for (Commands i : instructionList) {
                    writer.write(formatting(doMashineCode(i)));
                }
            } catch (IOException e) {
                System.err.println("Error writing the output file: " + e.getMessage());
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error closing the output file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error opening the output file: " + e.getMessage());
        }
    }
    public static boolean go(String inputFileName, StringBuilder result) {
        File inputFile = new File(inputFileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String modifiedLine = line.replace(",", " , ");
                result.append(modifiedLine).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("error");
            return false;
        }
        return true;
    }

    public static boolean parse(String input, List<Commands> out) {
        StringBuilder sb = new StringBuilder();
        boolean isSuccess = go(input, sb);
        if (!isSuccess) {
            return false;
        }
        try {
            boolean hasNext = false;
            String prevNext = "";
            Scanner sc = new Scanner(sb.toString());
            while (sc.hasNext()) {
                String next = !hasNext ? sc.next() : prevNext;
                hasNext = false;
                String[] args = new String[0];
                if (next.equals("fence")) {
                    if (!sc.hasNext()) {
                        args = new String[0];
                    } else {
                        String arg = sc.next();
                        if (arg.equals("or") || arg.equals("ori")) {
                            if (!sc.hasNext()) {
                                args = new String[1];
                                args[0] = arg;
                            } else {
                                String word = sc.next();
                                if (word.equals(",")) {
                                    if (!sc.hasNext()) {
                                        return false;
                                    }
                                    String nextArg = sc.next();
                                    if (nextArg.equals(",")) {
                                        return false;
                                    } else {
                                        args = new String[2];
                                        args[0] = arg;
                                        args[1] = nextArg;
                                    }
                                } else {
                                    hasNext = true;
                                    prevNext = word;
                                }
                            }
                        } else if (Maps.instruct.containsKey(arg)) {
                            args = new String[0];
                            hasNext = true;
                            prevNext = arg;
                        } else {
                            if (!sc.hasNext()) {
                                args = new String[1];
                                args[0] = arg;

                            } else {

                                String word = sc.next();
                                if (word.equals(",")) {
                                    String nextArg = sc.next();
                                        args = new String[2];
                                        args[0] = arg;
                                        args[1] = nextArg;
                                } else {
                                    args = new String[1];
                                    args[0] = arg;
                                    hasNext = true;
                                    prevNext = word;
                                }
                            }
                        }

                    }
                } else {
                    if (!Maps.instruct.containsKey(next)) {
                        return false;
                    }
                    String type = Maps.instruct.get(next);
                    int c = Maps.fmtMap.get(type);
                    if (next.equals("ebreak") || next.equals("ecall")) {
                        c = 0;
                    }
                    args = new String[c];

                    for (int i = 0; i < args.length; i++) {
                        if (!sc.hasNext()) {
                            return false;
                        }
                        String arg = sc.next();
                        if (arg.equals(",")) {
                            return false;
                        }
                        args[i] = arg;
                        if (i + 1 != args.length) {
                            String comma = sc.next();
                            if (!comma.equals(",")) {
                                return false;
                            }
                        }
                    }
                }
                String[] tokens = new String[1 + args.length];
                tokens[0] = next;
                System.arraycopy(args, 0, tokens, 1, args.length);
                out.add(doCommands(tokens));
            }
        } catch (Exception e) {
            System.out.println("error ");
            return false;
        }
        return true;
    }
}
