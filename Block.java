import java.util.ArrayList;

public class Block {
    public static final int BLOCK_SIZE = 400; // 400 bytes block size
    public static final int RECORD_SIZE = 32; // 32 bytes record size
    public static final int MAX_NUM_RECORDS = BLOCK_SIZE/RECORD_SIZE;
    private Record[] records; // array of records
    private ArrayList<Integer> availIndex = new ArrayList<>(); // keep track of the available slots in block to store a record
    

    public Block() {
        this.records = new Record[MAX_NUM_RECORDS];
        for(int i=0; i<MAX_NUM_RECORDS; i++) { // initially, all slots should be available for record to be inserted into
            availIndex.add(i); 
        }
    }

    public Address addRecord(Record record) {
        int offset = availIndex.get(0);
        records[offset] = record;
        availIndex.remove(0);

        return new Address(this, offset);
    }

    public void deleteRecord(int index) {
        availIndex.add(index);
    }

    public boolean isFull() {
        if(availIndex.size() == 0) return true;
        return false;
    }

    public boolean isEmpty() {
        if(availIndex.size() == MAX_NUM_RECORDS) return true;
        return false;
    }

    public Record[] getRecords() {
        return records;
    }
}

