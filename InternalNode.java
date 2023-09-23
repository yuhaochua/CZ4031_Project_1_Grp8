// child class of node, has max of n+1 pointers to child nodes
// has minimum of floor(n/2) keys
public class InternalNode extends Node {
    // Has a total of n+1 pointers
    private Node[] childPointers; // 4 bytes * (n+1), pointers to child nodes(could be internal or leaf nodes)

    public InternalNode() {
        super();
        childPointers = new Node[n+1];
    }

    public Node[] getChildPointers() {
        return childPointers;
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
