package aed.tables;

import java.util.*;

public class ForgettingCuckooHashTable<Key, Value> implements ISymbolTable<Key, Value>
{

    private static class Pair<Key, Value>
    {
        public Key key;
        public Value value;
        public int time;

        public Pair(Key k, Value v, int currentTime)
        {
            this.key = k;
            this.value = v;
            this.time = currentTime;
        }
    }

    private class Logger
    {
        private static final int LOG_SIZE = 100;
        private boolean isLogging;
        private int[] swapLog;
        private int swapLogSize;
        private int swapLogIndex;

        public Logger()
        {
            this.isLogging = false;
            this.swapLog = new int[LOG_SIZE];
            this.swapLogSize = 0;
            this.swapLogIndex = 0;
        }

        public void log(int i)
        {
            swapLog[swapLogIndex++] = i;
            if (this.swapLogIndex >= LOG_SIZE)
                this.swapLogIndex -= LOG_SIZE;
            if (swapLogSize < LOG_SIZE)
                swapLogSize++;
        }

        public float getAverage()
        {
            int avg = 0;
            for (int i = 0; i < this.swapLogSize; i++)
                avg += swapLog[i];
            if (this.swapLogSize != 0)
                return (float) avg / (float) this.swapLogSize;
            return 0;
        }

        public float getVariance()
        {
            float sum = 0;
            float avg = getAverage();
            for (int i = 0; i < this.swapLogSize; i++)
                sum += (float) Math.pow(swapLog[i] - avg, 2);
            if (this.swapLogSize != 0)
                return sum / (this.swapLogSize); // -1
            return 0;
        }
    }

    private static int[] primesTable0 =
        {
                7, 17, 37, 79, 163, 331,
                673, 1361, 2729, 5471, 10949,
                21911, 43853, 87719, 175447, 350899,
                701819, 1403641, 2807303, 5614657,
                11229331, 22458671, 44917381, 89834777, 179669557
        };

    private static int[] primesTable1 =
        {
                11, 19, 41, 83, 167, 337,
                677, 1367, 2731, 5477, 10957,
                21929, 43867, 87721, 175453, 350941,
                701837, 1403651, 2807323, 5614673,
                11229341, 22458677, 44917399, 89834821, 179669563
        };

    // worst case scenario for size >= MAX_INSERT_DEPTH is the table looping MAX_INSERT_DEPTH times
    private static final int MAX_INSERT_DEPTH = 100;
    private static final int FORGET_TIME_LIMIT = 24;
    private static final float RESIZE_THRESHOLD = 0.5f;

    private Pair<Key, Value>[] table0;
    private Pair<Key, Value>[] table1;
    private List<Key> keyList;
    private int primeIndex;
    private int size;
    private int internalTimeHours;

    private Logger logger;

    @SuppressWarnings("unchecked")
    public ForgettingCuckooHashTable(int primeIndex)
    {
        this.primeIndex = primeIndex;
        this.table0 = (Pair<Key, Value>[]) new Pair[primesTable0[primeIndex]];
        this.table1 = (Pair<Key, Value>[]) new Pair[primesTable1[primeIndex]];
        this.size = 0;
        this.keyList = new LinkedList<Key>();
        this.logger = new Logger();
        this.internalTimeHours = 0;
    }

    public ForgettingCuckooHashTable()
    {
        this(0);
    }

    public int size()
    {
        return this.size;
    }

    @Override
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    public int getCapacity()
    {
        return primesTable0[primeIndex] + primesTable1[primeIndex];
    }

    public float getLoadFactor()
    {
        return (float) this.size() / (float) this.getCapacity();
    }

    public boolean containsKey(Key k)
    {
        int hash0 = hash0(k);
        int hash1 = hash1(k);
        return table0[hash0] != null && table0[hash0].key.equals(k) ||
            table1[hash1] != null && table1[hash1].key.equals(k);
    }

    public Value get(Key k)
    {
        Value result = null;
        int hash0 = hash0(k);
        int hash1 = hash1(k);
        Pair<Key, Value> p0 = this.table0[hash0];
        Pair<Key, Value> p1 = this.table1[hash1];

        if (p0 != null && p0.key.equals(k))
        {
            result = p0.value;
            this.table0[hash0].time = this.internalTimeHours;
        } else if (p1 != null && p1.key.equals(k))
        {
            result = p1.value;
            this.table1[hash1].time = this.internalTimeHours;
        }

        return result;
    }

    public void put(Key k, Value v) throws IllegalArgumentException
    {
        put(new Pair<Key, Value>(k, v, this.internalTimeHours));
    }

    private void put(Pair<Key, Value> p) throws IllegalArgumentException
    {
        if (p.key == null)
            throw new IllegalArgumentException("key can't be null.");
        if (generatesInfiniteLoop(p.key))
            throw new IllegalArgumentException("This table doesn't support 3 objects with same hash");
        if (getLoadFactor() >= RESIZE_THRESHOLD)
            resizeUp();

        if (p.value == null)
            delete(p.key);
        else if (containsKey(p.key))
            update(p.key, p.value);
        else
            insert(p);

    }

    private boolean generatesInfiniteLoop(Key k)
    {
        Pair<Key, Value> p0 = table0[hash0(k)];
        Pair<Key, Value> p1 = table1[hash1(k)];
        return p0 != null && k.hashCode() == p0.key.hashCode() && getDeltaTime(p0.time) < FORGET_TIME_LIMIT &&
            p1 != null && k.hashCode() == p1.key.hashCode() && getDeltaTime(p1.time) < FORGET_TIME_LIMIT &&
            p0.key != k && p1.key != k;
    }

    private void update(Key k, Value v)
    {
        int hash0 = hash0(k);
        int hash1 = hash1(k);
        Pair<Key, Value> p0 = table0[hash0];
        Pair<Key, Value> p1 = table1[hash1];
        if (p0 != null && p0.key.equals(k))
        {
            table0[hash0].value = v;
            table0[hash0].time = this.internalTimeHours;
        } else if (p1 != null && p1.key.equals(k))
        {
            table1[hash1].value = v;
            table1[hash1].time = this.internalTimeHours;
        }
    }

    private void insert(Pair<Key, Value> p)
    {
        insert0(p, 0);
    }

    //insertKey0 and insertKey1 could be the same function, but at the cost of minor performance
    private void insert0(Pair<Key, Value> pair, int iteration)
    {
        if (iteration >= MAX_INSERT_DEPTH)
        {
            resizeUp();
            iteration = 0;
        }
        if (iteration == 0)
            keyList.add(pair.key);

        int putIndex = hash0(pair.key);
        if (table0[putIndex] == null) // insertion
        {
            table0[putIndex] = pair;
            this.size++;
            if (this.logger.isLogging)
                logger.log(iteration);
        } else // collision (can't be update)
        {
            Pair<Key, Value> oldPair = table0[putIndex];
            table0[putIndex] = pair;
            if (getDeltaTime(oldPair.time) < FORGET_TIME_LIMIT)
                insert1(oldPair, ++iteration);
            else
                logger.log(iteration); // means a key was inserted aswell
        }
    }

    private void insert1(Pair<Key, Value> pair, int iteration)
    {
        if (iteration >= MAX_INSERT_DEPTH)
        {
            resizeUp();
            iteration = 0;
        }
        if (iteration == 0)
            keyList.add(pair.key);

        int putIndex = hash1(pair.key);
        if (table1[putIndex] == null) // insertion
        {
            table1[putIndex] = pair;
            this.size++;
            if (this.logger.isLogging)
                logger.log(iteration);
        } else // collision (can't be update)
        {
            Pair<Key, Value> oldPair = table1[putIndex];
            table1[putIndex] = pair;
            if (getDeltaTime(oldPair.time) < FORGET_TIME_LIMIT)
                insert0(oldPair, ++iteration);
            else
                logger.log(iteration); // means a key was inserted aswell
        }
    }

    private void resizeUp()
    {
        if (this.primeIndex >= primesTable0.length - 1)
            return;

        List<Pair<Key, Value>> oldKeyPairs = getAllPairs();
        this.primeIndex++;

        resetTable();
        repopulateTable(oldKeyPairs);
    }

    private List<Pair<Key, Value>> getAllPairs()
    {
        List<Pair<Key, Value>> keyPairs = new LinkedList<>();
        for (Key k : this.keyList)
        {
            int hash0 = hash0(k);
            int hash1 = hash1(k);
            Pair<Key, Value> p0 = table0[hash0];
            Pair<Key, Value> p1 = table1[hash1];
            if (p0 != null && p0.key.equals(k))
                keyPairs.add(p0);
            else if (p1 != null && p1.key.equals(k))
                keyPairs.add(p1);
        }
        return keyPairs;
    }

    @SuppressWarnings("unchecked")
    private void resetTable()
    {
        this.table0 = (Pair<Key, Value>[]) new Pair[primesTable0[primeIndex]];
        this.table1 = (Pair<Key, Value>[]) new Pair[primesTable1[primeIndex]];
        this.keyList.clear();
        this.size = 0;
    }

    private void repopulateTable(List<Pair<Key, Value>> oldKeyPairs)
    {
        boolean wasLogging = logger.isLogging;
        setSwapLogging(false);
        for (Pair<Key, Value> pair : oldKeyPairs)
            put(pair);
        setSwapLogging(wasLogging);
    }

    private int hash0(Key k)
    {
        return (k.hashCode() & 0x7fffffff) % primesTable0[primeIndex];
    }

    private int hash1(Key k)
    {
        return ((31 * k.hashCode()) & 0x7fffffff) % primesTable1[primeIndex];
    }

    public void delete(Key k)
    {
        int hash0 = hash0(k);
        int hash1 = hash1(k);
        if (table0[hash0] != null && table0[hash0].key.equals(k))
        {
            table0[hash0] = null;
            this.size--;
        } else if (table1[hash1] != null && table1[hash1].key.equals(k))
        {
            table1[hash1] = null;
            this.size--;
        }

        if (getLoadFactor() < 0.125)
            resizeDown();
    }

    private void resizeDown()
    {
        if (this.primeIndex <= 0)
            return;

        List<Pair<Key, Value>> oldKeyPairs = getAllPairs();
        this.primeIndex--;

        resetTable();
        repopulateTable(oldKeyPairs);
    }

    private int getDeltaTime(int oldTime)
    {
        return Math.abs(this.internalTimeHours - oldTime);
    }

    public Iterable<Key> keys()
    {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<Key>, Iterable<Key>
    {
        List<Key> iteratedKeys;
        Iterator<Key> keyListIterator;
        Key oldValue;
        Key nextValue;

        KeyIterator()
        {
            keyListIterator = keyList.iterator();
            this.oldValue = null;
            this.nextValue = null;
            this.iteratedKeys = new ArrayList<>();
            updateNextValue();
        }

        private void updateNextValue()
        {
            Key tempNext = null;
            while (keyListIterator.hasNext())
            {
                tempNext = keyListIterator.next();
                if (iteratedKeys.contains(tempNext))
                {
                    tempNext = null;
                    continue;
                }
                if (containsKey(tempNext))
                {
                    iteratedKeys.add(tempNext);
                    break;
                } else
                    tempNext = null;
            }
            nextValue = tempNext;
        }

        public boolean hasNext()
        {
            return nextValue != null;
        }

        public Key next()
        {
            this.oldValue = this.nextValue;
            updateNextValue();
            return this.oldValue;
        }

        public void remove()
        {
            int oldPrimeIndex = primeIndex;
            delete(this.oldValue);
            if (primeIndex != oldPrimeIndex)
                keyListIterator = keyList.iterator();
            // updateNextValue();
        }

        @Override
        public Iterator<Key> iterator()
        {
            return this;
        }
    }

    public void setSwapLogging(boolean state)
    {
        logger.isLogging = state;
    }

    public float getSwapAverage()
    {
        return logger.getAverage();
    }

    public float getSwapVariation()
    {
        return logger.getVariance();
    }

    public void advanceTime(int hours)
    {
        this.internalTimeHours += hours;
    }

    public void printDeepSize()
    {
        int validSize = 0;
        int outdatedSize = 0;
        for (int i = 0; i < table0.length; i++)
            if (table0[i] != null)
                if (getDeltaTime(table0[i].time) < FORGET_TIME_LIMIT)
                    validSize++;
                else
                    outdatedSize++;

        for (int i = 0; i < table1.length; i++)
            if (table1[i] != null)
                if (getDeltaTime(table1[i].time) < FORGET_TIME_LIMIT)
                    validSize++;
                else
                    outdatedSize++;

        System.out.println("----------");
        System.out.println("valid size: " + validSize);
        System.out.println("outdated size: " + outdatedSize);
        System.out.println("real size: " + (validSize + outdatedSize));
        System.out.println("estimated size: " + this.size());
        System.out.println("----------");
    }

    // public static void main(String[] args)
    // {
    //     // testing hashing function
    //     // for (int i = 50; i <= 12000; i += 50)
    //     //     printAvgStats(i, 10000);

    //     // testing forget mechanism
    //     // for (int i = 2550; i <= 12000; i += 50)
    //     //     printAvgStatsForgetting(i, 10000, 20);

    //     // doubling ratio tests for put()
    //     // doublingRatioPut(15, 250, 1000);

    //     // doubling ratio tests for get()
    //     // doublingRatioGet(15, 250, 100000);

    //     // doubling ratio tests for put() on a linear probing hashtable
    //     // doublingRatioPutLinear(15, 250, 30);

    //     // doubling ratio tests for get() on a linear probing hashtable
    //     // doublingRatioGetLinear(15, 250, 100000);
    // }

    // private static final Random R = new Random();

    // private static void fillTable(ForgettingCuckooHashTable<Integer, Integer> hashTable, int size)
    // {
    //     for (int i = 0; i < size; i++)
    //         hashTable.put(R.nextInt(), R.nextInt());
    // }

    // private static Integer[] fillTableFromArray(ForgettingCuckooHashTable<Integer, Integer> hashTable, int size)
    // {
    //     Integer[] ints = new Integer[size];
    //     for (int i = 0; i < size; i++)
    //         ints[i] = R.nextInt();
    //     for (int i = 0; i < size; i++)
    //         hashTable.put(ints[i], R.nextInt());
    //     return ints;
    // }

    // private static void printAvgStats(int complexity, int trials)
    // {
    //     double avgAverage = 0;
    //     double avgVariance = 0;

    //     int progress = 0;
    //     final int frequency = 10;
    //     for (int i = 0; i < trials; i++)
    //     {
    //         if (((double) i / trials) * 100 - progress >= frequency)
    //         {
    //             progress += frequency;
    //             // System.out.println(progress + "% completed...");
    //         }
    //         ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<>();
    //         hashTable.setSwapLogging(true);
    //         fillTable(hashTable, complexity);
    //         avgAverage += hashTable.getSwapAverage();
    //         avgVariance += hashTable.getSwapVariation();
    //     }
    //     avgAverage /= trials;
    //     avgVariance /= trials;

    //     System.out.print(complexity + ",");
    //     System.out.print(String.format("%.2f", avgAverage) + ",");
    //     System.out.println(String.format("%.2f", avgVariance));
    //     // System.out.println("Stats for complexity of " + complexity + " with " + trials + " trials.");
    //     // System.out.println("Average: " + String.format("%.2f", avgAverage));
    //     // System.out.println("Variance: " + String.format("%.2f", avgVariance));
    // }

    // private static void printAvgStatsForgetting(int complexity, int trials, int chanceToRemember)
    // {
    //     double avgAverage = 0;
    //     double avgVariance = 0;

    //     int progress = 0;
    //     final int frequency = 10;
    //     for (int i = 0; i < trials; i++)
    //     {
    //         if (((double) i / trials) * 100 - progress >= frequency)
    //         {
    //             progress += frequency;
    //             // System.out.println(progress + "% completed...");
    //         }

    //         ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<>();
    //         hashTable.setSwapLogging(true);
    //         fillTableRemember(hashTable, complexity, chanceToRemember);
    //         avgAverage += hashTable.getSwapAverage();
    //         avgVariance += hashTable.getSwapVariation();
    //     }
    //     avgAverage /= trials;
    //     avgVariance /= trials;

    //     System.out.print(complexity + ",");
    //     System.out.print(String.format("%.2f", avgAverage) + ",");
    //     System.out.println(String.format("%.2f", avgVariance));
    //     // System.out.println("Stats for complexity of " + complexity + " with " + trials + " trials.");
    //     // System.out.println("Average: " + String.format("%.2f", avgAverage));
    //     // System.out.println("Variance: " + String.format("%.2f", avgVariance));
    // }

    // private static void fillTableRemember(ForgettingCuckooHashTable<Integer, Integer> hashTable, int size, int chanceToRemember)
    // {
    //     List<Integer> remember = new ArrayList<Integer>();

    //     for (int i = 0; i < size; i++)
    //     {
    //         if (i % 23 == 0)
    //             visitAllRememberKeys(hashTable, remember);
    //         hashTable.advanceTime(1);

    //         Integer key = R.nextInt();
    //         hashTable.put(key, R.nextInt());

    //         int chance = R.nextInt(100) + 1;
    //         if (chance <= chanceToRemember)
    //             remember.add(key);
    //     }
    // }

    // private static void visitAllRememberKeys(ForgettingCuckooHashTable<Integer, Integer> hashTable, List<Integer> remember)
    // {
    //     for (Integer key : remember)
    //         hashTable.get(key);
    // }

    // private static void doublingRatioPut(int iterations, int complexity, int trials)
    // {
    //     long timeElapsed = 0;
    //     long lastTimeElapsed = 0;
    //     for (int i = 0; i < iterations; i++)
    //     {
    //         for (int j = 0; j < trials; j++)
    //         {
    //             ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<>();
    //             fillTable(hashTable, complexity);
    //             long startTime = System.nanoTime();
    //             hashTable.put(R.nextInt(), R.nextInt());
    //             long stopTime = System.nanoTime();
    //             timeElapsed += stopTime - startTime;
    //         }
    //         timeElapsed /= trials;
    //         double doublingRatio = 0;
    //         if (lastTimeElapsed > 0)
    //             doublingRatio = (double) timeElapsed / lastTimeElapsed;
    //         lastTimeElapsed = timeElapsed;

    //         System.out.println("n: " + complexity +
    //             " | Avg time: " + String.format("%.1f", ((double) timeElapsed / 1000000)) + "ms / " + timeElapsed + "ns" +
    //             " | r: " + doublingRatio);

    //         complexity *= 2;
    //     }
    // }

    // private static void doublingRatioGet(int iterations, int complexity, int trials)
    // {
    //     long timeElapsed = 0;
    //     long lastTimeElapsed = 0;
    //     for (int i = 0; i < iterations; i++)
    //     {
    //         ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<>();
    //         Integer[] ints = fillTableFromArray(hashTable, complexity);
    //         for (int j = 0; j < trials; j++)
    //         {
    //             long startTime = System.nanoTime();
    //             hashTable.get(ints[R.nextInt(ints.length)]);
    //             long stopTime = System.nanoTime();
    //             timeElapsed += stopTime - startTime;
    //         }
    //         timeElapsed /= trials;
    //         double doublingRatio = 0;
    //         if (lastTimeElapsed > 0)
    //             doublingRatio = (double) timeElapsed / lastTimeElapsed;
    //         lastTimeElapsed = timeElapsed;

    //         System.out.println("n: " + complexity +
    //             " | Avg time: " + String.format("%.1f", ((double) timeElapsed / 1000000)) + "ms / " + timeElapsed + "ns" +
    //             " | r: " + doublingRatio);

    //         complexity *= 2;
    //     }
    // }

    // private static void doublingRatioPutLinear(int iterations, int complexity, int trials)
    // {
    //     long timeElapsed = 0;
    //     long lastTimeElapsed = 0;
    //     for (int i = 0; i < iterations; i++)
    //     {
    //         for (int j = 0; j < trials; j++)
    //         {
    //             OpenAdressingHashTable<Integer, Integer> hashTable = new OpenAdressingHashTable<>();
    //             fillTableLinear(hashTable, complexity);
    //             long startTime = System.nanoTime();
    //             hashTable.put(R.nextInt(), R.nextInt());
    //             long stopTime = System.nanoTime();
    //             timeElapsed += stopTime - startTime;
    //         }
    //         timeElapsed /= trials;
    //         double doublingRatio = 0;
    //         if (lastTimeElapsed > 0)
    //             doublingRatio = (double) timeElapsed / lastTimeElapsed;
    //         lastTimeElapsed = timeElapsed;

    //         System.out.println("n: " + complexity +
    //             " | Avg time: " + String.format("%.1f", ((double) timeElapsed / 1000000)) + "ms / " + timeElapsed + "ns" +
    //             " | r: " + doublingRatio);

    //         complexity *= 2;
    //     }
    // }

    // private static void doublingRatioGetLinear(int iterations, int complexity, int trials)
    // {
    //     long timeElapsed = 0;
    //     long lastTimeElapsed = 0;
    //     for (int i = 0; i < iterations; i++)
    //     {
    //         OpenAdressingHashTable<Integer, Integer> hashTable = new OpenAdressingHashTable<>();
    //         Integer[] ints = fillTableFromArrayLinear(hashTable, complexity);
    //         for (int j = 0; j < trials; j++)
    //         {
    //             long startTime = System.nanoTime();
    //             hashTable.get(ints[R.nextInt(ints.length)]);
    //             long stopTime = System.nanoTime();
    //             timeElapsed += stopTime - startTime;
    //         }
    //         timeElapsed /= trials;
    //         double doublingRatio = 0;
    //         if (lastTimeElapsed > 0)
    //             doublingRatio = (double) timeElapsed / lastTimeElapsed;
    //         lastTimeElapsed = timeElapsed;

    //         System.out.println("n: " + complexity +
    //             " | Avg time: " + String.format("%.1f", ((double) timeElapsed / 1000000)) + "ms / " + timeElapsed + "ns" +
    //             " | r: " + doublingRatio);

    //         complexity *= 2;
    //     }
    // }

    // private static void fillTableLinear(OpenAdressingHashTable<Integer, Integer> hashTable, int size)
    // {
    //     for (int i = 0; i < size; i++)
    //         hashTable.put(R.nextInt(), R.nextInt());
    // }

    // private static Integer[] fillTableFromArrayLinear(OpenAdressingHashTable<Integer, Integer> hashTable, int size)
    // {
    //     Integer[] ints = new Integer[size];
    //     for (int i = 0; i < size; i++)
    //         ints[i] = R.nextInt();
    //     for (int i = 0; i < size; i++)
    //         hashTable.put(ints[i], R.nextInt());
    //     return ints;
    // }

    // private static class OpenAdressingHashTable<Key, Value>
    // {
    //     private static final float RESIZE_THRESHOLD_LINEAR = 0.5f;
    //     private static int[] primes = {
    //             17, 37, 79, 163, 331, 673, 1361, 2729, 5471, 10949, 21911,
    //             43853, 87719, 175447, 350899, 701819, 1403641, 2807303,
    //             5614657, 11229331, 22458671, 44917381, 89834777, 179669557};
    //     private int m;
    //     private int primeIndex;
    //     private int size;
    //     private float loadFactor;
    //     private Key[] keys;
    //     private Value[] values;

    //     @SuppressWarnings("unchecked")
    //     private OpenAdressingHashTable(int primeIndex)
    //     {
    //         this.primeIndex = primeIndex;
    //         this.m = this.primes[primeIndex];
    //         this.size = 0;
    //         this.loadFactor = 0;
    //         this.keys = (Key[]) new Object[this.m];
    //         this.values = (Value[]) new Object[this.m];
    //     }

    //     public OpenAdressingHashTable()
    //     {
    //         this(0);
    //     }

    //     private int hash(Key k)
    //     {
    //         return (k.hashCode() & 0x7fffffff) % this.m;
    //     }

    //     public Value get(Key k)
    //     {
    //         for (int i = hash(k); this.keys[i] != null; i = (i + 1) % this.m)
    //         {
    //             //key was found, return its value
    //             if (this.keys[i].equals(k))
    //             {
    //                 return this.values[i];
    //             }
    //         }
    //         return null;
    //     }

    //     public void put(Key k, Value v)
    //     {
    //         if (this.loadFactor >= RESIZE_THRESHOLD_LINEAR)
    //         {
    //             resize(this.primeIndex + 1);
    //         }
    //         int i = hash(k);
    //         for (; this.keys[i] != null; i = (i + 1) % this.m)
    //         {
    //             //key was found, update its value
    //             if (this.keys[i].equals(k))
    //             {
    //                 this.values[i] = v;
    //                 return;
    //             }
    //         }
    //         //we've found the right insertion position, insert
    //         this.keys[i] = k;
    //         this.values[i] = v;
    //         this.size++;
    //         this.loadFactor = this.size / this.m;
    //     }

    //     private void resize(int primeIndex)
    //     {
    //         //if invalid size do not resize;
    //         if (primeIndex < 0 || primeIndex >= primes.length)
    //             return;
    //         this.primeIndex = primeIndex;
    //         OpenAdressingHashTable<Key, Value> aux =
    //             new OpenAdressingHashTable<Key, Value>(this.primeIndex);
    //         //place all existing keys in new table
    //         for (int i = 0; i < this.m; i++)
    //         {
    //             if (keys[i] != null)
    //                 aux.put(keys[i], values[i]);
    //         }
    //         this.keys = aux.keys;
    //         this.values = aux.values;
    //         this.m = aux.m;
    //         this.loadFactor = this.size / this.m;
    //     }

    //     private void delete(Key k)
    //     {
    //         int i = hash(k);
    //         while (true)
    //         {
    //             //no key to delete, return
    //             if (this.keys[i] == null)
    //                 return;
    //             //if key was found, exit the loop
    //             if (this.keys[i].equals(k))
    //                 break;
    //             i = (i + 1) % this.m;
    //         }
    //         //delete the key and value
    //         this.keys[i] = null;
    //         this.values[i] = null;
    //         this.size--;
    //         //we need to reenter any subsequent keys
    //         i = (i + 1) % this.m;
    //         while (this.keys[i] != null)
    //         {
    //             Key auxKey = this.keys[i];
    //             Value auxValue = this.values[i];
    //             //remove from previous position
    //             this.keys[i] = null;
    //             this.values[i] = null;
    //             //temporarily reduce size,
    //             //next put will increment it
    //             this.size--;
    //             //add the key and value again
    //             this.put(auxKey, auxValue);
    //             i = (i + 1) % this.m;
    //         }
    //         this.loadFactor = this.size / this.m;
    //         if (this.loadFactor < 0.125f)
    //             resize(this.primeIndex - 1);
    //     }
    // }
}
