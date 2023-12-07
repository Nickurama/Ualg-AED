import java.util.*;
import aed.tables.ForgettingCuckooHashTable;

public class Main
{
    public static void main(String[] args)
    {
        ForgettingCuckooHashTable<Integer, String> hashTable = new ForgettingCuckooHashTable<>();
        hashTable.setSwapLogging(true);
        hashTable.put(2, "Hello");
        hashTable.put(428761, "World");
        hashTable.put(5782477, "this");
        hashTable.put(-9794747, "is");
        hashTable.put(835683, "my");
        hashTable.put(2858, "cutie");
        hashTable.put(46932596, "patootie");
        // hashTable.put(3, "hash");
        // hashTable.put(4, "table");
        // hashTable.put(5, "OwO");
        // hashTable.put(6, "1");
        // hashTable.put(7, "2");
        // hashTable.put(8, "3");
        // hashTable.put(9, "4");
        // hashTable.put(10, "4");
        // hashTable.put(11, "4");
        // hashTable.put(12, "4");
        // hashTable.put(13, "4");

        System.out.println(hashTable.getSwapAverage());
        System.out.println(hashTable.getSwapVariation());
        Iterable<Integer> it = hashTable.keys();
        for (Integer key : it)
            System.out.println(hashTable.get(key));
    }
}


