import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Linearprobing4 {
    private String[] slots; // Stores array of strings
    private int capacity; // Max number of items
    private int filled; // Current filled spots
    private double maxLoad; // Load factor
    private int[] hashDistribution; // Tracks how many times each position is hashed
    private int[] probeCounts; // Array tracks how many probes
    private long[] searchTimes; // Array stores search times
    private int searchCount; // Counter for search options

    // Constructor that initializes hashtable
    public Linearprobing4(int size, double load) {
        capacity = size;
        maxLoad = load;
        slots = new String[capacity];
        filled = 0;
        hashDistribution = new int[capacity];
        probeCounts = new int[capacity];
        searchTimes = new long[capacity]; // Assuming we'll not exceed capacity for search items
        searchCount = 0;
    }

    // Hashing method to calculate the hash index as a string
    private int computeHash(String key) {
        int hash = key.hashCode();
        int index = Math.abs(hash % capacity);
        hashDistribution[index]++; // Track how often each position is hashed to
        return index;
    }

    // Adds the element into the hash table
    public void add(String item) {
        if (filled >= capacity * maxLoad) {
            System.out.println("Rehashing required, table overflows.");
            return;
        }

        int index = computeHash(item);
        int startIdx = index;
        int attempt = 0;

        // Try to place the item using linear probing
        while (slots[index] != null) { 
            index = (index + 1) % capacity; // Index gets incremented and checks the next slot
            attempt++;
            if (index == startIdx) {
                System.out.println("No room for " + item); // Says that the table was probed
                return;
            }
        }

        slots[index] = item;
        filled++;
        probeCounts[attempt]++; // Track number of probes taken for this insertion
        System.out.println("Added " + item + " at position " + index + ", took " + attempt + " attempts.");
    }

    // Method to add a file 
    public void addFile(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            add(line.trim());
        }
    }

    // Method searches for an element in the hashtable
    public boolean find(String item) {
        int index = computeHash(item);
        int startIdx = index;
        int attempt = 0;

        long startTime = System.nanoTime(); // Start time tracking

        // Searches using linear probing
        while (slots[index] != null) { // Loop stops if slot is null
            if (slots[index].equals(item)) { 
                long endTime = System.nanoTime(); // End time tracking
                searchTimes[searchCount++] = endTime - startTime; // Store search time
                System.out.println("Found " + item + " at " + index + ", after " + attempt + " attempts. Time taken: " + (endTime - startTime) + " ns.");
                return true;
            }
            index = (index + 1) % capacity; 
            attempt++; // Keeps searching by incrementing until the table is fully probed
            if (index == startIdx) {
                break;
            }
        }

        long endTime = System.nanoTime(); // End time tracking
        searchTimes[searchCount++] = endTime - startTime; // Store search time
        System.out.println(item + " not found, searched " + attempt + " places. Time taken: " + (endTime - startTime) + " ns.");
        return false;
    }

    // Method to display hashtable
    public void show() {
        System.out.println(Arrays.toString(slots));
    }

    // Displays probe count and distribution
    public void showStatistics() {
        System.out.println("Hash Distribution:");
        for (int i = 0; i < hashDistribution.length; i++) {
            if (hashDistribution[i] > 0) {
                System.out.println("Position " + i + ": " + hashDistribution[i] + " times");
            }
        }

        System.out.println("\nProbe Count Distribution:");
        for (int i = 0; i < probeCounts.length; i++) {
            if (probeCounts[i] > 0) {
                System.out.println(i + " probes: " + probeCounts[i] + " occurrences");
            }
        }
    }

    // Method to show search time statistics
    public void showSearchTimeStatistics() {
        System.out.println("\nSearch Time Statistics:");
        for (int i = 0; i < searchCount; i++) {
            System.out.println("Search " + (i + 1) + ": " + searchTimes[i] + " ns");
        }
    }

    public static void main(String[] args) {
        String filePath = "/C:/users/liang/downloads/words_a-2.txt"; // File path
        int size = 61702; // Table size
        double load = 0.5; // Load factor

        Linearprobing4 table = new Linearprobing4(size, load); // Initializes table

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { // Reads the file
            table.addFile(reader);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        // Show table
        table.show();

        // Show hash distribution and probe statistics
        table.showStatistics();

        // Perform search operations
        table.find("org");

        // Show search time statistics
        table.showSearchTimeStatistics();
    }
}
