import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class FunctionTimer
{
    private Testable testable;

    public FunctionTimer(Testable testable)
    {
        this.testable = testable;
    }

    public long getAverageCPU(int numOfTests)
    {
        long elapsedCPU = 0;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for (int i = 0; i < numOfTests; i++)
            elapsedCPU += getExecutionTime(threadMXBean, allThreadIds);
        elapsedCPU /= numOfTests;

        return elapsedCPU;
    }

    private long getExecutionTime(ThreadMXBean threadMXBean, long[] allThreadIds)
    {
        long startTime, stopTime;

        startTime = getCPUTime(threadMXBean, allThreadIds);
        testable.run();
        stopTime = getCPUTime(threadMXBean, allThreadIds);

        return stopTime - startTime;
    }

    private long getCPUTime(ThreadMXBean threadMXBean, long[] allThreadIds)
    {
        long CPUTime = 0;
        for (long thread : allThreadIds)
            CPUTime += threadMXBean.getThreadCpuTime(thread);
        return CPUTime;
    }

}


interface Testable
{
    public void run();
}
