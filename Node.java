import java.util.Arrays;

// parent class for all nodes, value of n is derived from N * CostOFKey + (N+1) * CostOfPointer <= Block Size
// this class contains an array of float, which represents the keys in the node
// the children classes(LeafNode, InternalNode) will have their own implementations of pointers
public abstract class Node {
    public static final int n = 32;
    public static final int MIN_N = 16;

    private int numKeys;
    public Node parent;
    private float[] keys; // 4 bytes * value of n

    public Node() {
        numKeys = 0;
        keys = new float[n];
        Arrays.fill(keys, Float.MAX_VALUE); // initiate keys with max value of float
    }

    public float[] getKeys() {
        return keys;
    }

    public abstract void insertRecord(float key);
    public abstract void deleteRecord(float key); // delete records with "FG_PCT_home" below 0.35 inclusively
    public abstract float searchQuery(float key); // search for records with "FG_PCT_home" equal to certain value and return the average "FG3_PCT_home" of those records
    public abstract float rangeQuery(float lowerKey, float upperKey); // search for records with "FG_PCT_home" within lowerKey and upperKey, both inclusively, and return the average "FG3_PCT_home" of those records
}
