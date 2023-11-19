package aed.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TimeAnalysisUtils {

    private static final int DEFAULT_TRIALS = 30;
    private static final int MINIMUM_COMPLEXITY = 125;


    public static<T> void runDoublingRatioTest(Function<Integer,T> exampleGenerator, Consumer<T> methodToTest, int iterations)
    {
        assert(iterations > 0);
        int n = MINIMUM_COMPLEXITY;
        double previousTime = getAverageCPUTime(exampleGenerator,n,methodToTest,DEFAULT_TRIALS);
        System.out.println("i\tcomplexity\ttime(ms)\testimated r");
        System.out.println("0\t" + n + "\t" + previousTime + "\t ---");
        double newTime;
        double doublingRatio;


        for(int i = 0; i < iterations; i++)
        {
            n *= 2;
            newTime = getAverageCPUTime(exampleGenerator,n,methodToTest,DEFAULT_TRIALS);
            if(previousTime > 0)
            {
                doublingRatio = newTime/previousTime;
            }
            else doublingRatio = 0;

            previousTime = newTime;
            System.out.println(i+1 + "\t" + n + "\t" + newTime/1E6 + "\t" + doublingRatio);
        }
    }

    //this method is used to empirically determine the average execution time of the received consumer method across a number of trials
    //However, it additionally receives a method to generate a new example for a given complexity size n. The example generation method will not
    //count towards the execution time.
    //This is useful when we want to perform a doubling ratio analysis
    //@param exampleGenerator - a method that receives an integer representing the complexity of a problem, and generates a new example with the given complexity
    //@param complexity - the level of complexity used to generate and example
    //@param method - the method for which we want to measure average time, receiving the generated example
    //@param trials - the number of trials
    public static<T> long getAverageCPUTime(Function<Integer,T> exampleGenerator, int complexity, Consumer<T> method, int trials)
    {
        long startTime;
        long stopTime;
        long elapsedCPU = 0;
        T example;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for(int i = 0; i < trials; i++)
        {
            example = exampleGenerator.apply(complexity);
            startTime = getCPUTime(threadMXBean,allThreadIds);
            method.accept(example);
            stopTime = getCPUTime(threadMXBean,allThreadIds);
            elapsedCPU += stopTime - startTime;
        }

        elapsedCPU /= trials;

        return elapsedCPU;
    }

    //this method is used to empirically determine the average execution time of the received consumer method across a number of trials
    //However, it additionally receives a method to initialize/copy the object. The initialization method will not
    //count towards the execution time.
    //This is useful when we want to analyze the execution time of a method working with a complex object that will be destroyed
    //or changed by the method.
    //For instance, if we want to measure the execution time of an operation that deletes/removes elements from a list,
    //in the initialization method we want to initialize the list (or create a copy of an already initialized list),
    //and in the consumer method we want to delete the elements from that list
    //@param object - the initial object used for initialization
    //@param setupMethod - a method that is applied to the initial object and returns the initialized object (or a copy of the object)
    //@param method - the method for which we want to measure average time, receiving the initialized object
    //@param trials - the number of trials
    public static<T> long getAverageCPUTime(T object, UnaryOperator<T> setupMethod, Consumer<T> method, int trials)
    {
        long startTime;
        long stopTime;
        long elapsedCPU = 0;
        T startingObject;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for(int i = 0; i < trials; i++)
        {
            startingObject = setupMethod.apply(object);
            startTime = getCPUTime(threadMXBean,allThreadIds);
            method.accept(startingObject);
            stopTime = getCPUTime(threadMXBean,allThreadIds);
            elapsedCPU += stopTime - startTime;
        }

        elapsedCPU /= trials;

        return elapsedCPU;
    }

    public static<T> long getAverageCPUTime(T object, UnaryOperator<T> setupMethod, Consumer<T> method)
    {
        return getAverageCPUTime(object, setupMethod, method, DEFAULT_TRIALS);
    }


    //this method empirically determines the average execution time of the received method across a number of trials
    public static long getAverageCPUTime(Runnable method, int trials)
    {
        long startTime;
        long stopTime;
        long elapsedCPU = 0;

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for(int i = 0; i < trials; i++)
        {
            startTime = getCPUTime(threadMXBean,allThreadIds);
            method.run();
            stopTime = getCPUTime(threadMXBean,allThreadIds);
            elapsedCPU += stopTime - startTime;
        }

        elapsedCPU /= trials;

        return elapsedCPU;
    }

    public static long getAverageCPUTime(Runnable method)
    {
        return getAverageCPUTime(method,DEFAULT_TRIALS);
    }

    private static long getCPUTime(ThreadMXBean threadMXBean, long[] allThreadIds)
    {
        long nano = 0;
        for (long id : allThreadIds) {
            nano += threadMXBean.getThreadCpuTime(id);
        }
        return nano;
    }

}
