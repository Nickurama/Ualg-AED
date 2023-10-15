import java.util.Arrays;
import java.util.Random;

public class App
{
    public static void main(String[] Args) throws Exception
    {
        Executable<Void> exe = (Void) -> waitOneSec();
        Generator<Void> gen = (n) -> null;
        FunctionTimer<Void> functionTimer = new FunctionTimer<Void>(exe, gen);
        functionTimer.startLogging();
        //functionTimer.getAverageCPU(1, 100);

        Executable<int[]> exe2 = args -> twoSum(args[0]);
        Generator<int[]> gen2 = (n) -> new int[][] {twoSumGenerator(n)};
        FunctionTimer<int[]> functionTimer2 = new FunctionTimer<int[]>(exe2, gen2);
        //functionTimer2.startLogging();
        //functionTimer2.getAverageCPU(1, 100);
        functionTimer2.doubledRatioTest(1000);

        Executable<int[]> exe3 = args -> threeSum(args[0]);
        Generator<int[]> gen3 = (n) -> new int[][] {twoSumGenerator(n)};
        FunctionTimer<int[]> functionTimer3 = new FunctionTimer<int[]>(exe3, gen3);
        //functionTimer3.startLogging();
        //functionTimer3.doubledRatioTest(100);
    }

    public static void waitOneSec()
    {
        double start = System.nanoTime();
        while (System.nanoTime() - start < 1000000000);
    }

    public static int twoSum(int[] a)
    {
        int n = a.length;
        int count = 0;

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (a[i] + a[j] == 0)
                    count++;

        return count;
    }

    public static int[] twoSumGenerator(int n)
    {
        Random r = new Random();
        int[] examples = new int[n];
        for (int i = 0; i < n; i++)
        {
            examples[i] = r.nextInt();
        }
        return examples;
    }

    public static int threeSum(int[] a)
    {
        int n = a.length;
        int count = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                for (int k = j + 1; k < n; k++)
                {
                    if (a[i] + a[j] + a[k] == 0)
                        count++;
                }
            }
        }
        return count;
    }
}
