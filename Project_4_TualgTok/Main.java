import java.util.*;
import aed.tables.ForgettingCuckooHashTable;

public class Main
{
    public static void main(String[] args)
    {
        ForgettingCuckooHashTable<Integer, String> hashTable = new ForgettingCuckooHashTable<>();
        hashTable.setSwapLogging(true);

        hashTable.put(10004, "Hello0");
        hashTable.put(10081, "Hello1");
        hashTable.put(10158, "Hello2");
        hashTable.put(10235, "Hello3");
        // hashTable.put(428761, "World");
        // hashTable.put(5782477, "this");
        // hashTable.put(-9794747, "is");
        // hashTable.put(835683, "my");
        // hashTable.put(2858, "cutie");
        // hashTable.put(46932596, "patootie");
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

        System.out.println(hashTable.getCapacity());
        System.out.println(hashTable.getSwapAverage());
        System.out.println(hashTable.getSwapVariation());
        Iterable<Integer> it = hashTable.keys();
        for (Integer key : it)
            System.out.println(hashTable.get(key));

        // getSameH();
    }

    private static void getSameH()
    {
        List<Integer> equals = new ArrayList();
        for (int i = 10000; i < 11000; i++)
            if (h0(i) == h1(i))
                equals.add(i);

        containsFour(equals);
    }

    private static void containsFour(List<Integer> list)
    {
        for (Integer i0 : list)
        {
            List<Integer> related = new ArrayList();
            for (Integer i1 : list)
                if (h0(i0) == h0(i1))
                    related.add(i1);
            if (related.size() >= 4)
                printList(related);
        }

    }

    private static void printList(List<Integer> list)
    {
        for (Integer i : list)
            System.out.print(i + ",");
        System.out.println();
    }

    private static int h0(int i)
    {
        return i % 7;
    }

    private static int h1(int i)
    {
        return 31 * i % 11;
    }
}


