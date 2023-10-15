import java.util.Arrays;
import java.util.Random;

public class App
{
    public static void main(String[] args) throws Exception
    {
        Executable<Void> exe = (Void) -> waitOneSec();
        Generator<Void> gen = (n) -> null;
        FunctionTimer<Void> functionTimer = new FunctionTimer<Void>(exe, gen);
        functionTimer.startLogging();
        //functionTimer.getAverageCPU(1, 100);

        Executable<int[]> exe2 = a -> twoSum(a[0]);
        Generator<int[]> gen2 = (n) -> new int[][] {twoSumGenerator(n)};
        FunctionTimer<int[]> functionTimer2 = new FunctionTimer<int[]>(exe2, gen2);
        functionTimer2.startLogging();
        //functionTimer2.getAverageCPU(1, 100);
        functionTimer2.doubledRatioTest(1, 100)
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
}
