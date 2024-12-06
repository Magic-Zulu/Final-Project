# Linear Probing Hash Table in Java

This project is all about building an effective hash table in Java using linear probing for handling collisions. What is the main goal? To efficiently store, search, and track data, while also keeping an eye on how well the hash function spreads out entries.

## Overview

The project includes:

1. *Basic Hash Table Functionality*: We’re storing strings in a hash table with a set capacity, so there’s a limit to how many entries can fit. We handle collisions by moving sequentially (linear probing) until an empty spot is found.
   
2. *Statistics on Hashing and Probing*: When items are added, the hash table keeps track of:
   - *How often each slot gets hashed to*.
   - *How many probes it takes*.

3. *Search Time Tracking*: Every search operation is timed to help us see how efficient the search process is. It’s easy to compare how search times change as more items are added and the table gets fuller.

## How It Works

### Key Components

Here’s a rundown of the main methods of the code:

- *`computeHash`*: Hashing method to calculate the hash index as a string. It also tracks how often each index is used.

- *`add`*: The method adds the element into the hash table. If the table is getting too full, it warns you that rehashing might be needed soon. 

- *`find`*: This method searches for an element in the hash table. If it finds the item, it reports the index, the number of probes it took, and the time it took to search. If not, it reports that too.

- *`showStatistics`*: After filling the table, you can call this method to display probe count and distribution.

- *`showSearchTimeStatistics`*: Displays the time it took for each search operation, giving insights into the performance of the hash table over time.

### How to Use It

To get started, you’ll initialize the `Linearprobing` table with your desired size and load factor, then you can add data from a file or manually. Here’s an example:

```java
public static void main(String[] args) {
    String filePath = "/C:/users/liang/downloads/words_a-2.txt"; // Path to your text file
    int size = 61703; // Table size (just an example)
    double load = 0.7; // Load factor threshold (just an example)

    Linearprobing table = new Linearprobing(size, load); // Creating a table

    // Adding items from a file
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        table.addFile(reader);
    } catch (IOException e) {
        System.out.println("Error reading the file: " + e.getMessage());
    }

    table.show(); // View table contents

    // Show statistics
    table.showStatistics();

    // Searching for specific items
    table.find("banana");

    table.showSearchTimeStatistics(); // See search times
}