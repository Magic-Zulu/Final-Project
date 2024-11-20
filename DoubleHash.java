
import java.io.*;
import java.util.Scanner;

public class DoubleHash {
    static int tableSize=1000003;
    static String words[] = new String[tableSize];
    static int[] numHashed=new int[tableSize]; //stores how many times each index has been hashed to 
    static String[] dictionary=new String[tableSize]; //stores the hash table or hashed array
    static int[] prob=new int[tableSize]; //stores the number of probes it took to hash that value
    final static int P5=933047; //prime tablesize with a 0.5 load factor
    final static int P7=666461; //prime tablesize with a 0.7 load factor
    final static int NP5=933020; //nonprime tablesize with a 0.5 load factor
    final static int NP7=666449; //nonprime tablesize with a 0.7 load factor
    
        /* This calculates the ascii number value of each string */
        static int textToNum(String x){
            int n=0;
            if (x==null){
                System.out.println("Code attempted to perform textToNum on a null value");
                return -1;
            }
            else {
                for (int i=0;i<x.length();i++){
                    n = n + x.charAt(i);
                }
                return n;
            }
        }
    
        /* This inserts each string from 'words' into the hash table */
        static void hashing(int s){
            for (int i = 0; i < s; i++) {
                int hv = textToNum(words[i]) % tableSize; //hash function
                numHashed[hv]=numHashed[hv]+1; //updates the count of how many times this value has been hashed to
                if (dictionary[hv] == null) {//no collision, insert into table there
                    prob[hv]=0;
                    dictionary[hv] = words[i];
                }
                else { //otherwise use the second hash function
                    int hv2=100003-textToNum(words[i])%100003; //second hash function
                    if (dictionary[hv2] == null) {//no collision in the second one, insert into table there
                        dictionary[hv2] = words[i];
                        prob[hv2]=1;
                    }
                    else{
                        int p=1;
                        for (int j = 0; j < tableSize; j++) {
                            int t = (hv + j * hv2) % tableSize; //find the value after the last one checked using both hash values
                            if(t<0){
                                t=t*-1;
                            }
                            p=p+1;
                            if (dictionary[t] == null) {// Break the loop after inserting the data in the table
                                dictionary[t] = words[i];
                                prob[t]=p; //store a count of how many probes it took to hash this data
                                break;
                            }
                        }
                    }
                }
            }
        }
        /* Find the data which took the greatest number of probes to hash */
        static void maxProb(){
            int m=0;
            int in=0;
            for(int i=0;i<tableSize;i++){
                if (prob[i]>m){
                    m=prob[i];
                    in=i;
                }
            }
            System.out.println("Index "+in+" has the max number of probes needed to hash it at "+m);
        }
        /* Find the average number of probes it took to hash each bit of data */
        static void avrProb(){
                double a=0;
            for(int i=0;i<tableSize;i++){
                a=a+prob[i];
            }
                double average=a/tableSize;
                System.out.println("The average number of probes to each index is: "+average);
        }
        /* Converts time from milliseconds to a minutes:seconds format (it was more useful during testing)*/
        static void timeCnv(long end, long start){
            long sec=(((end-start)-(end-start)%1000))/1000;
            long min=(sec-(sec%60))/60;
            System.out.printf("Time: %d:%02d",min,sec%60);
            System.out.println();
        }
        /* Sets the size of the table based on the user's input */
        static void setTableSize(int ilf, boolean prime){
            if(prime){ 
                switch(ilf){
                    case 5:
                        tableSize=P5;
                        break;
                    case 7:
                        tableSize=P7;
                        break;
                }
                System.out.println("A prime tableSize with 0."+ilf+" load factor: "+tableSize);
            }
            else{
                switch(ilf){
                    case 5:
                        tableSize=NP5;
                        break;
                    case 7:
                        tableSize=NP7;
                        break;
                }
                System.out.println("A non-prime tableSize with 0."+ilf+" load factor: "+tableSize);
            }

        }
        /* Locates a value and returns its index */
        static int findIn(String x,int s){
                int hv = textToNum(x) % tableSize; //hash function
                if(dictionary[hv]==null){ //if the first hash value is null, this data defiently isn't in the table
                    System.out.println("Input not found");
                    return -1;
                }
                else{
                if (dictionary[hv].equals(x)) {//no collision, its in its first hash value's index
                    return hv;
                }
                else { 
                        int hv2=100003-textToNum(x)%100003; //second hash function
                    if(dictionary[hv2]==null){ //if it gets to here and there's a null value, this data isn't in the table
                        System.out.println("Input not found");
                        return -1;
                    }
                    else{
                        if (dictionary[hv2].equals(x)) {//no collision in the second one, its here
                            return hv2;
                        }
                        else{
                            int t=-1; //stores the latest index checked
                            for (int j = 0; j < tableSize; j++) {
                                t = (hv + j * hv2) % tableSize; //find the value after the last one checked
                                if(t<0){
                                    t=t*-1;
                                }
                                if(dictionary[t]==null){//if it ever comes up null, that means the data isn't in the table, there should only be collisions until you find what you are looking for if its there
                                    System.out.println("Input not found");
                                    t=-1;
                                    break;
                                }
                                if (dictionary[t].equals(x)) {// Break the loop after finding the data
                                    break;
                                }
                            }
                            return t;
                        }
                    }
                }
            }
        }

    public static void main(String[] args) {
        /* Take the text file and import it into the words array */
        Scanner text=null;
        Scanner in=new Scanner(System.in);
        try{
            text=new Scanner(new File("/Users/emmadumont/Downloads/words_a-2.txt/"));
        }
        catch(FileNotFoundException x){
            System.out.println("Please enter the file path to words_a-2.txt on your computer: ");
            String path=in.nextLine();
            try{
                text=new Scanner(new File(path));
            }
            catch(FileNotFoundException y){
                System.out.println("File not found");
            }
        }
        int s=0; //stores a count of how many words have been inserted
        long startT=System.currentTimeMillis();
        while(text.hasNextLine()){
            words[s]=text.nextLine();
            s=s+1; 
        }
        long endT=System.currentTimeMillis();
        System.out.println("Number of words in the dictionary: "+s);
        System.out.println("Milliseconds taken to count the list: "+(endT-startT));

        /* Ask the user to input the type of tablesize they want, prime or nonprime and a load factor of either 0.5 or 0.7 */
        System.out.println("Would you like to have a load factor of 0.5 or 0.7? (Please enter either 5 or 7)");
        int initialLoadFactor=in.nextInt();
        if(initialLoadFactor!=7&&initialLoadFactor!=5){ //repeat until the user enters a valid input
            while(initialLoadFactor!=7&&initialLoadFactor!=5){
                System.out.println("Invalid input, please enter either 5 or 7: ");
                initialLoadFactor=in.nextInt();
            }
        }
        boolean pm=true; //stores whether the tablesize should be prime or not
        System.out.println("Do you want to use a prime table size? (please enter y for yes and n for no)");
        String prime=in.next();
        if(prime.charAt(0)!='y'&&prime.charAt(0)!='n'){ //repeat until the user enters a valid input
            while(prime.charAt(0)!='y'&&prime.charAt(0)!='n'){
                System.out.println("Invalid input, please enter either y or n: ");
                prime=in.next();
            }
        }
        if(prime.charAt(0)=='y'){
            pm=true;
        }
        if(prime.charAt(0)=='n'){
            pm=false;
        }
        setTableSize(initialLoadFactor,pm);

        /* Hash the table */
        long startT2=System.currentTimeMillis();
        hashing(s);
        long endT2=System.currentTimeMillis();
        System.out.println("MS taken to hash the dictionary: "+(endT2-startT2));
        //timeCnv(endT2, startT2);

        /* Display the max probes and average probes */
        maxProb();
        avrProb();

        /* Get a list of words from the user to search for */
        System.out.println("How many words would you like to locate?"); //prompt user to determine how many inputs need to be taken
        int numWords=in.nextInt(); //variable to store the number of inputs to be taken
        in.nextLine(); //prevents the next code from taking a blank input
        System.out.println("Now enter your word(s): ");
        String[] findWords=new String[numWords]; //stores the user's word(s) in an array
        for (int i=0;i<numWords;i++){ //take inputs however many time as determined by the number the user input
            findWords[i]=in.nextLine();
        }

        /* Locate all the user's words, or word, and output where they are */
        int id1; //stores the index of the word
        int p2; //stores the number of probes to that word
        long startTFind=System.currentTimeMillis();
        for (int i=0;i<numWords;i++){
            id1=findIn(findWords[i],s);
            if(id1>=0){
                p2=prob[id1];
                System.out.println("'"+findWords[i]+"' is at index "+id1+" and the number of probes taken to reach it was "+p2);
            }
        }
        long endTFind=System.currentTimeMillis();
        System.out.println("MS taken to find all the words: "+(endTFind-startTFind));
    
        /* Close scanners */
        text.close();
        in.close();
    }
}
