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
        Function<Integer, String[]> generatorDuplicates = RecursiveStringSort::generateDuplicates;
        Function<Integer, String[]> generatorDuplicatesLessSymbols = RecursiveStringSort::generateDuplicatesLessSymbols;
        Function<Integer, String[]> generatorDuplicatesHalfLen = RecursiveStringSort::generateDuplicatesHalfLen;
        Function<Integer, String[]> generatorLessSymbols = RecursiveStringSort::generateLessSymbols;
        Consumer<String[]> qsortTest = RecursiveStringSort::quicksort;
        Consumer<String[]> sortTest = RecursiveStringSort::sort;

        // Medium Arrays
        //testTime("qsort", generator, 1000, qsortTest, 100);
        //testTime("recursiveSort", generator, 1000, sortTest, 100);

        // Big Arrays
        //testTime("qsort", generator, 1000000, qsortTest, 100);
        //testTime("recursiveSort", generator, 1000000, sortTest, 100);

        // Doubling Ratio Tests Average
        //System.out.println("Average case tests");
        //doublingRatioOfAverage(generator, sortTest, 16, 100);
        //TimeAnalysisUtils.runDoublingRatioTest(generator, sortTest, 16);

        // Doubling Ratio Tests Worse Case
        //TimeAnalysisUtils.runDoublingRatioTest(generatorSameLen, sortTest, 16);
        //TimeAnalysisUtils.runDoublingRatioTest(generatorDuplicates, sortTest, 16);
        //System.out.println("Worst case tests");
        //doublingRatioOfAverage(generatorDuplicates, sortTest, 16, 100);
        //System.out.println("Worst case tests half len");
        //doublingRatioOfAverage(generatorDuplicatesHalfLen, sortTest, 16, 100);

        // Memory Tests
        doublingRatioForMemory(generator, sortTest, 16, 100);
    }

    //spacial tests

    private static void doublingRatioForMemory(Function<Integer, String[]> generator, Consumer<String[]> function, int iterations, int trials)
    {
        int currentComplexity = 125;
        long lastMemory = 0;
        for (int i = 0; i < iterations; i++)
        {
            currentComplexity *= 2;
            long currentAverageMemory = getMemoryUsed(generator, function, currentComplexity, trials);
            double doublingRatio = 0;
            if (lastMemory > 0)
                doublingRatio = (double) currentAverageMemory / lastMemory;

            System.out.println("n: " + currentComplexity +
                " | Avg memory: " + String.format("%.1f", ((double) currentAverageMemory / 1000000)) + "mb / " + currentAverageMemory + "b" +
                " | r: " + doublingRatio);

            lastMemory = currentAverageMemory;
        }
    }

    private static long getMemoryUsed(Function<Integer, String[]> generator, Consumer<String[]> executer, int complexity, int trials)
    {
        long startMemory;
        long stopMemory;
        long memoryUsed = 0;
        String[] example;

        for (int i = 0; i < trials; i++)
        {
            example = generator.apply(complexity);
            System.gc();
            startMemory = getCurrentMemory();
            executer.accept(example);
            stopMemory = getCurrentMemory();
            if (stopMemory > startMemory) //gc might have been called and memory used can't be negative
                memoryUsed += stopMemory - startMemory;
        }

        memoryUsed /= trials;
        return memoryUsed;
    }

    private static long getCurrentMemory()
    {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    // temporal tests

    private static void testTime(String name, Function<Integer, String[]> generator, int complexity, Consumer<String[]> executer, int trials)
    {
        long time = TimeAnalysisUtils.getAverageCPUTime(generator, complexity, executer, trials);
        System.out.println("(n=" + complexity + ") " + name + " time: " + time + "ns | " + (time / 1000000) + "ms");
    }

    private static void doublingRatioOfAverage(Function<Integer, String[]> generator, Consumer<String[]> function, int iterations, int trials)
    {
        int currentComplexity = 125;
        long lastTime = 0;
        for (int i = 0; i < iterations; i++)
        {
            currentComplexity *= 2;
            long currentAverageNano = TimeAnalysisUtils.getAverageCPUTime(generator, currentComplexity, function, trials);
            double doublingRatio = 0;
            if (lastTime > 0)
                doublingRatio = (double) currentAverageNano / lastTime;

            System.out.println("n: " + currentComplexity +
                " | Avg time: " + String.format("%.1f", ((double) currentAverageNano / 1000000)) + "ms / " + currentAverageNano + "ns" +
                " | r: " + doublingRatio);

            lastTime = currentAverageNano;
        }
    }

    private static String[] generateDuplicatesHalfLen(Integer size)
    {
        final char[] possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        final int len = 500;

        String[] strings = new String[size];
        StringBuilder buffer = new StringBuilder(len);
        for (int j = 0; j < len; j++)
            buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
        String dup = buffer.toString();
        for (int i = 0; i < size; i++)
            strings[i] = dup;
        return strings;
    }

    private static String[] generateDuplicatesLessSymbols(Integer size)
    {
        final char[] possibleChars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final int len = 1000;

        String[] strings = new String[size];
        StringBuilder buffer = new StringBuilder(len);
        for (int j = 0; j < len; j++)
            buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
        String dup = buffer.toString();
        for (int i = 0; i < size; i++)
            strings[i] = dup;
        return strings;
    }

    private static String[] generateLessSymbols(Integer size)
    {
        final char[] possibleChars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final int maxLen = 1000;

        String[] strings = new String[size];
        for (int i = 0; i < size; i++)
        {
            int len = 1 + R.nextInt(maxLen);
            StringBuilder buffer = new StringBuilder(len);
            for (int j = 0; j < len; j++)
                buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
            strings[i] = buffer.toString();
        }
        return strings;
    }

    private static String[] generateDuplicates(Integer size)
    {
        final char[] possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        final int len = 1000;

        String[] strings = new String[size];
        StringBuilder buffer = new StringBuilder(len);
        for (int j = 0; j < len; j++)
            buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
        String dup = buffer.toString();
        for (int i = 0; i < size; i++)
            strings[i] = dup;
        return strings;
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
            int len = 1 + R.nextInt(maxLen);
            StringBuilder buffer = new StringBuilder(len);
            for (int j = 0; j < len; j++)
                buffer.append(possibleChars[R.nextInt(possibleChars.length)]);
            strings[i] = buffer.toString();
        }
        return strings;
    }
}
