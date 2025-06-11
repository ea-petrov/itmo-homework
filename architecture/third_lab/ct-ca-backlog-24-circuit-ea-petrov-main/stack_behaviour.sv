module stack_behaviour_normal(
    inout wire [3:0] IO_DATA,
    input wire RESET,
    input wire CLK,
    input wire [1:0] COMMAND,
    input wire [2:0] INDEX
);
    reg [3:0] out = 4'bzzzz;
    assign IO_DATA = out;
    reg [4:0][3:0] memory;
    reg [2:0] top = 0;

    wire notCLK;
    assign notCLK = !CLK;

    always @(posedge notCLK) begin
        out = 4'bzzzz;
    end

    always @(*) begin
        if (RESET) begin
            top = 0;
            memory[0] = 4'b0000;
            memory[1] = 4'b0000;
            memory[2] = 4'b0000;
            memory[3] = 4'b0000;
            memory[4] = 4'b0000;
            out = 4'bzzzz;
        end
        else if (CLK) begin
            if (COMMAND == 2'b00) begin
                out = 4'bzzzz;
            end
            else if (COMMAND == 2'b01) begin
                memory[top] = IO_DATA;
                top = (top + 1) % 5;
                out = 4'bzzzz;
            end
            else if (COMMAND == 2'b10) begin
                top = (top + 4) % 5;
                out = memory[top];
            end
            else if (COMMAND == 2'b11) begin
                out = memory[(top - INDEX + 9) % 5];
            end
        end
    end
endmodule
