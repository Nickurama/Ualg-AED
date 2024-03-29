import java.util.*;
import aed.tables.ForgettingCuckooHashTable;

public class Main
{
    public static void main(String[] args)
    {
        Random R = new Random();
        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<>();
        String[] keys = new String[500000];
        for (int i = 0; i < 500000; i++)
            keys[i] = CuckooHashTableTests.generateRandomString(15);


        for (int i = 0; i < 500000; i++)
        {
            if (i % 1000 == 0)
            {
                hashTable.advanceTime(20);
                for (int j = 0; j < 10; j++)
                    hashTable.get(keys[j]);
            }
            hashTable.put(keys[i], R.nextInt());
        }
        hashTable.advanceTime(20);
        for (int j = 0; j < 10; j++)
            hashTable.get(keys[j]);
        hashTable.advanceTime(20);


        System.out.println("Size (before deletion):");
        hashTable.printDeepSize();

        Iterable<String> it = hashTable.keys();
        Iterator<String> iter = it.iterator();
        for (int i = 0; i < 77500 && iter.hasNext(); i++)
        {
            iter.next();
            iter.remove();
        }

        System.out.println("Size (after deletion):");
        hashTable.printDeepSize();

        return;

        // second wave
        // for (int i = 0; i < 200000; i++)
        //     keys[i] = CuckooHashTableTests.generateRandomString(15);

        // for (int i = 0; i < 200000; i++)
        // {
        //     if (i % 1000 == 0)
        //     {
        //         hashTable.advanceTime(1);
        //         for (int j = 0; j < 10; j++)
        //             hashTable.get(keys[j]);
        //     }
        //     hashTable.put(keys[i], R.nextInt());
        // }



        // System.out.println("Size 2 (before deletion):");
        // hashTable.printDeepSize();

        // it = hashTable.keys();
        // iter = it.iterator();
        // for (int i = 0; i < 10000 && iter.hasNext(); i++)
        // {
        //     iter.next();
        //     iter.remove();
        // }

        // System.out.println("Size 2 (after deletion):");
        // hashTable.printDeepSize();



        // System.out.println("Size (after 1 deletion):");
        // hashTable.printDeepSize();

        // it = hashTable.keys();
        // iter = it.iterator();
        // while (iter.hasNext())
        // {
        //     iter.next();
        //     iter.remove();
        // }

        // System.out.println(strs[0] + ": " + hashTable.get(strs[0]));
        // System.out.println(strs[100] + ": " + hashTable.get(strs[100]));
        // System.out.println(strs[1000] + ": " + hashTable.get(strs[1000]));
        // System.out.println(strs[10000] + ": " + hashTable.get(strs[10000]));
        // System.out.println(strs[100000] + ": " + hashTable.get(strs[100000]));

        // System.out.println("Contains outdated key?: " + hashTable.containsKey(strs[1000]));
        // hashTable.delete(strs[1000]);

        // hashTable.put(strs[100], 100);
        // System.out.println("Capacity: " + hashTable.getCapacity());
        // System.out.println("Load: " + hashTable.getLoadFactor());
        // System.out.println("after put, " + strs[100] + ": " + hashTable.get(strs[100]));

        // System.out.println("Size: " + hashTable.size());
        // int realSize = 0;
        // for (int i = 0; i < 500000; i++)
        //     if (hashTable.get(strs[i]) != null)
        //         realSize++;
        // System.out.println("real size: " + realSize);

        // System.out.println("--------------------------");

        // String[] strs2 = new String[500000];
        // for (int i = 0; i < 500000; i++)
        //     strs2[i] = CuckooHashTableTests.generateRandomString(15);
        // for (int i = 0; i < 500000; i++)
        // {
        //     if (i % 1000 == 0)
        //     {
        //         hashTable.advanceTime(20);
        //         for (int j = 0; j < 1000; j++)
        //             hashTable.get(strs2[j]);
        //     }
        //     hashTable.put(strs2[i], R.nextInt());
        // }

        // System.out.println("Size: " + hashTable.size());
        // System.out.println(strs2[0] + ": " + hashTable.get(strs2[0]));
        // System.out.println(strs2[100] + ": " + hashTable.get(strs2[100]));
        // System.out.println(strs2[1000] + ": " + hashTable.get(strs2[1000]));
        // System.out.println(strs2[10000] + ": " + hashTable.get(strs2[10000]));
        // System.out.println(strs2[100000] + ": " + hashTable.get(strs2[100000]));

        // System.out.println("Contains outdated key?: " + hashTable.containsKey(strs2[1000]));

        // hashTable.put(strs2[100], 100);
        // System.out.println("Capacity: " + hashTable.getCapacity());
        // System.out.println("Load: " + hashTable.getLoadFactor());
        // System.out.println("after put, " + strs2[100] + ": " + hashTable.get(strs2[100]));

        // System.out.println("Size: " + hashTable.size());
        // realSize = 0;
        // for (int i = 0; i < 500000; i++)
        //     if (hashTable.containsKey(strs[i]))
        //         realSize++;
        // for (int i = 0; i < 500000; i++)
        //     if (hashTable.containsKey(strs2[i]))
        //         realSize++;
        // System.out.println("real size: " + realSize);

        // hashTable.put(new String("ABC"), 10);
        // hashTable.put("Xpto", 189);
        // hashTable.put("hello", 2746);
        // hashTable.put("102", 102);
        // hashTable.put("five", 5);

        // hashTable.delete("102");

        // Iterable<String> it = hashTable.keys();
        // for (String key : it)
        //     System.out.print(key + ",");
        // System.out.println();

        // CuckooHashTableTests.test11();


        // ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<>();

        // hashTable.put(new String("ABC"), 10);
        // hashTable.put("Xpto", 189);
        // hashTable.put("hello", 2746);
        // hashTable.put("102", 102);
        // hashTable.put("five", 5);

        // hashTable.put("ABC", -4313);
        // hashTable.put("five", -4313);

        // System.out.println(hashTable.get("ABC"));
        // System.out.println(hashTable.get("five"));

        // Iterable<String> it = hashTable.keys();
        // for (String key : it)
        //     System.out.println(hashTable.get(key));



        // Testing 3 collisions
        // ForgettingCuckooHashTable<String, String> hashTableStr = new ForgettingCuckooHashTable<>();
        // hashTableStr.put("AaAa", "str1");
        // hashTableStr.put("BBBB", "str2");
        // hashTableStr.put("AaBB", "str2");

        // Iterable<String> itStr = hashTableStr.keys();
        // for (String key : itStr)
        //     System.out.println(hashTableStr.get(key));


        // ForgettingCuckooHashTable<Integer, String> hashTable = new ForgettingCuckooHashTable<>();
        // hashTable.setSwapLogging(true);

        // hashTable.put(10004, "Hello0");
        // hashTable.advanceTime(10);
        // hashTable.put(10004, "Hello00");
        // hashTable.get(10004);
        // hashTable.advanceTime(14);
        // hashTable.put(10081, "Hello1");
        // hashTable.put(10158, "Hello2");
        // hashTable.put(10235, "Hello3");
        // // hashTable.put(428761, "World");
        // // hashTable.put(5782477, "this");
        // // hashTable.put(-9794747, "is");
        // // hashTable.put(835683, "my");
        // // hashTable.put(2858, "cutie");
        // // hashTable.put(46932596, "patootie");
        // // hashTable.put(3, "hash");
        // // hashTable.put(4, "table");
        // // hashTable.put(5, "OwO");
        // // hashTable.put(6, "1");
        // // hashTable.put(7, "2");
        // // hashTable.put(8, "3");
        // // hashTable.put(9, "4");
        // // hashTable.put(10, "4");
        // // hashTable.put(11, "4");
        // // hashTable.put(12, "4");
        // // hashTable.put(13, "4");

        // System.out.println(hashTable.getCapacity());
        // System.out.println(hashTable.getSwapAverage());
        // System.out.println(hashTable.getSwapVariation());
        // Iterable<Integer> it = hashTable.keys();
        // for (Integer key : it)
        //     System.out.println(hashTable.get(key));

        // // getSameH();

        // ForgettingCuckooHashTable<Integer, String> hashTable = new ForgettingCuckooHashTable<>();
        // hashTable.put(10004, "Hello0");
        // hashTable.put(10081, "Hello1");
        // hashTable.put(10158, "Hello2");
        // hashTable.put(10235, "Hello3");
        // hashTable.put(428761, "World");
        // hashTable.delete(428761);
        // hashTable.delete(10235);
        // hashTable.delete(10158);
        // hashTable.delete(10081);
        // hashTable.delete(10004);
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
    }

    private static void getSameH()
    {
        List<Integer> equals = new ArrayList<Integer>();
        for (int i = 10000; i < 11000; i++)
            if (h0(i) == h1(i))
                equals.add(i);

        containsFour(equals);
    }

    private static void containsFour(List<Integer> list)
    {
        for (Integer i0 : list)
        {
            List<Integer> related = new ArrayList<Integer>();
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


