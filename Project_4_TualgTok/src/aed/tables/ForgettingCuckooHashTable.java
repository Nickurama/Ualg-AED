package aed.tables;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForgettingCuckooHashTable<Key, Value> implements ISymbolTable<Key, Value>
{
    private static final Random R = new Random();

    private static class Pair<Key, Value>
    {
        public Key key;
        public Value value;

        public Pair(Key k, Value v)
        {
            this.key = k;
            this.value = v;
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
                return sum / (this.swapLogSize - 1);
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
    private static final int MAX_INSERT_DEPTH = 10;

    private Pair<Key, Value>[] table0;
    private Pair<Key, Value>[] table1;
    private List<Key> keyList;
    private int primeIndex;
    private int size;

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
        return table0[hash0(k)] != null || table1[hash1(k)] != null;
    }

    public Value get(Key k)
    {
        Value result = null;
        int hash0 = hash0(k);
        int hash1 = hash1(k);
        Pair<Key, Value> p0 = this.table0[hash0];
        Pair<Key, Value> p1 = this.table1[hash1];
        if (p0 != null && p0.key.equals(k))
            result = p0.value;
        else if (p1 != null && p1.key.equals(k))
            result = p1.value;
        return result;
    }

    public void put(Key k, Value v) throws IllegalArgumentException
    {
        put(new Pair<Key, Value>(k, v));
    }

    private void put(Pair<Key, Value> p) throws IllegalArgumentException
    {
        if (p.key == null)
            throw new IllegalArgumentException("key can't be null.");
        if (generatesInfiniteLoop(p.key))
            System.out.println("infinite loop detected :3");
        // throw new IllegalArgumentException("There were 3 keys with the same hash");
        if (getLoadFactor() >= 0.5)
            resizeUp();

        if (p.value == null)
            delete(p.key);
        else
            insert(p);

    }

    private boolean generatesInfiniteLoop(Key k)
    {
        Pair<Key, Value> p0 = table0[hash0(k)];
        Pair<Key, Value> p1 = table1[hash1(k)];
        return p0 != null && p0.key != k && hash0(p0.key) == hash0(k) &&
            p1 != null && p1.key != k && hash0(p1.key) == hash0(k);
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
        if (table0[putIndex] == null)
        {
            table0[putIndex] = pair;
            this.size++;
            if (this.logger.isLogging)
                logger.log(iteration);
        } else
        {
            Pair<Key, Value> oldPair = table0[putIndex];
            if (pair.key.equals(oldPair.key))
                table0[putIndex].value = pair.value;
            else
            {
                table0[putIndex] = pair;
                insert1(oldPair, ++iteration);
            }
        }
    }

    private void insert1(Pair<Key, Value> pair, int iteration)
    {
        if (iteration >= MAX_INSERT_DEPTH)
        {
            resizeUp();
            iteration = 0;
        }

        int putIndex = hash1(pair.key);
        if (table1[putIndex] == null)
        {
            table1[putIndex] = pair;
            this.size++;
            if (this.logger.isLogging)
                logger.log(iteration);
        } else
        {
            Pair<Key, Value> oldPair = table1[putIndex];
            if (pair.key.equals(oldPair.key))
                table1[putIndex].value = pair.value;
            else
            {
                table1[putIndex] = pair;
                insert0(oldPair, ++iteration);
            }
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
        for (Pair<Key, Value> pair : oldKeyPairs)
            put(pair);
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

    public Iterable<Key> keys()
    {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<Key>, Iterable<Key>
    {
        Iterator<Key> keyListIterator;
        Key nextValue;

        KeyIterator()
        {
            keyListIterator = keyList.iterator();
            updateNextValue();
        }

        private void updateNextValue()
        {
            Key tempNext = null;
            while (keyListIterator.hasNext() && (tempNext == null || !containsKey(tempNext)))
                tempNext = keyListIterator.next();
            nextValue = tempNext;
        }

        public boolean hasNext()
        {
            return nextValue != null;
        }

        public Key next()
        {
            Key result = nextValue;
            updateNextValue();
            return result;
        }

        public void remove()
        {
            delete(nextValue);
            updateNextValue();
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
        // TODO: implement
    }

    public static void main(String[] args)
    {
        // testing hashing function
        ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<>();
        hashTable.setSwapLogging(true);
        fillTable(hashTable, 100000);
        printStatistics(hashTable);
        System.out.println("Finished.");
    }

    private static void fillTable(ForgettingCuckooHashTable<Integer, Integer> hashTable, int size)
    {
        for (int i = 0; i < size; i++)
            hashTable.put(R.nextInt(), R.nextInt());
    }

    private static void printStatistics(ForgettingCuckooHashTable<Integer, Integer> hashTable)
    {
        System.out.println(hashTable.getSwapAverage());
        System.out.println(hashTable.getSwapVariation());
    }
}
