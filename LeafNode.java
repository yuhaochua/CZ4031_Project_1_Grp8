// child class of node, has max of n pointers pointing to actual records, and 1 pointer pointing to next leaf node
// has minimum of floor((n+1)/2) keys
public class LeafNode extends Node {
    // Has a total of n+1 pointers
    private Address[] dataPointers; // 4 bytes * n, pointers to actual records stored in blocks
    private LeafNode nextLeafNode; // pointer to the neighboring leaf node

    public LeafNode() {
        super();
        dataPointers = new Address[n];
    }

    public LeafNode getNextLeafNode() {
        return nextLeafNode;
    }

    public void setNextLeafNode(LeafNode nextLeafNode) {
        this.nextLeafNode = nextLeafNode;
    }

    public Address[] getDataPointers() {
        return dataPointers;
    }

    public void insertRecord(float key) {
        // IMPLEMENTATION
    }

    public void deleteRecord(float key) {
        // IMPLEMENTATION
    }

    public float searchQuery(float key) {
        // IMPLEMENTATION
        return (float) 0.1;
    }

    public float rangeQuery(float lowerKey, float upperKey) {
        // IMPLEMENTATION

        return (float) 0.1;
    }
}
