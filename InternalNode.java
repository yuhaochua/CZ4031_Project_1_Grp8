// child class of node, has max of n+1 pointers to child nodes
// has minimum of floor(n/2) keys

import java.util.ArrayList;
import java.util.Arrays;

public class InternalNode extends Node {
    // Has a total of n+1 pointers
    private Node[] childPointers; // 4 bytes * (n+1), pointers to child nodes(could be internal or leaf nodes)


    public InternalNode() {
        super();
        childPointers = new Node[n+1];
        Arrays.fill(childPointers, null);
    }

    public Node[] getChildPointers() {
        return childPointers;
    }

    public void insertRecord(Address address) {
        // TODO use binary search on node to find spot to insert
        // TODO find a better way to insert node
        // TODO shift all childPointers if inserting a node in between

        int insertPos = 0;
        Block block = address.getBlock();
        int index = address.getIndex();
        float key = block.getRecords()[index].getFg_pct_home();
        while (insertPos < Node.n && this.keys[insertPos] <= key) {
            if(this.keys[insertPos] == key) break;
            insertPos++;
        }



        if(insertPos < Node.n && this.keys[insertPos] == key) {
            this.childPointers[insertPos + 1].insertRecord(address);
        } else {
            // if(insertPos > 0) System.out.println("The previous key here is " + this.keys[insertPos - 1]);
            // System.out.println("The key here is " + this.keys[insertPos]);
            // System.out.println("insert pos is " + insertPos);
            this.childPointers[insertPos].insertRecord(address);
        }
    }

    public void insertChild(Node childNode) {
        if(this.isFull()) {
            int insertPos = 0;
            float key = childNode.getSubtreeLB();
            while (insertPos < Node.n && this.keys[insertPos] <= key) {
                if(this.keys[insertPos] == key) break;
                insertPos++;
            }
            InternalNode rightNode = new InternalNode();

            // NEED TO INSERT THE NEW CHILD ALSO AFTER DISTRIBUTING
            if(insertPos <= (Node.n)/2) { // will be inserted into left node
                for(int i=Node.n/2, j=0; i<Node.n; i++, j++) { 
                    rightNode.childPointers[j] = this.childPointers[i+1];   
                    rightNode.childPointers[j].setParent(rightNode);             
                    if(i+1 < Node.n) rightNode.keys[j] = this.keys[i+1];
                    this.keys[i] = Float.MAX_VALUE;
                    this.childPointers[i+1] = null;
                }
                
                if(this.keys[insertPos] != Float.MAX_VALUE) {
                    for (int i = Node.n - 1; i > insertPos; i--) {
                        this.keys[i] = this.keys[i - 1];
                        this.childPointers[i + 1] = this.childPointers[i];
                    }
                }

                // inserting of new key and updating data pointer
                this.keys[insertPos] = key;
                this.childPointers[insertPos + 1] = childNode;
                

            } else { // will be inserted into right node
                boolean inserted = false; // check whether the new key has been inserted already
                for(int i=Node.n/2+1, j=0; i<=Node.n; i++, j++) {
                    // System.out.println("Value of j: " + j + "Value of i: " + i);
                    if(insertPos == Node.n/2+1) { // if inserted child is at start of this new right node
                        if(insertPos == i) {
                            rightNode.childPointers[j] = childNode; // copy pointer only
                            rightNode.childPointers[j].setParent(rightNode);
                        } else {
                            rightNode.keys[j-1] = this.keys[i-1];
                            rightNode.childPointers[j] = this.childPointers[i]; 
                            rightNode.childPointers[j].setParent(rightNode);                           
                            this.keys[i-1] = Float.MAX_VALUE;
                            this.childPointers[i] = null;
                        }
                    } else{ // else if inserted child is somewhere in the middle of the new right node, need copy both pointer and key
                        // System.out.println("Inserting at middle of node");
                        if(insertPos == i && !inserted) {
                            if(i==Node.n) {
                                rightNode.childPointers[j] = childNode;
                                rightNode.childPointers[j].setParent(rightNode);
                            } else {
                                rightNode.childPointers[j] = childNode;
                                rightNode.childPointers[j].setParent(rightNode);
                                rightNode.keys[j] = rightNode.keys[j-1];
                                rightNode.keys[j-1] = key;
                                i--;
                                inserted = true;
                            }                            
                        } else {
                            rightNode.childPointers[j] = this.childPointers[i+1];
                            rightNode.childPointers[j].setParent(rightNode);                
                            if(i+1 == Node.n) break;
                            if(i+1 < Node.n) rightNode.keys[j] = this.keys[i+1];
                            this.keys[i] = Float.MAX_VALUE;
                            this.childPointers[i+1] = null;
                        }
                    }
                }
            }

            if(this.getParent() == null) {
                InternalNode parentNode = new InternalNode();
                this.setParent(parentNode);
                rightNode.setParent(parentNode);
                parentNode.getChildPointers()[0] = this;
                parentNode.getChildPointers()[1] = rightNode;
                parentNode.getKeys()[0] = rightNode.getSubtreeLB();
            } else {
                this.getParent().insertChild(rightNode);
                rightNode.setParent(this.getParent());
            }

            // System.out.println("full node");
            // this.flagNullPointer();
            // if(insertPos <= (Node.n)/2) System.out.println("left node problem");
            // if(insertPos > (Node.n)/2) System.out.println("right node problem");
            // rightNode.flagNullPointer();

        } else {
            float key = childNode.getSubtreeLB();
            int insertPos = 0;
            while (insertPos < Node.n && this.keys[insertPos] < key) {
                insertPos++;
            }

            // Shift keys and pointers right if there is a need to.
            // For future improvement, maybe can stop shifting right once we hit the FLOAT MAX VALUE
            if(this.keys[insertPos] != Float.MAX_VALUE) {
                for (int i = Node.n - 1; i > insertPos; i--) {
                    this.keys[i] = this.keys[i - 1];
                    this.childPointers[i + 1] = this.childPointers[i];
                }
            }

            // inserting of new key and updating data pointer
            this.keys[insertPos] = key;
            this.childPointers[insertPos + 1] = childNode;
        }

        updateKeys();
    }

    public void flagNullPointer() {
        for(int i=0; i<Node.n; i++) {
            if(this.keys[i] != Float.MAX_VALUE && this.childPointers[i+1] == null) {
                System.out.println("GOT UNEXPECTED NULL POINTER HERE! AT: position " + i + " and key " + this.keys[i]);
                enumerateNodes();
            }
        }
    }

    // after inserting new record, the lower bound of the subtrees might change, so need to update the keys
    public void updateKeys() {
        for(int i=0; i<Node.n; i++) {
            try {
                if(this.keys[i] == Float.MAX_VALUE) break;
                this.keys[i] = this.childPointers[i+1].getSubtreeLB();
            } catch (NullPointerException e) {
                printNodeStructure();
            }
            // if(this.keys[i] == Float.MAX_VALUE) break;
            // this.keys[i] = this.childPointers[i+1].getSubtreeLB();
        }
    }

    public void printNodeStructure() {
        System.out.println("Node structure: ");
        for(int i=0; i<Node.n; i++) {
            System.out.print(" POINTER: " + this.childPointers[i]);
            System.out.print(" KEY: " + this.keys[i]);
        }
        System.out.println(" POINTER: " + this.childPointers[Node.n]);
    }

    public void enumerateNodes() {
        // System.out.print("Keys in this level are: ");
        // for(int i=0; i<Node.n; i++) {
        //     if(this.childPointers[i] == null) break;
        //     this.childPointers[i].enumerateNodes();
        // }

        // for(int i=0; i<Node.n; i++) {
        //     if(this.keys[i] == Float.MAX_VALUE) break;
        //     System.out.print(" " + this.keys[i]);
        // }
        
        this.childPointers[0].enumerateNodes();



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

    public float getSubtreeLB() {
        return childPointers[0].getSubtreeLB(); // lower bound should exist in the subtree pointed to by the first pointer in this node
    }
}
