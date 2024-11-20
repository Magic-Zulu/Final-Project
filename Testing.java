
import java.io.*;
import java.util.Scanner;

public class Testing {
    static int tableSize=1000003;
    static String words[] = new String[tableSize];
    static int[] numHashed=new int[tableSize]; //stores how many times each index has been hashed to 
    static String[] dictionary=new String[tableSize]; //stores the hash table or hashed array
    static int[] prob=new int[tableSize]; //stores the number of probes it took to hash that value
    final static int P5=933047;
    final static int P7=666461;
    final static int NP5=933020;
    final static int NP7=666449;
    
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
                //100003
                else { //otherwise check each value after that until you get an empty one
                    int hv2=100003-textToNum(words[i])%100003; //second hash function
                    if (dictionary[hv2] == null) {//no collision in the second one, insert into table there
                        dictionary[hv2] = words[i];
                        prob[hv2]=1;
                    }
                    else{
                        int p=1;
                        for (int j = 0; j < tableSize; j++) {
                            int t = (hv + j * hv2) % tableSize; //find the value after the last one checked
                            if(t<0){
                                t=t*-1;
                            }
                            p=p+1;
                            if (dictionary[t] == null) {// Break the loop after inserting the data in the table
                                dictionary[t] = words[i];
                                prob[t]=p;
                                break;
                            }
                        }
                    }
                }
            }
        }
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
        static void avrProb(){
                double a=0;
            for(int i=0;i<tableSize;i++){
                a=a+prob[i];
            }
                double average=a/tableSize;
                System.out.println("The average number of probes to each index is: "+average);
        }
        static void timeCnv(long end, long start){
            long sec=(((end-start)-(end-start)%1000))/1000;
            long min=(sec-(sec%60))/60;
            System.out.printf("Time: %d:%02d",min,sec%60);
            System.out.println();
        }
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
        static int findIn(String x,int s){
                int hv = textToNum(x) % tableSize; //hash function
                if(dictionary[hv]==null){
                    System.out.println("Input not found");
                    return -1;
                }
                else{
                if (dictionary[hv].equals(x)) {//no collision
                    return hv;
                }
                else { //otherwise check each value after that until you get an empty one
                        int hv2=100003-textToNum(x)%100003; //second hash function
                    if(dictionary[hv2]==null){
                        System.out.println("Input not found");
                        return -1;
                    }
                    else{
                        if (dictionary[hv2].equals(x)) {//no collision in the second one, its here
                            return hv2;
                        }
                        else{
                            int t=-1;
                            for (int j = 0; j < tableSize; j++) {
                                t = (hv + j * hv2) % tableSize; //find the value after the last one checked
                                if(dictionary[t]==null){
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
        Scanner text=null;
        Scanner in=new Scanner(System.in);
        try{
            text=new Scanner(new File("/Users/emmadumont/Downloads/words_a-2.txt/"));
        }
        catch(FileNotFoundException x){
            System.out.println("File not found");
        }
        int s=0;
        long startT=System.currentTimeMillis();
        while(text.hasNextLine()){
            words[s]=text.nextLine();
            s=s+1; 
        }
        long endT=System.currentTimeMillis();
        System.out.println("Number of words in the dictionary: "+s);
        System.out.println("Milliseconds taken to count the list: "+(endT-startT));
        System.out.println("Would you like to have a load factor of 0.5 or 0.7? (Please enter either 5 or 7)");
        int initialLoadFactor=in.nextInt();
        if(initialLoadFactor!=7&&initialLoadFactor!=5){
            while(initialLoadFactor!=7&&initialLoadFactor!=5){
                System.out.println("Invalid input, please enter either 5 or 7: ");
                initialLoadFactor=in.nextInt();
            }
        }
        boolean pm=true;
        System.out.println("Do you want to use a prime table size? (please enter y for yes and n for no)");
        String prime=in.next();
        if(prime.charAt(0)!='y'&&prime.charAt(0)!='n'){
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
        long startT2=System.currentTimeMillis();
        hashing(s);
        long endT2=System.currentTimeMillis();
        System.out.println("MS taken to hash the dictionary: "+(endT2-startT2));
        timeCnv(endT2, startT2);
        maxProb();
        avrProb();
        // for(int i=0;i<300;i++){
        //     System.out.println(i+"   "+dictionary[i]);
        // }
        System.out.println("How many words would you like to locate?"); //prompt user to determine how many inputs need to be taken
        int numWords=in.nextInt(); //variable to store the number of inputs to be taken
        in.nextLine(); //prevents the next code from taking a blank input
        System.out.println("Now enter your word(s): ");
        String[] findWords=new String[numWords];
        for (int i=0;i<numWords;i++){ //take inputs however many time as determined by the number the user input
            findWords[i]=in.nextLine();
        }
        int id1;
        int p2;
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
        // System.out.println("Enter a word to find its index: ");
        // String input=in.nextLine();
        // System.out.println("ascii value: "+textToNum(input));
        // int id=findIn(input,s);
        // int p=0;
        // if(id>=0){
        //     p=prob[id];
        //     System.out.println("'"+input+"' is at index "+id+" and the number of probes taken to reach it was "+p);
        // }
        text.close();
        in.close();
    }
}
