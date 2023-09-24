import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Predicate;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        //testFactorials();
        //testLists();
        //testRandom();
        testFile();
    }

    // File

    public static void testFile()
    {
        try
        {
            File file = new File("csvFile.csv");
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                String[] tokens = line.split(",");
                System.out.println(tokens[1]); //prints second token
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
        }
    }

    // Random

    public static void testRandom()
    {
        String[] a = {"isto","é","uma","lista","de","palavras","a","baralhar"};
        sattoloShuffle(a);
        for (String s : a)
            System.out.println(s);
    }

    public static void sattoloShuffle(Object[] a) {
        int n = a.length;
        for (int i = n; i > 1; i--) {
            // choose index uniformly in [0, i-1[
            int r = (int) (Math.random() * (i-1));
            Object swap = a[r];
            a[r] = a[i-1];
            a[i-1] = swap;
        }
    }

    // Lists

    public static void testLists()
    {
        Scanner sc = new Scanner(System.in);
        List<Integer> inputs = new LinkedList<>();
        System.out.println("Type 10 numbers to filter.");
        for (int i = 0; i < 10; i++)
            inputs.add(Integer.parseInt(sc.nextLine()));

        List<Integer> results = filterList(inputs);
        System.out.print("Numbers below 10: ");
        printList(results);

        results = filterList(inputs, Main::isLessThan10);
        System.out.print("Numbers below 10 (generic): ");
        printList(results);
    }

    public static void printList(List<Integer> list)
    {
        for (int n : list)
            System.out.print(n + " ");
        System.out.println();
    }

    public static void testFactorials()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type a number to calculate the factorial.");
        int input = Integer.parseInt(sc.nextLine());
        System.out.println("The Factorial is: " + factorial(input) + " (recursively: " + factorialRecursive(input) + ")");
    }

    public static List<Integer> filterList(List<Integer> list)
    {
        List<Integer> result = new LinkedList<>();
        for (int n : list)
            if (n < 10)
                result.add(n);
        return result;
    }

    public static <T> List<T> filterList(List<T> list, Predicate<T> predicate)
    {
        List<T> result = new LinkedList<>();
        for (T n : list)
            if (predicate.test(n))
                result.add(n);
        return result;
    }

    public static boolean isLessThan10(int n)
    {
        return n < 10;
    }

    // Factorials

    public static int factorial(int n)
    {
        int result = n;
        while (n > 1)
        {
            --n;
            result *= n;
        }
        return result;
    }

    public static int factorialRecursive(int n)
    {
        if (n == 1)
            return 1;
        return n * factorialRecursive(--n);
    }
}