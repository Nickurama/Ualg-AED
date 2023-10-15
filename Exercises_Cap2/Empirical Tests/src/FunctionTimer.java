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

    public long doubledRatioTest(int numOfTests)
    {
        int n = 125;
        return 0;
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
        executable.run(testArgs);
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
            (int) toSeconds(executionTime) + "s / " +
            (int) toMilliseconds(executionTime) + "ms / " +
            executionTime + "ns");
    }

    private void printElapsed(long elapsedCPU)
    {
        System.out.println("Test Completed.");
        System.out.println("Average Time: " +
            (int) toSeconds(elapsedCPU) + "s / " +
            (int) toMilliseconds(elapsedCPU) + "ms / " +
            elapsedCPU + "ns");
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
