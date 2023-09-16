import java.util.ArrayList;
import java.util.HashMap;

public class Block {
    public static final int MAX_BLOCK_SIZE = 400; // 400 bytes max block size
    public static final int MAX_RECORD_SIZE = 24; // 24 bytes record size
    private static final int MAX_NUM_RECORDS = MAX_BLOCK_SIZE/MAX_RECORD_SIZE;


    // private List<Record> records; 
    private Record[] records; // array of records
    private ArrayList<Integer> availIndex = new ArrayList<>(); // keep track of the available slots in block to store a record
    private HashMap<String, Integer> recordMap; // Map uniqueID of each record to its index in records
    

    public Block() {
        this.records = new Record[16];
        for(int i=0; i<MAX_NUM_RECORDS; i++) { // initially, all slots should be available for record to be inserted into
            availIndex.add(i); 
        }
    }

    public void addRecord(Record record) {
        int pointer = availIndex.get(0);
        records[pointer] = record;
        String uniqueID = Integer.valueOf(record.getGame_date_est()).toString() + Integer.valueOf(record.getTeam_id_home()).toString();
        recordMap.put(uniqueID, pointer);
        availIndex.remove(0);
    }

    public void deleteRecord() {
        int pointer = recordMap.get("uniqueID");
        availIndex.add(pointer);
    }

    public boolean isFull() {
        if(availIndex.size() == 0) return true;
        return false;
    }

    public boolean isEmpty() {
        if(availIndex.size() == MAX_NUM_RECORDS) return true;
        return false;
    }
}