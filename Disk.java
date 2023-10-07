import java.util.HashSet;
import java.util.Set;

public class Disk {
    private final int DISK_SIZE; 
    private final int MAX_BLOCKS;
    private Set<Block> availBlocks; // HashSet to track if there are any Blocks available for insertion, otherwise will have to create new Block
    private Set<Block> blockSet; // HashSet to track the Blocks which have records on them

    public Disk() {
        this.DISK_SIZE = 500 * 1024 * 1024; // 500MB = 500 * 2^20
        this.MAX_BLOCKS = this.DISK_SIZE/Block.BLOCK_SIZE;
        this.blockSet = new HashSet<>();
        this.availBlocks = new HashSet<>();
    }

    public Address insertRecord(Record record) { // insert record into an available Block
        Address address;
        if(blockSet.size() >= MAX_BLOCKS) {
            return null;
        }
        if (availBlocks.size() < 1) {
            Block newBlock = new Block();
            address = newBlock.addRecord(record);
            blockSet.add(newBlock);
            availBlocks.add(newBlock);
            return address;
        }
        else {
            Block availBlock = availBlocks.iterator().next();
            address = availBlock.addRecord(record);
            if (availBlock.isFull()) {
                availBlocks.remove(availBlock);
            }
            return address;
        }
    }

    public void deleteRecord(Address address) { // delete record from the Block it is in
        Block block = address.getBlock();
        block.deleteRecord(address.getIndex());
        if (block.isEmpty()) { // if block is empty, we no longer need it
            availBlocks.remove(block);
            blockSet.remove(block);
        } else { // if block still has space then we add it to the set of availBlocks
            availBlocks.add(block);
        }
    }

    public int getNumBlocks() { // get number of Blocks stored on Disk
        return blockSet.size();
    }

    public Set<Block> getBlockSet() { // get the set of all Blocks stored on Disk
        return blockSet;
    }
}
