public record CellByte(int value) implements MemoryCell {

    @Override
    public CellTypo getMemmoryCellTypo() {
        return CellTypo.BYTE;
    }
}
