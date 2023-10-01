public class Stats
{
    String problem;
    int total;
    int accepted;
    int presentationError;
    int wrongAnswer;
    int memoryLimitExceeded;
    int timeLimitExceeded;
    int runtimeError;
    int compileTimeError;
    int other;

    public Stats(String problem)
    {
        this.problem = problem;
        total = 0;
        accepted = 0;
        presentationError = 0;
        wrongAnswer = 0;
        memoryLimitExceeded = 0;
        timeLimitExceeded = 0;
        runtimeError = 0;
        compileTimeError = 0;
        other = 0;
    }

    public void increment(Result result)
    {
        total++;
        switch (result)
        {
            case ACCEPTED:
                accepted++;
                break;
            case PRESENTATION_ERROR:
                presentationError++;
                break;
            case WRONG_ANSWER:
                wrongAnswer++;
                break;
            case MEMORY_LIMIT_EXCEEDED:
                memoryLimitExceeded++;
                break;
            case TIME_LIMIT_EXCEEDED:
                timeLimitExceeded++;
                break;
            case RUNTIME_ERROR:
                runtimeError++;
                break;
            case COMPILE_TIME_ERROR:
                compileTimeError++;
                break;
            default:
                other++;
                break;
        }
    }

    public void print()
    {
        System.out.println("Problem: " + problem);
        System.out.println("Total Submissions: " + total);
        System.out.println(Result.ACCEPTED.toString() + ": " + accepted);
        System.out.println(Result.PRESENTATION_ERROR.toString() + ": " + presentationError);
        System.out.println(Result.WRONG_ANSWER.toString() + ": " + wrongAnswer);
        System.out.println(Result.MEMORY_LIMIT_EXCEEDED.toString() + ": " + memoryLimitExceeded);
        System.out.println(Result.TIME_LIMIT_EXCEEDED.toString() + ": " + timeLimitExceeded);
        System.out.println(Result.RUNTIME_ERROR.toString() + ": " + runtimeError);
        System.out.println(Result.COMPILE_TIME_ERROR.toString() + ": " + compileTimeError);
        System.out.println("other: " + other);
    }
}
