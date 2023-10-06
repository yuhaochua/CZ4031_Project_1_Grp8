import java.util.HashSet;
import java.util.Set;

public class Disk {
    private final int DISK_SIZE;
    private final int MAX_BLOCKS;
    private Set<Block> availBlocks;
    private Set<Block> blockSet;

    public Disk() {
        this.DISK_SIZE = 500 * 1024 * 1024; // 500MB = 500 * 2^20
        this.MAX_BLOCKS = this.DISK_SIZE/Block.MAX_BLOCK_SIZE;
        // this.blockMap = new HashMap<>();
        this.blockSet = new HashSet<>();
        this.availBlocks = new HashSet<>();
    }

    public Address insertRecord(Record record) {
        Address address;
        if(blockSet.size() >= MAX_BLOCKS) {
            return null;
        }
        if (availBlocks.size() < 1) {
            Block newBlock = new Block();
            address = newBlock.addRecord(record);
            blockSet.add(newBlock);
            availBlocks.add(newBlock);
            // if (!newBlock.isFull()) {
            //     blockMap.put(newBlock, true); // indicate that this block still has space
            // }
            return address;
        }
        else {
            // Block availBlock = blockMap.keySet().iterator().next();
            Block availBlock = availBlocks.iterator().next();
            address = availBlock.addRecord(record);
            if (availBlock.isFull()) {
                // blockMap.remove(availBlock);
                availBlocks.remove(availBlock);
            }
            return address;
        }
    }

    public void deleteRecord(Address address) {
        Block block = address.getBlock();
        block.deleteRecord(address.getIndex());
        if (block.isEmpty()) { // if block is empty, we no longer need it
            // blockMap.remove(block);
            availBlocks.remove(block);
            blockSet.remove(block);
        } else { // if block still has space then we add it to the set of availBlocks
            // blockMap.put(block, true);
            availBlocks.add(block);
        }
    }

    public int getNumBlocks() {
        return blockSet.size();
    }

    public Set<Block> getBlockSet() {
        return blockSet;
    }
}
