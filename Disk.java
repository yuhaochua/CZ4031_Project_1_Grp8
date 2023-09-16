import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Disk {
    private int final DISK_SIZE;
    private int maxBlocks;
    private Map<Block, Boolean> blockMap;
    private Set<Block> blockSet;

    public Disk() {
        this.DISK_SIZE = 500 * 1024 * 1024;
        this.maxBlocks = 1250000;
        this.blockMap = new HashMap<>();
        this.blockSet = new HashSet<>();
    }

    public Block insertRecord(Record record) {
        if (blockMap.size() < 1) {
            Block newBlock = new Block();
            newBlock.addRecord(record);
            blockSet.add(newBlock);
            if (!newBlock.isFull()) {
                blockMap.put(newBlock, true);
            }
            return newBlock;
        }
        else {
            Block newBlock = blockMap.keySet().iterator().next();
            newBlock.addRecord(record);
            if (newBlock.isFull()) {
                blockMap.remove(newBlock);
            }
            return newBlock;
        }
    }

    public void deleteRecord(Block block, Record record) {
        block.deleteRecord(record);
        if (block.isEmpty()) {
            blockMap.remove(block);
            blockSet.remove(block);
        } else {
            blockMap.put(block, true);
        }
    }
}
