import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static Disk disk = new Disk();
    private static int numRecords = 0;
    private static BPlusTree bplustree = new BPlusTree();
    public static void main(String[] arg) {
        // Define the file path
        String filePath = "games.txt";

        try {
            // Node leafNode = new LeafNode();
            // Node internalNode = new InternalNode();

            // System.out.println("This node is a " + leafNode.getClass());
            // System.out.println("This node is a " + internalNode.getClass());
            // if(leafNode.getClass().toString().equals("class LeafNode")) {
            //     System.out.println("TRUE");
            // }

            Scanner scanner = new Scanner(new File(filePath));
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // Split the line into individual values using tab as the delimiter
                String[] values = line.split("\t");

                if (!values[0].equals("") && !values[1].equals("") && !values[2].equals("") && !values[3].equals("") && !values[4].equals("") && !values[5].equals("") && !values[6].equals("") && !values[7].equals("") && !values[8].equals("")) {
                    // Extract and store individual values in variables
                    String dateStr = values[0].replace("/", ""); // Remove slashes
                    int date = Integer.parseInt(dateStr);
                    int team_id_home = Integer.parseInt(values[1]);
                    short pts_home = (short)Integer.parseInt(values[2]);
                    float fg_pct_home = Float.parseFloat(values[3]);
                    float ft_pct_home = Float.parseFloat(values[4]);
                    float fg3_pct_home = Float.parseFloat(values[5]);
                    byte ast_home = (byte)Integer.parseInt(values[6]);
                    byte reb_home = (byte)Integer.parseInt(values[7]);
                    byte home_team_wins = (byte)Integer.parseInt(values[8]);

                    Record newRecord = new Record(date, team_id_home, pts_home, fg_pct_home, ft_pct_home, fg3_pct_home, ast_home, reb_home, home_team_wins);
                    Address address = disk.insertRecord(newRecord);
                    // Block block = address.getBlock();
                    // int index = address.getIndex();
                    // float key = block.getRecords()[index].getFg_pct_home();
                    // System.out.println("inserting into bplus tree: " + key);
                    bplustree.insertRecord(address);

                    numRecords++;
                }
                // System.out.println("Date: " + date);
                // System.out.println("Value 1: " + intValue1);
                // System.out.println("Value 2: " + shortValue);
                // System.out.println("Value 3: " + doubleValue1);
                // System.out.println("Value 4: " + doubleValue2);
                // System.out.println("Value 5: " + doubleValue3);
                // System.out.println("Value 6: " + byteValue1);
                // System.out.println("Value 7: " + byteValue2);
                // System.out.println("Value 8: " + byteValue3);
            }

            // Close the scanner
            scanner.close();
            
            // Object objectToMeasure = new Record(1,1,(short)1,(float)0.1,(float)0.1,(float)0.1,(byte)2,(byte)2,(byte)1);
            // long size = calculator.getObjectSize(calculator);

            System.out.println("Number of records: " + numRecords);
            // System.out.println("Size of a record in java : " + size);
            System.out.println("Size of a record: " + Record.RECORD_SIZE);
            System.out.println("Number of records stored in a block: " + Block.MAX_NUM_RECORDS);
            System.out.println("Number of blocks for storing data: " + disk.getNumBlocks());
            System.out.println("Enumerating bplus tree");
            bplustree.enumerateNodes();

        } catch (FileNotFoundException | NumberFormatException e) {
            System.err.println("File not found: " + filePath);

            System.out.println(e.getMessage());
            System.out.println("record number: " + numRecords);
        }
        

    }

    public void experiment2() {

    }

}
