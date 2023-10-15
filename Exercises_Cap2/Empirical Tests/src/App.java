import java.util.Random;

public class App
{
    public static void main(String[] args) throws Exception
    {
        Executable exe = () -> waitOneSec();
        Generator gen = n -> twoSumGenerateExample(n);
        FunctionTimer functionTimer = new FunctionTimer(exe);
        functionTimer.startLogging();
        functionTimer.getAverageCPU(100);
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

    public static int[] twoSumGenerateExample(int n)
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
