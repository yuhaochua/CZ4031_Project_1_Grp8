import java.util.HashMap;

public class Block {
    public static final int MAX_BLOCK_SIZE = 400; // 400 Bytes max block size

    // private List<Record> records; 
    private Record[] records; // array of records
    private int pointer; // pointer to indicate which index to insert record to
    private HashMap<String, Integer> recordMap; // Map uniqueID of each record to its index in records
    

    public Block() {
        this.records = new Record[16];
        pointer = 0;
    }

    public void addRecord(Record record) {
        records[pointer] = record;
        String uniqueID = Integer.valueOf(record.getGame_date_est()).toString() + Integer.valueOf(record.getTeam_id_home()).toString();
        recordMap.put(uniqueID, pointer);
        pointer++;
    }

    public void deleteRecord(Record record) {

    }
}