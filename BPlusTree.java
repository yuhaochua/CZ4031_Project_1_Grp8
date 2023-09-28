public class BPlusTree {
    private Node rootNode; 

    public BPlusTree() {
        rootNode = new LeafNode();
    }

    // count the number of nodes in this B+ tree
    public int countNodes() {
        // IMPLEMENTATION
        return 1;
    }

    // count the number of levels in this B+ tree
    public int countLevels() {
        // IMPLEMENTATION
        return 1;
    }

    // retrieve root node of this B+ tree
    // will likely use this for expriement 2, to display the keys of the root node
    public Node getRootNode() {
        return rootNode;
    }

    public void enumerateNodes() {
        rootNode.enumerateNodes();
    }

    // insert record into B+ tree
    public void insertRecord(Address address) {
        // IMPLEMENTATION
        rootNode.insertRecord(address);
        if(rootNode.getParent() != null) rootNode = rootNode.getParent();
    }

    // delete record from B+ tree
    // need to report running time of this delete process
    public void deleteRecord() {
        // IMPLEMENTATION
    }

    // search for records with "FG_PCT_home" equal to certain value and return the average "FG3_PCT_home" of those records
    // need to report number of index nodes AND number of data blocks the process access, maybe can use some sort of print statement within this method
    // need to report running time of this search query
    public float searchQuery(float key) {
        // IMPLEMENTATION
        return (float) 0.1;
    }

    // search for records with "FG_PCT_home" within lowerKey and upperKey, both inclusively, and return the average "FG3_PCT_home" of those records
    // need to report number of index nodes AND number of data blocks the process access, maybe can use some sort of print statement within this method
    // need to report running time of this range query
    public float rangeQuery(float lowerKey, float upperKey) {
        // IMPLEMENTATION
        return (float) 0.1;
    }
}
