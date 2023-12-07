import java.security.Key;
import java.util.*;
import aed.tables.ForgettingCuckooHashTable;

public class Main
{
    public static void main(String[] args)
    {
        ForgettingCuckooHashTable<Integer, String> hashTable = new ForgettingCuckooHashTable<>();
        hashTable.put(-1, "Hello");
        hashTable.put(0, "World");
        hashTable.put(1, "this");
        hashTable.put(2, "is");
        hashTable.put(3, "my");
        hashTable.put(4, "cutie");
        hashTable.put(5, "patootie");
        hashTable.put(6, "hash");
        hashTable.put(7, "table");

        for (int i = -1; i < 8; i++)
            System.out.println(hashTable.get(i));
    }
}
