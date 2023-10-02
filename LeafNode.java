// child class of node, has max of n pointers pointing to actual records, and 1 pointer pointing to next leaf node
// has minimum of floor((n+1)/2) keys

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeafNode extends Node {
    // Has a total of n+1 pointers
    // private Address[] dataPointers; // 4 bytes * n, pointers to actual records stored in blocks
    private List<Address>[] dataPointers; // 4 bytes * n, pointers to ArrayList of references to actual records stored in blocks
    private Node nextLeafNode; // pointer to the neighboring leaf node

    public LeafNode() {
        super();
        dataPointers = new List[n];
        Arrays.fill(dataPointers, new ArrayList<Address>());
        nextLeafNode = null;
    }

    public Node getNextLeafNode() {
        return nextLeafNode;
    }

    public void setNextLeafNode(Node nextLeafNode) {
        this.nextLeafNode = nextLeafNode;
    }

    // public Address[] getDataPointers() {
    //     return dataPointers;
    // }

    public List<Address>[] getDataPointers() {
        return dataPointers;
    }

    public void insertRecord(Address address) {
        // IMPLEMENTATION
        Block block = address.getBlock();
        int index = address.getIndex();
        float key = block.getRecords()[index].getFg_pct_home();
        if(!isFull() || containsKey(key)) {
            int insertPos = 0;
            while (insertPos < Node.n && this.keys[insertPos] <= key) {
                if(this.keys[insertPos] == key) break;
                insertPos++;
            }
            
            // if there is duplicate key, insert into linked list
            if(this.keys[insertPos] == key) {
                this.dataPointers[insertPos].add(address);
            } else {
                // Shift keys and pointers right if there is a need to.
                // For future improvement, maybe can stop shifting right once we hit the FLOAT MAX VALUE
                if(this.keys[insertPos] != Float.MAX_VALUE) {
                    for (int i = Node.n - 1; i > insertPos; i--) {
                        this.keys[i] = this.keys[i - 1];
                        this.dataPointers[i] = this.dataPointers[i - 1];
                    }
                }

                // inserting of new key and updating data pointer
                this.keys[insertPos] = key;
                this.dataPointers[insertPos] = new ArrayList<Address>();
                this.dataPointers[insertPos].add(address);
            }
            // System.out.println("Inserted key into non full tree: " + key);


        } else { // leaf node is full, need to split
            int insertPos = 0;
            while (insertPos < Node.n && this.keys[insertPos] <= key) {
                if(this.keys[insertPos] == key) break;
                insertPos++;
            }

            LeafNode rightChild = new LeafNode();
            if(this.nextLeafNode != null) rightChild.setNextLeafNode(this.nextLeafNode); // inserting the new node in between current node and curent nextLeafNode
            this.setNextLeafNode(rightChild);

            // distributing the keys from this node to the new rightChild node
            for(int i=(Node.n+1)/2, j=0; i<Node.n; i++, j++) {
                rightChild.keys[j] = this.keys[i];
                rightChild.dataPointers[j] = this.dataPointers[i];                
                this.keys[i] = Float.MAX_VALUE;
                this.dataPointers[i] = new ArrayList<Address>();
            }

            
            // NEED TO INSERT THE NEW KEY ALSO AFTER DISTRIBUTING
            if(insertPos < (Node.n+1)/2) { // will be inserted into left node
                this.insertRecord(address);
            } else { // will be inserted into right node
                rightChild.insertRecord(address);
            }

            if(this.getParent() == null) {
                InternalNode parentNode = new InternalNode();
                this.setParent(parentNode);
                rightChild.setParent(parentNode);
                parentNode.getChildPointers()[0] = this;
                parentNode.getChildPointers()[1] = rightChild;
                parentNode.getKeys()[0] = rightChild.getSubtreeLB();
            } else {
                this.getParent().insertChild(rightChild);
                rightChild.setParent(this.getParent());
            }

            // System.out.println("Inserted key into full tree: " + key);
            // System.out.println("This right child parent is: " + rightChild.getParent());
        }
    }

    public void enumerateNodes() {
        System.out.print("Keys in this leaf node are:");
        for(int i=0; i<Node.n; i++) {
            if(this.keys[i] == Float.MAX_VALUE) break;
            System.out.print(" " + this.keys[i]);
        }
        System.out.println();
        System.out.println("The parent is: " + this.getParent());

        if(this.getNextLeafNode() != null) {
            this.getNextLeafNode().enumerateNodes();
        }
    }

    public void deleteRecord(float key, Disk disk) {
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

    // for leaf node, there is no subtree, so the lower bound exists in this node as the first key
    public float getSubtreeLB() {
        return this.keys[0];
    }

    public boolean containsKey(float key) {
        for(float k : this.keys) {
            if(key == k) return true;
        }

        return false;
    }
}
