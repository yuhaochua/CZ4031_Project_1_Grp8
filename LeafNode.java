// child class of node, has max of n pointers pointing to actual records, and 1 pointer pointing to next leaf node
// has minimum of floor((n+1)/2) keys

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

            int nValue = Node.n;
            // distributing the keys from this node to the new rightChild node
            if(nValue % 2 == 0) {
                for(int i=(Node.n+1)/2, j=0; i<Node.n; i++, j++) {
                    rightChild.keys[j] = this.keys[i];
                    rightChild.dataPointers[j] = this.dataPointers[i];                
                    this.keys[i] = Float.MAX_VALUE;
                    this.dataPointers[i] = new ArrayList<Address>();
                }
            } else {
                // distributing the keys from this node to the new rightChild node
                // NEED TO INSERT THE NEW KEY ALSO AFTER DISTRIBUTING
                if(insertPos < (Node.n+1)/2) { // will be inserted into left node
                    for(int i=(Node.n+1)/2-1, j=0; i<Node.n; i++, j++) {
                        rightChild.keys[j] = this.keys[i];
                        rightChild.dataPointers[j] = this.dataPointers[i];                
                        this.keys[i] = Float.MAX_VALUE;
                        this.dataPointers[i] = new ArrayList<Address>();
                    }
                    this.insertRecord(address);
                } else { // will be inserted into right node
                    for(int i=(Node.n+1)/2, j=0; i<Node.n; i++, j++) {
                        rightChild.keys[j] = this.keys[i];
                        rightChild.dataPointers[j] = this.dataPointers[i];                
                        this.keys[i] = Float.MAX_VALUE;
                        this.dataPointers[i] = new ArrayList<Address>();
                    }
                    rightChild.insertRecord(address);
                }
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
    
    public boolean deleteRecord(float key, Disk disk, Node leftSibling, Node rightSibling) {
        // System.out.println("BEGIN DELETING FROM LEAF NODE");
        // System.out.println("Leaf node keys are");
        // for(int i=0; i<Node.n; i++) {
        //     if(this.keys[i] == Float.MAX_VALUE) break;
        //     System.out.print(" " + this.keys[i]);
        // }
        // System.out.println();
        // System.out.println("The parent is: " + this.getParent());
        int deletePos = 0;
        while(deletePos < Node.n-1 && this.keys[deletePos+1] <= key) {
            deletePos++;
        }
        if(deletePos == 0 && this.keys[deletePos] > key) {
            return false;
        }

        for(Address addr : this.dataPointers[deletePos]) {
            disk.deleteRecord(addr);
        }

        // case 1: simply delete
        if(getNumKeys()-1 >= (Node.n+1)/2 ) {
            deleteAndShiftLeft(deletePos);
            // System.out.println("Leaf node simple deleting at pos " + deletePos);

        } else { // not enough keys to maintain leaf node requirement after delete
            // case 2: borrow from left or right sibiling
            if(leftSibling != null && leftSibling.getNumKeys()-1 >= (Node.n+1)/2) {
                // System.out.println("Leaf node borrowing from left");
                borrowLeftSibling(deletePos, (LeafNode) leftSibling);
            } else if(rightSibling != null && rightSibling.getNumKeys()-1 >= (Node.n+1)/2) {
                // System.out.println("Leaf node borrowing from right");
                borrowRightSibling(deletePos, (LeafNode) rightSibling);
            } else { // case 3: merge with sibling
                if(leftSibling != null) {
                    deleteAndShiftLeft(deletePos);
                    // System.out.println("Leaf node merging with left");
                    mergeWithLeft((LeafNode) leftSibling);
                } else {
                    deleteAndShiftLeft(deletePos);
                    // System.out.println("Leaf node merging with right");
                    mergeWithRight((LeafNode) rightSibling);
                }
            }
        }

        return true;

    }

    // after simple deletion, shift all keys to left to fill in the gap created from deletion
    public void deleteAndShiftLeft(int deletePos) {
        if(deletePos == Node.n-1) {
            this.dataPointers[deletePos] = null;
            this.keys[deletePos] = Float.MAX_VALUE;
        }
        for(int i=deletePos; i<Node.n-1; i++) {
            if(this.keys[i] == Float.MAX_VALUE) break;
            this.dataPointers[i] = this.dataPointers[i+1];
            this.keys[i] = this.keys[i+1];
            if(i==Node.n-2) {
                this.dataPointers[i+1] = null;
                this.keys[i+1] = Float.MAX_VALUE;
            }
        }
    }

    public void borrowLeftSibling(int deletePos, LeafNode leftSibling) {
        deleteAndShiftRight(deletePos);
        int shiftPos = 0; // which key from leftSibling to shift
        while(shiftPos < Node.n && leftSibling.getKeys()[shiftPos] != Float.MAX_VALUE) {
            shiftPos++;
        }
        if(shiftPos < Node.n && leftSibling.getKeys()[shiftPos] == Float.MAX_VALUE) shiftPos--;
        if(shiftPos == Node.n) shiftPos--;

        this.dataPointers[0] = leftSibling.getDataPointers()[shiftPos];
        this.keys[0] = leftSibling.getKeys()[shiftPos];
        leftSibling.getDataPointers()[shiftPos] = null;
        leftSibling.getKeys()[shiftPos] = Float.MAX_VALUE;
    }

    public void borrowRightSibling(int deletePos, LeafNode rightSibling) {
        deleteAndShiftLeft(deletePos);
        int insertPos = 0; // which key from leftSibling to insert into
        while(insertPos < Node.n && this.keys[insertPos] != Float.MAX_VALUE) {
            insertPos++;
        }
        // if(this.keys[insertPos] == Float.MAX_VALUE) insertPos--;

        this.dataPointers[insertPos] = rightSibling.getDataPointers()[0];
        this.keys[insertPos] = rightSibling.getKeys()[0];
        // rightSibling.getDataPointers()[0] = null;
        // rightSibling.getKeys()[0] = Float.MAX_VALUE;

        rightSibling.deleteAndShiftLeft(0);
    }

    public void mergeWithLeft(LeafNode leftSibling) {
        int insertPos = 0; // which key in leftSibling to start inserting from
        while(insertPos < Node.n && leftSibling.getKeys()[insertPos] != Float.MAX_VALUE) {
            insertPos++;
        }
        for(int i=0; i<Node.n; i++) {
            if(this.keys[i] == Float.MAX_VALUE) break;
            leftSibling.getDataPointers()[insertPos + i] = this.dataPointers[i];
            leftSibling.getKeys()[insertPos + i] = this.keys[i];
        }
        leftSibling.setNextLeafNode(this.nextLeafNode);
        this.setParent(null);
    }

    public void mergeWithRight(LeafNode rightSibling) {
        int insertPos = 0; // which key in this current node to start inserting from
        while(insertPos < Node.n && this.keys[insertPos] != Float.MAX_VALUE) {
            insertPos++;
        }
        for(int i=0; i<Node.n; i++) {
            if(rightSibling.getKeys()[i] == Float.MAX_VALUE) break;
            this.dataPointers[insertPos + i] = rightSibling.getDataPointers()[i];
            this.keys[insertPos + i] = rightSibling.getKeys()[i];
        }
        this.setNextLeafNode(rightSibling.getNextLeafNode());
        rightSibling.setParent(null);
    }

    // when borrowing from left sibling after deletion, need to shift all keys right to create a spot at the start for left sibling key
    public void deleteAndShiftRight(int deletePos) {
        for(int i=deletePos; i>0; i--) {
            this.dataPointers[i] = this.dataPointers[i-1];
            this.keys[i] = this.keys[i-1];
        }
    }

    public float searchQuery(float key) {

        // float resultCount = 0;
        boolean resultFound = false;
        float resultSum = 0;
        float result;

        Block block;
        int index;
        Record record;

        List<Address> addresses;
        Set<Block> scannedBlocks = new HashSet<>();


        // Search for records with the specified key
        for (int i = 0; i < n; i++) {

            if (keys[i] != Float.MAX_VALUE && keys[i] == key) {
                resultFound = true;
                addresses = dataPointers[i];
                for (Address address : addresses) {
                    block = address.getBlock();
                    index = address.getIndex();
                    record = block.getRecords()[index];
                    scannedBlocks.add(block);
                    resultSum += record.getFg3_pct_home();                    
                }

                System.out.printf("No. of data block accesses: %d\n", (int) scannedBlocks.size());

                break;

            } else if (keys[i] > key) {
                break; // Since keys are sorted, no more matching keys will be found
            }
        }

        //key not found
        if (!resultFound) {
            return -1;
        }

        result = resultSum / scannedBlocks.size();

        return result;

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

    public boolean isLeaf() {
        return true;
    }

    public int countNodes() {
        return 0;
    }
}
