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
                for(int i=Node.n/2-1, j=0; i<Node.n; i++, j++) { 
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
                for(int i=Node.n/2, j=0; i<=Node.n; i++, j++) {
                    // System.out.println("Value of j: " + j + "Value of i: " + i);
                    if(insertPos == Node.n/2) { // if inserted child is at start of this new right node
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
        
        System.out.println("Number of keys in this level: " + getNumKeys());
        this.childPointers[0].enumerateNodes();



    }

    public boolean deleteRecord(float fg_pct_home, Disk disk, Node leftSibling, Node rightSibling) {
        boolean deleted=false;
        InternalNode leftInternalNode = (InternalNode) leftSibling;
        InternalNode rightInternalNode = (InternalNode) rightSibling;

        int deletePos = 0;
        while(deletePos < Node.n-1 && this.keys[deletePos+1] <= fg_pct_home) {
            deletePos++;
        }

        if(fg_pct_home < this.keys[deletePos]) { // means the record is in the pointer on the left of this key
            Node childLeftSibling;
            if(deletePos - 1 >= 0){ // checking if there is a left sibling for the child
                childLeftSibling = this.childPointers[deletePos - 1];
            } else {
                if(leftInternalNode != null) {
                    int lastChildPos = 0;
                    while(lastChildPos < Node.n && leftInternalNode.getKeys()[lastChildPos] != Float.MAX_VALUE) {
                        lastChildPos++;
                    }
                    childLeftSibling = leftInternalNode.getChildPointers()[lastChildPos];
                } else {
                    childLeftSibling = null;
                }
            }
            Node childRightSibling = this.childPointers[deletePos + 1];
            deleted = this.childPointers[deletePos].deleteRecord(fg_pct_home, disk, childLeftSibling, childRightSibling);
        
        } else { // means the record is in the pointer on the right of this key
            Node childRightSibling;
            if(deletePos + 2 > Node.n){ // checking if there is a right sibling for the child
                if(rightInternalNode != null) {
                    childRightSibling = rightInternalNode.getChildPointers()[0];
                } else {
                    childRightSibling = null;
                }
            } else {
                childRightSibling = this.childPointers[deletePos + 2];
            }
            Node childLeftSibling = this.childPointers[deletePos];
            deleted = this.childPointers[deletePos + 1].deleteRecord(fg_pct_home, disk, childLeftSibling, childRightSibling);
        }

        if(leftInternalNode != null) leftInternalNode.updateKeys();
        updateKeys();
        if(rightInternalNode != null) rightInternalNode.updateKeys();

        if(leftInternalNode != null && leftInternalNode.updateChildren(leftInternalNode, rightInternalNode)) { // check if the child deleted belongs to left sibling
            // System.out.println("Left Parent Internal Node updated");
        } else if(updateChildren(leftInternalNode, rightInternalNode)) { // or child deleted belongs to this node
            // System.out.println("This Parent internal node updated");
        } else if(rightInternalNode != null){ // else child deleted belongs to right sibling
            // if(rightInternalNode.updateChildren(leftInternalNode, rightInternalNode)) System.out.println("Right Parent internal node updated");
        }
        
        return deleted;
    }

    // return true if performed any update
    public boolean updateChildren(InternalNode leftSibling, InternalNode rightSibling) {
        // System.out.println("Parent Node updating children");
        for(int i=0; i<=Node.n; i++) {
            if(this.childPointers[i] == null) break;
            if(this.childPointers[i].getParent() == null) {
                deleteChild(i, leftSibling, rightSibling);
                return true;
            }
        }

        return false;
    }

    // note: deletePos here refers to the position of the childPointer, not the key
    public void deleteChild(int deletePos, InternalNode leftSibling, InternalNode rightSibling) {
        // System.out.println("Internal node deleting children");
        
        if(this.parent == null) { // special case for root node
            deleteAndShiftLeft(deletePos);
            // System.out.println("root node simple deleting");
        } else if(getNumKeys()-1 >= (Node.n)/2 ) { // case 1: simply delete
            deleteAndShiftLeft(deletePos);
            // System.out.println("Internal node simple deleting");

        } else { // not enough keys to maintain internal node requirement after delete
            // case 2: borrow from left or right sibiling
            if(leftSibling != null && leftSibling.getNumKeys()-1 >= (Node.n)/2) {
                // System.out.println("Internal node borrow from left sibling");
                borrowLeftSibling(deletePos, leftSibling);
            } else if(rightSibling != null && rightSibling.getNumKeys()-1 >= (Node.n)/2) {
                // System.out.println("Internal node borrow from right sibling");
                borrowRightSibling(deletePos, rightSibling);
            } else { // case 3: merge with sibling
                if(leftSibling != null) {
                    deleteAndShiftLeft(deletePos);
                    // System.out.println("Internal node merging with left sibling");
                    mergeWithLeft(leftSibling);
                } else if(rightSibling != null){
                    deleteAndShiftLeft(deletePos);
                    // System.out.println("Internal node merging with right sibling");
                    mergeWithRight(rightSibling);
                }
            }
        }
    }

    // after simple deletion, shift all keys to left to fill in the gap created from deletion
    public void deleteAndShiftLeft(int deletePos) {
        for(int i=deletePos; i<Node.n; i++) {
            if(this.childPointers[i] == null) break;
            if(deletePos == 0) { // child to delete is the in the first child pointer position
                this.childPointers[i] = this.childPointers[i+1];
                if(i==Node.n-1) break;
                this.keys[i] = this.keys[i+1];
            } else {
                this.childPointers[i] = this.childPointers[i+1];
                this.keys[i-1] = this.keys[i];
            }

        }
    }

    public void borrowLeftSibling(int deletePos, InternalNode leftSibling) {
        deleteAndShiftRight(deletePos);
        int shiftPos = 0; // which key from leftSibling to shift
        while(shiftPos < Node.n && leftSibling.getKeys()[shiftPos] != Float.MAX_VALUE) {
            shiftPos++;
        }

        this.childPointers[0] = leftSibling.getChildPointers()[shiftPos];
        leftSibling.getChildPointers()[shiftPos].setParent(this);
        leftSibling.getKeys()[shiftPos-1] = Float.MAX_VALUE;
        leftSibling.getChildPointers()[shiftPos] = null;
    }

    public void borrowRightSibling(int deletePos, InternalNode rightSibling) {
        deleteAndShiftLeft(deletePos);
        int insertPos = 0; // which key from leftSibling to shift
        while(insertPos < Node.n && this.keys[insertPos] != Float.MAX_VALUE) {
            insertPos++;
        }

        this.childPointers[insertPos+1] = rightSibling.getChildPointers()[0];
        this.keys[insertPos] = rightSibling.getChildPointers()[0].getSubtreeLB();

        rightSibling.deleteAndShiftLeft(0);
    }

    public void mergeWithLeft(InternalNode leftSibling) {
        int insertPos = 0; // which key in leftSibling to start inserting from
        while(insertPos < Node.n && leftSibling.getKeys()[insertPos] != Float.MAX_VALUE) {
            insertPos++;
        }
        leftSibling.getChildPointers()[insertPos+1] = this.childPointers[0];
        leftSibling.getKeys()[insertPos] = this.childPointers[0].getSubtreeLB();
        this.childPointers[0].setParent(leftSibling);
        for(int i=1; i<Node.n; i++) {
            leftSibling.getChildPointers()[insertPos + i + 1] = this.childPointers[i];
            leftSibling.getKeys()[insertPos + i] = this.keys[i-1];
            this.childPointers[i].setParent(leftSibling);
            if(this.keys[i] == Float.MAX_VALUE) break;
        }
        this.setParent(null);
    }

    public void mergeWithRight(InternalNode rightSibling) {
        int insertPos = 0; // which key in this current node to start inserting from
        while(insertPos < Node.n && this.keys[insertPos] != Float.MAX_VALUE) {
            insertPos++;
        }
        this.childPointers[insertPos+1] = rightSibling.getChildPointers()[0];
        this.keys[insertPos] = rightSibling.getChildPointers()[0].getSubtreeLB();
        rightSibling.getChildPointers()[0].setParent(this);
        System.out.println("insert pos is " + insertPos);
        for(int i=1; i<Node.n; i++) {
            System.out.println("value of i is " + i);
            this.keys[insertPos+i] = rightSibling.getKeys()[i-1];
            this.childPointers[insertPos + i+1] = rightSibling.getChildPointers()[i];
            rightSibling.getChildPointers()[i].setParent(this);
            if(rightSibling.getKeys()[i] == Float.MAX_VALUE) break;
        }
        
        rightSibling.setParent(null);
    }

    // when borrowing from left sibling after deletion, need to shift all keys right to create a spot at the start for left sibling key
    public void deleteAndShiftRight(int deletePos) {
        for(int i=deletePos; i>0; i--) {
            if(i==1) {
                this.childPointers[i] = this.childPointers[i-1];
                this.keys[i-1] = this.childPointers[i-1].getSubtreeLB();
                break;
            }
            this.childPointers[i] = this.childPointers[i-1];
            this.keys[i-1] = this.keys[i-2];
        }
    }

    public float searchQuery(float key) {
        
        int nodeCount = 0;
        float result;
        int i = 0;
        Node node = this;
        
        // Traverse the internal nodes until a leaf node is reached
        while (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;

            i = 0;

            // Increment the number of nodes accessed
            nodeCount++;

            // Decide which child node to follow based on the target key
            while (i < Node.n && key >= node.getKeys()[i]) {
                i++;
            }
    
            // Follow the pointer to the child node
            node = internalNode.getChildPointers()[i];
        }

        result = -2; //if searchQuery returned this, did not enter leaf node
    
        // When a leaf node is reached, delegate the search to the leaf node and return the result
        if (node instanceof LeafNode) {
            nodeCount++;
            result = node.searchQuery(key);
        }

        System.out.printf("No. of index nodes accessed: %d\n", nodeCount);

        return result;
    }

    public float rangeQuery(float lowerKey, float upperKey) {
        // IMPLEMENTATION
        int nodeCount = 0;
        float result;
        int i = 0;
        Node node = this;
        
        // Traverse the internal nodes until a leaf node is reached
        while (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;
            i = 0;
            // Increment the number of nodes accessed
            nodeCount++;
            // Decide which child node to follow based on the target key
            while (i < Node.n && node.getKeys()[i] <= lowerKey) {
                i++;
            }
            // Follow the pointer to the child node
            node = internalNode.getChildPointers()[i];
        }
        
        result = -2; //if searchQuery returned this, did not enter leaf node

        // When a leaf node is reached, delegate the search to the leaf node and return the result
        if (node instanceof LeafNode) {
            nodeCount++;
            result = node.rangeQuery(lowerKey, upperKey);
        }

        System.out.printf("No. of index nodes accessed: %d\n", nodeCount);

        return result;
    }

    public float getSubtreeLB() {
        return childPointers[0].getSubtreeLB(); // lower bound should exist in the subtree pointed to by the first pointer in this node
    }

    public boolean isLeaf() {
        return false;
    }

    public int countNodes() {
        int numNodes = this.getNumKeys() + 1;
        int childPos=0;
        while(childPos <= Node.n && this.childPointers[childPos] != null) {
            numNodes += this.childPointers[childPos].countNodes();
            childPos++;
        }

        return numNodes;
    }
}
