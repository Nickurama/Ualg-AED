import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class FunctionTimer<T>
{
    private static final int NANO_TO_MILLI_SCALAR = 1000000;
    private static final int NANO_TO_SEC_SCALAR = 1000000000;

    private boolean isLogging;
    private Executable<T> executable;
    private Generator<T> generator;

    public FunctionTimer(Executable<T> executable, Generator<T> generator)
    {
        this.executable = executable;
        this.generator = generator;
        this.isLogging = false;
    }

    public static double toSeconds(long n)
    {
        return (double) n / (double) NANO_TO_SEC_SCALAR;
    }

    public static double toMilliseconds(long n)
    {
        return n / NANO_TO_MILLI_SCALAR;
    }

    public void startLogging()
    {
        this.isLogging = true;
    }

    public void stopLogging()
    {
        this.isLogging = false;
    }

    public void doubledRatioTest(int numOfTests)
    {
        // !!! INFINITE LOOP !!!

        int n = 128;
        long previousTime = getAverageCPU(numOfTests, n);
        long newTime;
        double ratio = 0;
        for (n = 256; true; n *= 2)
        {
            newTime = getAverageCPU(numOfTests, n);
            if (previousTime > 0)
                ratio = (double) newTime / (double) previousTime;
            previousTime = newTime;

            printDoubledRatio(n, previousTime, ratio, numOfTests);
        }
    }


    public long getAverageCPU(int numOfTests, int testSize)
    {
        long elapsedCPU = 0;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for (int i = 0; i < numOfTests; i++)
        {
            T[] testArgs = generator.generate(testSize);
            long executionTime = getExecutionTime(threadMXBean, allThreadIds, testArgs);
            elapsedCPU += executionTime;
            if (isLogging)
                printTest(i, numOfTests, executionTime);
        }
        elapsedCPU /= numOfTests;

        if (isLogging)
            printElapsed(elapsedCPU);
        return elapsedCPU;
    }

    @SafeVarargs
    private long getExecutionTime(ThreadMXBean threadMXBean, long[] allThreadIds, T... testArgs)
    {
        long startTime, stopTime; // variable initialization before assignemnt to avoid timing accuracy loss

        startTime = getCPUTime(threadMXBean, allThreadIds);
        //startTime = System.nanoTime();
        executable.run(testArgs);
        //stopTime = System.nanoTime();
        stopTime = getCPUTime(threadMXBean, allThreadIds);

        return stopTime - startTime;
    }

    private long getCPUTime(ThreadMXBean threadMXBean, long[] allThreadIds)
    {
        long TotalCPUTime = 0;
        for (long thread : allThreadIds)
        {
            long cpuTime = threadMXBean.getThreadCpuTime(thread);
            if (cpuTime > 0)
                TotalCPUTime += threadMXBean.getThreadCpuTime(thread);
        }

        return TotalCPUTime;
    }

    private void printTest(int current, int max, long executionTime)
    {
        System.out.println("Test " + (current + 1) + "/" + max + " - Time: " +
            Math.round(toSeconds(executionTime)) + "s / " +
            Math.round(toMilliseconds(executionTime)) + "ms / " +
            executionTime + "ns");
    }

    private void printElapsed(long elapsedCPU)
    {
        System.out.println("Test Completed.");
        System.out.println("Average Time: " +
            Math.round(toSeconds(elapsedCPU)) + "s / " +
            Math.round(toMilliseconds(elapsedCPU)) + "ms / " +
            elapsedCPU + "ns");
    }

    private void printDoubledRatio(int n, long avgTime, double ratio, int numTests)
    {
        String formattedRatio = String.format("%.3f", ratio);
        String separator = " | ";
        long efficiency = 0;
        if (ratio >= 0.05 || ratio <= -0.05)
            efficiency = Math.round(Math.log(ratio) / Math.log(2));

        System.out.println("n: " + n + separator +
            "Ratio: " + formattedRatio + separator +
            "Estimated Efficiency: " + efficiency + separator +
            "Average Time: " +
            Math.round(toSeconds(avgTime)) + "s - " +
            Math.round(toMilliseconds(avgTime)) + "ms - " +
            avgTime + "ns" + separator +
            "Total Time: " +
            Math.round(numTests * toSeconds(avgTime)) + "s - " +
            Math.round(numTests * toMilliseconds(avgTime)) + "ms - " +
            numTests * avgTime + "ns");
    }
}


@SuppressWarnings("unchecked")
interface Executable<T>
{
    public void run(T... args);
}


interface Generator<T>
{
    public T[] generate(int n);
}


// interface Tester
// {
//     public <T> void run(T[] arg);

//     public <T> T[] generate(int n);
// }
