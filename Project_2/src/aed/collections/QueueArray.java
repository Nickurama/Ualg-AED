package aed.collections;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import aed.utils.TimeAnalysisUtils;

public class QueueArray<Item> implements Iterable<Item>
{
    public class QueueArrayIterator implements Iterator<Item>
    {
        int index;

        public QueueArrayIterator()
        {
            index = 0;
        }

        public boolean hasNext()
        {
            return index < size;
        }

        public Item next()
        {
            if (index == array.length)
                index = 0;
            return array[(low + index++) % array.length];
        }
    }

    private Item[] array;
    private int low;
    private int high;
    private int size;

    @SuppressWarnings("unchecked")
    public QueueArray(int size)
    {
        this.array = (Item[]) new Object[size];
        this.low = 0;
        this.high = 0;
        this.size = 0;
    }

    public QueueArray(Item[] array, int low, int high, int size)
    {
        this.array = array;
        this.low = low;
        this.high = high;
        this.size = size;
    }

    public void enqueue(Item item) throws IllegalArgumentException, OutOfMemoryError
    {
        if (item == null)
            throw new IllegalArgumentException("Null argument");
        if (size >= array.length)
            throw new OutOfMemoryError("Queue out of bounds");

        if (this.high >= array.length)
            this.high = 0;

        this.size++;
        array[this.high++] = item;
    }

    public Item dequeue()
    {
        if (size == 0)
            return null;
        Item result = array[this.low];
        array[this.low++] = null;
        if (this.low >= array.length)
            this.low = 0;
        this.size--;
        return result;
    }

    public Item peek()
    {
        if (size == 0)
            return null;
        return array[this.low];
    }

    public boolean isEmpty()
    {
        if (size == 0)
            return true;
        return false;
    }

    public int size()
    {
        // int result = this.high - this.low;
        // if (this.high < this.low)
        //     result += array.length;
        return size;
    }

    @SuppressWarnings("unchecked")
    public QueueArray<Item> shallowCopy()
    {
        Item[] arrayCopy = (Item[]) new Object[this.array.length];
        if (high > low)
            System.arraycopy(this.array, this.low, arrayCopy, this.low, this.size);
        else if (high <= low && size != 0)
        {
            System.arraycopy(this.array, this.low, arrayCopy, this.low, this.array.length - this.low);
            System.arraycopy(this.array, 0, arrayCopy, 0, this.high);
        }
        QueueArray<Item> result = new QueueArray<Item>(arrayCopy, this.low, this.high, this.size);
        return result;
    }

    public Iterator<Item> iterator()
    {
        return new QueueArrayIterator();
    }

    public void print()
    {
        for (Item i : this)
            System.out.println(i);
    }

    public static void main(String[] args)
    {
        Function<Integer, QueueArray<Integer>> exampleGenerator = QueueArray::generateExample;
        Consumer<QueueArray<Integer>> enqueue = QueueArray::enqueueFunction;
        Consumer<QueueArray<Integer>> dequeue = QueueArray::enqueueFunction;

        TimeAnalysisUtils.runDoublingRatioTest(exampleGenerator, enqueue, 16);
        TimeAnalysisUtils.runDoublingRatioTest(exampleGenerator, dequeue, 16);
    }

    private static QueueArray<Integer> generateExample(Integer size)
    {
        Random r = new Random();
        QueueArray<Integer> generatedQueue = new QueueArray<Integer>(size);
        for (int i = 0; i < size - 1; i++) // has space for one last value to be added
            generatedQueue.enqueue(r.nextInt());
        return generatedQueue;
    }

    private static void enqueueFunction(QueueArray<Integer> queue)
    {
        Random r = new Random();
        queue.enqueue(r.nextInt());
    }

    private static void dequeueFunction(QueueArray<Integer> queue)
    {
        queue.dequeue();
    }
}
