package aed.sorting;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import aed.utils.TimeAnalysisUtils;

class Limits
{
    char minChar;
    char maxChar;
    int maxLength;
}


public class RecursiveStringSort extends Sort
{
    private static final Random R = new Random();
    private static final int CUTOFF_VALUE = 10;


    public static <T extends Comparable<T>> void quicksort(T[] a)
    {
        qsort(a, 0, a.length - 1);
    }

    private static <T extends Comparable<T>> void qsort(T[] a, int low, int high)
    {
        if (high <= low)
            return;
        int j = partition(a, low, high);
        qsort(a, low, j - 1);
        qsort(a, j + 1, high);
    }

    private static <T extends Comparable<T>> int partition(T[] a, int low, int high)
    {
        //partition into a[low...j-1],a[j],[aj+1...high] and return j
        //choose a random pivot
        int pivotIndex = low + R.nextInt(high + 1 - low);
        exchange(a, low, pivotIndex);
        T v = a[low];
        int i = low, j = high + 1;

        while (true)
        {
            while (less(a[++i], v))
                if (i == high)
                    break;
            while (less(v, a[--j]))
                if (j == low)
                    break;

            if (i >= j)
                break;
            exchange(a, i, j);
        }
        exchange(a, low, j);

        return j;
    }



    public static void insertionSort(List<String> a)
    {
        int n = a.size();
        for (int i = 1; i < n; i++)
        {
            for (int j = i; j > 0; j--)
            {
                if (a.get(j).compareTo(a.get(j - 1)) < 0)
                {
                    String cache = a.get(j);
                    a.set(j, a.get(j - 1));
                    a.set(j - 1, cache);
                } else
                    break;
            }
        }
    }

    public static Limits determineLimits(List<String> a, int characterIndex)
    {
        Limits limits = new Limits();
        limits.minChar = 0;
        if (!a.isEmpty() && a.get(0).length() > characterIndex)
            limits.minChar = a.get(0).charAt(characterIndex);
        limits.maxChar = 0;
        limits.maxLength = 0;

        for (String s : a)
        {
            char c = 0;
            if (characterIndex < s.length())
                c = s.charAt(characterIndex);

            if (c > limits.maxChar)
                limits.maxChar = c;
            if (c < limits.minChar)
                limits.minChar = c;
            int len = s.length();
            if (len > limits.maxLength)
                limits.maxLength = len;
        }

        return limits;
    }

    public static void sort(String[] a)
    {
        recursive_sort(Arrays.asList(a), 0);
    }


    @SuppressWarnings("unchecked")
    public static void recursive_sort(List<String> a, int depth)
    {
        if (a.size() <= CUTOFF_VALUE)
        {
            insertionSort(a);
            return;
        }

        Limits limits = determineLimits(a, depth);
        int bucketSize = limits.maxChar - limits.minChar + 1;
        ArrayList<String>[] buckets = (ArrayList<String>[]) new ArrayList[bucketSize];
        ArrayList<String> ordered = new ArrayList<>();
        for (String s : a)
        {
            if (s.length() > depth)
            {
                int index = s.charAt(depth) - limits.minChar;
                if (buckets[index] == null)
                    buckets[index] = new ArrayList<String>();
                buckets[index].add(s);
            } else
                ordered.add(s);
        }

        for (ArrayList<String> bucket : buckets)
            if (bucket != null)
                recursive_sort(bucket, depth + 1);

        int index = 0;
        for (String s : ordered)
            a.set(index++, s);
        for (ArrayList<String> bucket : buckets)
            if (bucket != null)
                for (String s : bucket)
                    a.set(index++, s);
    }

    public static void fasterSort(String[] a)
    {
        //TODO: implement
    }

    // ---------- TESTING ----------

    public static void main(String[] args)
    {
        Function<Integer, String[]> generator = RecursiveStringSort::generate;
        Function<Integer, String[]> generatorSameLen = RecursiveStringSort::generateSameLen;
        Consumer<String[]> qsortTest = RecursiveStringSort::quicksort;
        Consumer<String[]> sortTest = RecursiveStringSort::sort;

        // Medium Arrays
        //testTime("qsort", generator, 1000, qsortTest, 100);
        //testTime("recursiveSort", generator, 1000, sortTest, 100);

        // Big Arrays
        //testTime("qsort", generator, 1000000, qsortTest, 100);
        //testTime("recursiveSort", generator, 1000000, sortTest, 100);

        // Doubling Ratio Tests Average
        //TimeAnalysisUtils.runDoublingRatioTest(generator, sortTest, 16);

        // Doubling Ratio Tests Worse Case
        TimeAnalysisUtils.runDoublingRatioTest(generatorSameLen, sortTest, 16);

    }

    private static void testTime(String name, Function<Integer, String[]> generator, int complexity, Consumer<String[]> executer, int trials)
    {
        long time = TimeAnalysisUtils.getAverageCPUTime(generator, complexity, executer, trials);
        System.out.println("(n=" + complexity + ") " + name + " time: " + time + "ns | " + (time / 1000000) + "ms");
    }

    private static String[] generateSameLen(Integer size)
    {
        final char[] possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        final int len = 1000;

        String[] strings = new String[size];
        for (int i = 0; i < size; i++)
        {
            StringBuilder buffer = new StringBuilder(len);
            for (int j = 0; j < len; j++)
                buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
            strings[i] = buffer.toString();
        }
        return strings;
    }

    private static String[] generate(Integer size)
    {
        final char[] possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        final int maxLen = 1000;

        String[] strings = new String[size];
        for (int i = 0; i < size; i++)
        {
            int len = R.nextInt(maxLen);
            StringBuilder buffer = new StringBuilder(len);
            for (int j = 0; j < len; j++)
                buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
            strings[i] = buffer.toString();
        }
        return strings;
    }
}
