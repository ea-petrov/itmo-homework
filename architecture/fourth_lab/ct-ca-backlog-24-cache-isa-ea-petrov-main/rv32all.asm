add  t3, a4, s1
andi s3, s6, 25
addi t5, zero, 2
auipc x3, 10
beq zero, s4, 0x4
bge x9, x10, 0x1C
bgeu x6, x5, 0x28
blt s1, s3, 45
bltu s7, s8, 65
bltu x11, x12, 0x20
bne x5, x6, 28
div x20, x22, x18
divu x21, x23, x19
fence or, i
jal a4, 32
jalr s5, s6, 15
lb x26, 15, x22
lh x24, 55, x27
lhu x28, 55, x30
lui x7, 25
lbu x27, 55, x29
lw x25, 55, x28
mul x16, x18, x14
mulh x17, x19, x15
mulhsu x18, x20, x16
mulhu x19, x21, x17
ori s1, s7, 20
or x14, x16, x12
rem x22, x24, x20
remu x23, x25, x21
sll x8, x10, x6
slli s9, s1, 30
slt x9, x11, x5
slti t1, s2, 7
sltiu t4, s5, 10
sltu x10, x12, x8
sra x13, x15, x11
srai s7, s9, 1
srl x12, x14, x10
srli s8, s2, 35
sub x7, x9, x3
sb x29, 55, x31
sh x30, 55, x0
sw x31, 55, x1
xor x11, x13, x9
xori a5, a2, 42
ebreak
ecall
