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
        recursiveSort(Arrays.asList(a), 0);
    }


    @SuppressWarnings("unchecked")
    public static void recursiveSort(List<String> a, int depth)
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
                recursiveSort(bucket, depth + 1);

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
        fasterRecursiveSortEnglish(Arrays.asList(a), 0);
    }

    public static void fasterSort1(String[] a)
    {
        fasterRecursiveSort(Arrays.asList(a), 0);
    }

    @SuppressWarnings("unchecked")
    public static void fasterRecursiveSort(List<String> a, int depth)
    {
        if (a.size() <= CUTOFF_VALUE)
        {
            insertionSort(a);
            return;
        }

        Limits limits = fasterDetermineLimits(a, depth);
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
                fasterRecursiveSort(bucket, depth + 1);

        int index = 0;
        for (String s : ordered)
            a.set(index++, s);
        for (ArrayList<String> bucket : buckets)
            if (bucket != null)
                for (String s : bucket)
                    a.set(index++, s);
    }

    private static Limits fasterDetermineLimits(List<String> a, int characterIndex)
    {
        Limits limits = new Limits();
        limits.minChar = 0;
        if (!a.isEmpty() && a.get(0).length() > characterIndex)
            limits.minChar = a.get(0).charAt(characterIndex);
        limits.maxChar = 0;

        for (String s : a)
        {
            char c = 0;
            if (characterIndex < s.length())
                c = s.charAt(characterIndex);

            if (c > limits.maxChar)
                limits.maxChar = c;
            if (c < limits.minChar)
                limits.minChar = c;
        }

        return limits;
    }

    public static void fasterSort2(String[] a)
    {
        fasterRecursiveSort2(Arrays.asList(a), 0);
    }

    @SuppressWarnings("unchecked")
    public static void fasterRecursiveSort2(List<String> a, int depth)
    {
        if (a.size() <= CUTOFF_VALUE)
        {
            insertionSort(a);
            return;
        }

        Limits limits = fasterDetermineLimits(a, depth);
        int bucketSize = limits.maxChar - limits.minChar + 1;
        ArrayList<String>[] buckets = (ArrayList<String>[]) new ArrayList[bucketSize];
        int overrideIndex = 0;
        for (String s : a)
        {
            if (s.length() > depth)
            {
                int index = s.charAt(depth) - limits.minChar;
                if (buckets[index] == null)
                    buckets[index] = new ArrayList<String>();
                buckets[index].add(s);
            } else
                a.set(overrideIndex++, s);
        }

        for (ArrayList<String> bucket : buckets)
            if (bucket != null)
            {
                fasterRecursiveSort2(bucket, depth + 1);
                for (String s : bucket)
                    a.set(overrideIndex++, s);
            }
    }

    public static void fasterSortEnglish(String[] a)
    {
        fasterRecursiveSortEnglish(Arrays.asList(a), 0);
    }

    @SuppressWarnings("unchecked")
    public static void fasterRecursiveSortEnglish(List<String> a, int depth)
    {
        if (a.size() <= CUTOFF_VALUE)
        {
            insertionSort(a);
            return;
        }

        ArrayList<String>[] buckets = (ArrayList<String>[]) new ArrayList[96];
        ArrayList<String> nonEnglishCharactersUpper = new ArrayList<String>();
        ArrayList<String> nonEnglishCharactersLower = new ArrayList<String>();
        int overrideIndex = 0;
        for (String s : a)
        {
            if (s.length() <= depth)
            {
                a.set(overrideIndex++, s);
                continue;
            }

            char currentChar = s.charAt(depth);
            if (currentChar >= 128)
            {
                nonEnglishCharactersUpper.add(s);
                continue;
            }
            if (currentChar <= 31)
            {
                nonEnglishCharactersLower.add(s);
                continue;
            }

            int index = currentChar - 32;
            if (buckets[index] == null)
                buckets[index] = new ArrayList<String>();
            buckets[index].add(s);
        }

        if (!nonEnglishCharactersLower.isEmpty())
        {
            fasterSortOneIteration(nonEnglishCharactersLower, depth);
            for (String s : nonEnglishCharactersLower)
                a.set(overrideIndex++, s);
        }

        for (ArrayList<String> bucket : buckets)
            if (bucket != null)
            {
                fasterRecursiveSortEnglish(bucket, depth + 1);
                for (String s : bucket)
                    a.set(overrideIndex++, s);
            }

        if (!nonEnglishCharactersUpper.isEmpty())
        {
            fasterSortOneIteration(nonEnglishCharactersUpper, depth);
            for (String s : nonEnglishCharactersUpper)
                a.set(overrideIndex++, s);
        }
    }

    @SuppressWarnings("unchecked")
    private static void fasterSortOneIteration(List<String> a, int depth)
    {
        if (a.size() <= CUTOFF_VALUE)
        {
            insertionSort(a);
            return;
        }

        Limits limits = fasterDetermineLimits(a, depth);
        int bucketSize = limits.maxChar - limits.minChar + 1;
        ArrayList<String>[] buckets = (ArrayList<String>[]) new ArrayList[bucketSize];
        int overrideIndex = 0;
        for (String s : a)
        {
            int index = s.charAt(depth) - limits.minChar;
            if (buckets[index] == null)
                buckets[index] = new ArrayList<String>();
            buckets[index].add(s);
        }

        for (ArrayList<String> bucket : buckets)
            if (bucket != null)
            {
                fasterRecursiveSortEnglish(bucket, depth + 1);
                for (String s : bucket)
                    a.set(overrideIndex++, s);
            }
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
        Function<Integer, String[]> generatorASCII = RecursiveStringSort::generateASCII;
        Consumer<String[]> qsortTest = RecursiveStringSort::quicksort;
        Consumer<String[]> sortTest = RecursiveStringSort::sort;
        Consumer<String[]> fasterSortTest = RecursiveStringSort::fasterSort1;
        Consumer<String[]> fasterSortTest2 = RecursiveStringSort::fasterSort2;
        Consumer<String[]> fasterSortTest3 = RecursiveStringSort::fasterSortEnglish;

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
        //doublingRatioForMemory(generator, sortTest, 16, 100);

        // Comparative Tests For Sort and FasterSort
        //testTime("recursiveSort", generator, 1000, sortTest, 10000);
        //testTime("fasterSort", generator, 1000, fasterSortTest, 10000);
        //compareTimes(generator, 1024000, 100, sortTest, "recursiveSort", fasterSortTest, "fasterSort V1");
        //compareTimes(generator, 1024000, 100, sortTest, "recursiveSort", fasterSortTest2, "fasterSort V2");
        //compareTimes(generatorASCII, 1024000, 100, sortTest, "recursiveSort", fasterSortTest3, "fasterSort V3");

        // Doubling Ratio Tests For FasterSort
        // System.out.println("recursiveSort");
        // doublingRatioOfAverage(generatorASCII, sortTest, 13, 100);
        // System.out.println("fasterSortV1");
        // doublingRatioOfAverage(generatorASCII, fasterSortTest, 13, 100);
        // System.out.println("fasterSortV2");
        // doublingRatioOfAverage(generatorASCII, fasterSortTest2, 13, 100);
        // System.out.println("fasterSortV3");
        // doublingRatioOfAverage(generatorASCII, fasterSortTest3, 13, 100);
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

    private static void compareTimes(Function<Integer, String[]> generator, int complexity, int trials,
        Consumer<String[]> method0, String name0, Consumer<String[]> method1, String name1)
    {
        Function<Integer, String[]> staticGenerator = RecursiveStringSort::staticGenerator;
        Function<Integer, String[]> staticGenerator2 = RecursiveStringSort::staticGenerator2;
        long avg0 = 0;
        long avg1 = 0;

        int progress = 0;
        final int frequency = 5;
        for (int i = 0; i < trials; i++)
        {
            if (((double) i / trials) * 100 - progress >= frequency)
            {
                progress += frequency;
                System.out.println(progress + "% completed...");
            }
            String[] localStaticArray = generator.apply(complexity);
            //staticArray = localStaticArray.clone();
            staticArray = arrayCopy(localStaticArray);
            staticArray2 = arrayCopy(localStaticArray);
            System.gc();
            avg0 += TimeAnalysisUtils.getAverageCPUTime(staticGenerator, complexity, method0, 1) / trials;
            System.gc();
            avg1 += TimeAnalysisUtils.getAverageCPUTime(staticGenerator2, complexity, method1, 1) / trials;
        }
        printResults(name0, avg0, complexity);
        printResults(name1, avg1, complexity);
        if (avg0 < avg1)
            printStats(name0, name1, avg1, avg0);
        else
            printStats(name1, name0, avg0, avg1);
    }

    private static String[] arrayCopy(String[] array)
    {
        String[] copy = new String[array.length];
        for (int i = 0; i < array.length; i++)
            copy[i] = array[i];
        return copy;
    }

    private static void printStats(String name0, String name1, long avg0, long avg1)
    {
        String ratio = String.format("%.1f", ((double) avg0 / avg1) * 100 - 100);
        System.out.println(name0 + " outperformed " + name1 + " by " + ratio + "%");
    }

    private static void printResults(String name, long avg, int complexity)
    {
        String milliseconds = String.format("%.1f", ((double) avg / 1000000));
        System.out.println("(n=" + complexity + ") " + name + " time: " + avg + "ns | " + milliseconds + "ms");
    }

    private static String[] staticArray;
    private static String[] staticArray2;

    private static String[] generateASCII(Integer size)
    {
        final int maxLen = 1000;

        String[] strings = new String[size];
        for (int i = 0; i < size; i++)
        {
            int len = 1 + R.nextInt(maxLen);
            StringBuilder buffer = new StringBuilder(len);
            for (int j = 0; j < len; j++)
                buffer.append(32 + R.nextInt(96));
            strings[i] = buffer.toString();
        }
        return strings;
    }

    private static String[] staticGenerator(Integer size)
    {
        return staticArray;
    }

    private static String[] staticGenerator2(Integer size)
    {
        return staticArray2;
    }

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
