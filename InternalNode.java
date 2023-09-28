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
        // if (!isFull()) {
        //     int insertPos = 0;
        //     while (insertPos < Node.n && this.keys[insertPos] < key) {
        //         insertPos++;
        //     }

        //     // Shift right
        //     for (int i = Node.n - 1; i > insertPos; i--) {
        //         this.keys[i] = this.keys[i - 1];
        //     }
        //     // Shift child pointers
        //     for (int i = Node.n; i > insertPos + 1; i--) {
        //         this.childPointers[i] = this.childPointers[i - 1];
        //     }

        //     this.keys[insertPos] = key;
        //     this.childPointers[insertPos + 1] = null;

        // } else {
        //     float[] newKeys = new float[n + 1];
        //     int keysi = 0;
        //     for (int i = 0; i < n + 1; i++) {
        //         if (key < this.keys[keysi]) {
        //             newKeys[i] = key;
        //         } else {
        //             newKeys[i] = this.keys[keysi];
        //         }
        //         keysi++;
        //     }

        //     Node left = new InternalNode();
        //     Node right = new InternalNode();
        //     for (int i = 0; i < (n + 1) / 2; i++) {
        //         left.insertRecord(this.keys[i]);
        //     }
        //     for (int i = (n + 1) / 2 + 1; i < n + 1; i++) {
        //         right.insertRecord(this.keys[i]);
        //     }

        //     if (this.parent == null) {
        //         this.parent = new InternalNode();
        //         this.parent.insertRecord(this.keys[(n + 1) / 2]);
        //         Node[] parentChildPointers = parent.getChildPointers();
        //         // TODO update parent child pointers
        //     } else {
        //         this.parent.insertRecord(this.keys[(n + 1) / 2]);
        //         Node[] parentChildPointers = parent.getChildPointers();
        //     }
        // }

        int insertPos = 0;
        Block block = address.getBlock();
        int index = address.getIndex();
        float key = block.getRecords()[index].getFg_pct_home();
        while (insertPos < Node.n && this.keys[insertPos] <= key) {
            if(this.keys[insertPos] == key) break;
            insertPos++;
        }

        if(this.keys[insertPos] <= key) {
            this.childPointers[insertPos + 1].insertRecord(address);
        } else {
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
                    if(i+1 < Node.n) rightNode.keys[j] = this.keys[i+1];
                    this.keys[i] = Float.MAX_VALUE;
                    this.childPointers[i+1] = null;
                }
                // int i = Node.n/2;
                // int j = 0;
                // while(i<Node.n) {
                //     rightNode.childPointers[j] = this.childPointers[i+1];
                //     if(i+1 < Node.n) rightNode.keys[j] = this.keys[i+1];
                //     this.keys[i] = Float.MAX_VALUE;
                //     this.childPointers[i+1] = null;

                //     i++;
                //     j++;
                // }
                
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
                // if(insertPos == Node.n/2) { // if inserted child is at start of this new right node
                //     for(int i=Node.n/2+1, j=0; i<=Node.n; i++, j++) {
                //         if(insertPos == i) {
                //             rightNode.childPointers[j] = childNode; // copy pointer only
                //         } else {
                //             rightNode.keys[j-1] = this.keys[i-1];
                //             rightNode.childPointers[j] = this.childPointers[i];                            
                //             this.keys[i-1] = Float.MAX_VALUE;
                //             this.childPointers[i] = null;
                //         }
                //     }
                // } else { // else if inserted child is somewhere in the middle of the new right node, need copy both pointer and key
                //     for(int i=Node.n/2+1, j=0; i<Node.n; i++, j++) {
                //         if(insertPos == i) {
                //             rightNode.childPointers[j] = childNode;
                //             rightNode.keys[j] = key;
                //             i--;
                //         } else {
                //             rightNode.childPointers[j] = this.childPointers[i+1];                
                //             if(i+1 < Node.n) rightNode.keys[j] = this.keys[i+1];
                //             this.keys[i] = Float.MAX_VALUE;
                //             this.childPointers[i+1] = null;
                //         }
                //     }
                // }

                boolean inserted = false;
                for(int i=Node.n/2+1, j=0; i<=Node.n; i++, j++) {
                    System.out.println("Value of j: " + j + "Value of i: " + i);
                    if(insertPos == Node.n/2+1) { // if inserted child is at start of this new right node
                        if(insertPos == i) {
                            rightNode.childPointers[j] = childNode; // copy pointer only
                        } else {
                            rightNode.keys[j-1] = this.keys[i-1];
                            rightNode.childPointers[j] = this.childPointers[i];                            
                            this.keys[i-1] = Float.MAX_VALUE;
                            this.childPointers[i] = null;
                        }
                    } else{ // else if inserted child is somewhere in the middle of the new right node, need copy both pointer and key
                        if(insertPos == i && !inserted) {
                            if(i==Node.n) {
                                rightNode.childPointers[j] = childNode;
                            } else {
                                rightNode.childPointers[j] = childNode;
                                rightNode.keys[j] = key;
                                i--;
                                inserted = true;
                            }                            
                        } else {
                            if(i+1 == Node.n) break;
                            rightNode.childPointers[j] = this.childPointers[i+1];                
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
            }

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
        
        this.childPointers[0].enumerateNodes();


        // for(int i=0; i<Node.n; i++) {
        //     if(this.keys[i] == Float.MAX_VALUE) break;
        //     System.out.print(" " + this.keys[i]);
        // }
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
