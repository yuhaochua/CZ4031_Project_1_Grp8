public class Address {
    private Block block;
    private int index;

    public Address(Block block, int index) {
        this.block = block;
        this.index = index;
    }

    public Block getBlock() {
        return block;
    }

    public int getIndex() {
        return index;
    }
}
