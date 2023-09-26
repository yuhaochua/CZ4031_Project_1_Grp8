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
        // TODO use binary search on node to find spot to insert
        // TODO find a better way to insert node
        // TODO shift all childPointers if inserting a node in between
        if (this.numKeys < Node.n) {
            int insertPos = 0;
            while (insertPos < Node.n && this.keys[insertPos] < key) {
                insertPos++;
            }

            // Shift right
            for (int i = Node.n - 1; i > insertPos; i--) {
                this.keys[i] = this.keys[i - 1];
            }
            // Shift child pointers
            for (int i = Node.n; i > insertPos + 1; i--) {
                this.childPointers[i] = this.childPointers[i - 1];
            }

            this.keys[insertPos] = key;
            this.childPointers[insertPos + 1] = null;

        } else {
            float[] newKeys = new float[n + 1];
            int keysi = 0;
            for (int i = 0; i < n + 1; i++) {
                if (key < this.keys[keysi]) {
                    newKeys[i] = key;
                } else {
                    newKeys[i] = this.keys[keysi];
                }
                keysi++;
            }

            Node left = new InternalNode();
            Node right = new InternalNode();
            for (int i = 0; i < (n + 1) / 2; i++) {
                left.insertRecord(this.keys[i]);
            }
            for (int i = (n + 1) / 2 + 1; i < n + 1; i++) {
                right.insertRecord(this.keys[i]);
            }

            if (this.parent == null) {
                this.parent = new InternalNode();
                this.parent.insertRecord(this.keys[(n + 1) / 2]);
                Node[] parentChildPointers = parent.getChildPointers();
                // TODO update parent child pointers
            } else {
                this.parent.insertRecord(this.keys[(n + 1) / 2]);
                Node[] parentChildPointers = parent.getChildPointers();
            }
        }
    }

    public void deleteRecord(float fg_pct_home, Disk disk) {

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
