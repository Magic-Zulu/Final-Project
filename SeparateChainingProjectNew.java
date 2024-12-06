import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;

public class SeparateChainingProjectNew {

    public static void main(String[] args) {

        List<String> words = new ArrayList<>();

        try {
            File file = new File("words-3.txt");
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                words.add(scanner.next());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error, file not found. " + e.getMessage());
            return;
        }

        int n = words.size();
        int tableSize = 2 * n;

        System.out.println("Number of words: " + n);
        System.out.println("Size of hash table: " + tableSize);

        List<String>[] hashTable = new ArrayList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }

        int[] frequency = new int[tableSize];

        for (String word : words) {
            int hashValue = Math.abs(word.hashCode() % tableSize);
            hashTable[hashValue].add(word);
            frequency[hashValue]++;
        }

        int totalProbes = 0;
        long totalSearchTime = 0;

        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Enter words to search for (space separated): ");
        String searchInput = inputScanner.nextLine();
        String[] searchWords = searchInput.split("\\s+");

        System.out.println("\nHash table positions with their frequencies for searched words: ");
        Set<String> searchedWords = new HashSet<>();
        for (String searchWord : searchWords) {
            int hashValue = Math.abs(searchWord.hashCode() % tableSize);
            System.out.println("Position " + hashValue + " for word '" + searchWord + "' has been hashed to "
                    + frequency[hashValue] + " times.");
            searchedWords.add(searchWord);
        }

        DecimalFormat df = new DecimalFormat("#.####");

        for (String searchWord : searchWords) {
            long startTime = System.nanoTime();
            int probes = searchForWord(searchWord, hashTable, tableSize);
            long endTime = System.nanoTime();

            double timeMs = (endTime - startTime) / 1000000.0;
            String formattedTime = df.format(timeMs);

            totalProbes += probes;
            totalSearchTime += (endTime - startTime);

            int hashValue = Math.abs(searchWord.hashCode() % tableSize);
            System.out.println("\nSearch for word: " + searchWord);
            System.out.println("Probes used: " + probes);
            System.out.println("Time taken: " + formattedTime + " ms");
            System.out.println("Position " + hashValue + " has been hashed to " + frequency[hashValue] + " times.");
        }

        double totalTimeInMS = totalSearchTime / 1000000.0;
        String formattedTotalTime = df.format(totalTimeInMS);
        System.out.println("\nTotal Results: ");
        System.out.println("Total probes used: " + totalProbes);
        System.out.println("Total time taken: " + formattedTotalTime + " ms");

        System.out.println("\nEnter number of positions to display their frequencies: ");
        int numOfWordsToDisplay = inputScanner.nextInt();
        displayWords(searchedWords, numOfWordsToDisplay, frequency, tableSize);

        inputScanner.close();
    }

    public static int searchForWord(String word, List<String>[] hashTable, int tableSize) {
        int hashValue = Math.abs(word.hashCode() % tableSize);
        List<String> chain = hashTable[hashValue];
        int probes = 0;

        for (String item : chain) {
            probes++;
            if (item.equals(word)) {
                return probes;
            }
        }
        return probes;
    }

    public static void displayWords(Set<String> searchWords, int n, int[] frequency, int tableSize) {
        String fileName = "hash_positions.csv";
        try(FileWriter writer = new FileWriter(fileName)){
            writer.append("Position, Frequency\n");

            int displayedCount = 0;
        for (int i = 0; i < tableSize && displayedCount < n; i++) {
            if (frequency[i] > 0) {
                writer.append(String.format("%d, %d\n", i, frequency[i]));
                displayedCount++;
        }
        
        
            }
            System.out.println("CSV file: " + fileName);
         } catch (IOException e) {
            System.out.println("An error occured while making CSV file: " + e.getMessage());
         }
    }
}
