public class BPlusTree {
    private Node rootNode;

    public BPlusTree() {
        rootNode = new LeafNode();
    }

    // count the number of nodes in this B+ tree
    public int countNodes() {
        // IMPLEMENTATION
        return rootNode.countNodes();
    }

    // count the number of levels in this B+ tree
    public int countLevels() {
        // IMPLEMENTATION
        if(rootNode.getClass().toString().equals("class LeafNode")) return 1;
        int numOfLevels = 1;
        InternalNode internalNode = (InternalNode) rootNode;
        Node childNode;
        while(true) {
            childNode = internalNode.getChildPointers()[0];
            if(childNode.isLeaf()) {
                numOfLevels++;
                break;
            }
            internalNode = (InternalNode) childNode;
            numOfLevels++;
        }
        return numOfLevels;
    }

    public void rootNodeContent() {
        System.out.print("Keys in the root node are:");
        for(int i=0; i<Node.n; i++) {
            if(rootNode.getKeys()[i] == Float.MAX_VALUE) break;
            System.out.print(" " + rootNode.getKeys()[i]);
        }
        System.out.println();
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
    public boolean deleteRecord(float key, Disk disk) {
        // IMPLEMENTATION
        boolean deleted = rootNode.deleteRecord(key, disk, null, null);
        if(rootNode.getNumKeys() == 0) {
            InternalNode oldRoot = (InternalNode) rootNode;
            rootNode = oldRoot.getChildPointers()[0];
            rootNode.setParent(null);
        }
        return deleted;
    }

    // search for records with "FG_PCT_home" equal to certain value and return the average "FG3_PCT_home" of those records
    // need to report number of index nodes AND number of data blocks the process access, maybe can use some sort of print statement within this method
    // need to report running time of this search query
    public float searchQuery(float key) {
        return rootNode.searchQuery(key);
    }

    // search for records with "FG_PCT_home" within lowerKey and upperKey, both inclusively, and return the average "FG3_PCT_home" of those records
    // need to report number of index nodes AND number of data blocks the process access, maybe can use some sort of print statement within this method
    // need to report running time of this range query
    public float rangeQuery(float lowerKey, float upperKey) {
        // IMPLEMENTATION
        return rootNode.rangeQuery(lowerKey, upperKey);
        
    }
}
