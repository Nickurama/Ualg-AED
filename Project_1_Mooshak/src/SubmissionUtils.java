import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmissionUtils
{
    private static final String FILE_HEADER = "#\tTime\tPoints\tGroup\tId\tTeam\tProblem\tLanguage\tResult\tState";

    public static Submission parseSubmission(String line) throws IllegalArgumentException
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String[] tokens = line.split("\t");
        Submission result;

        try
        {
            result = new Submission(Integer.parseInt(tokens[0]), LocalDateTime.parse(tokens[1], formatter),
                Integer.parseInt(tokens[2]), tokens[3], tokens[4],
                tokens[5], tokens[6], tokens[7],
                Result.parseSubmissionResult(tokens[8]), State.parseSubmissionState(tokens[9]));
        } catch (Exception e)
        {
            throw new IllegalArgumentException("Error parsing submission: " + e);
        }

        return result;
    }

    public static List<Submission> readSubmissionsFromFile(String fileName)
    {
        List<Submission> result = new ArrayList<Submission>();

        try
        {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            readSubmissionsToList(reader, result);
            fr.close();
        } catch (Exception e)
        {
            //throw new FileNotFoundException("File '" + fileName + "' does not exist: " + e);
            System.out.println(e.getMessage());
        }

        return result;
    }

    private static void readSubmissionsToList(BufferedReader reader, List<Submission> list) throws IOException
    {
        String line;
        while ((line = reader.readLine()) != null)
        {
            try
            {
                list.add(parseSubmission(line));
            } catch (Exception e)
            {
                //System.out.println("Line didn't have a valid submission.");
            }
        }
    }

    public static void writeSubmissionsToFile(String fileName, List<Submission> submissions)
    {
        try
        {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(FILE_HEADER + "\n");
            ListIterator<Submission> submissionIterator = submissions.listIterator();
            if (submissionIterator.hasNext())
                writer.write(submissionIterator.next().toTabString());
            while (submissionIterator.hasNext())
                writer.write("\n" + submissionIterator.next().toTabString());

            writer.flush();
            writer.close();
        } catch (Exception e)
        {
            //throw new IOException("File '" + fileName + "' could not be created: " + e);
            System.out.println(e.getMessage());
        }
    }

    public static void sortSubmissions(List<Submission> submissions)
    {
        submissions.sort((a, b) -> a.compareTo(b));
    }

    private static void sortSubmissionsByProblemAlphabetic(List<Submission> submissions)
    {
        submissions.sort((a, b) -> a.getProblema().compareTo(b.getProblema()));
    }

    public static void printSubmissions(List<Submission> submissions, int n)
    {
        Iterator<Submission> itr = submissions.iterator();
        for (int i = 0; i < n && itr.hasNext(); i++)
            System.out.println(itr.next().toString());
    }

    public static List<Submission> filterSubmissions(List<Submission> submissions, Predicate<Submission> pred)
    {
        List<Submission> result = new ArrayList<Submission>();
        for (Submission sub : submissions)
            if (pred.test(sub))
                result.add(sub);
        return result;
    }

    public static List<Submission> filterByProblem(List<Submission> submissions, String problem)
    {
        return filterSubmissions(submissions, a -> problem.equals(a.getProblema()));
    }

    public static List<Submission> filterByTeamName(List<Submission> submissions, String teamName)
    {
        return filterSubmissions(submissions, a -> teamName.equals(a.getNomeEquipa()));
    }

    public static Submission getSubmissionWithNumber(List<Submission> submissions, int submissionNumber)
    {
        for (Submission sub : submissions)
            if (sub.getNumero() == submissionNumber)
                return sub;
        return null;
    }

    public static void printProblemStats(List<Submission> submissions, String problem)
    {
        List<Submission> filteredSubmissions = filterByProblem(submissions, problem);
        Stats stats = new Stats(problem);
        for (Submission sub : filteredSubmissions)
            stats.increment(sub.getResultado());
        stats.print();
    }

    public static List<Submission> getBestSubmissions(List<Submission> submissions, String teamName)
    {
        List<Submission> result = new ArrayList<Submission>();
        List<String> uniqueProblems = getUniqueProblems(submissions);
        List<Submission> filteredSubmissions = filterByTeamName(submissions, teamName);
        for (String problem : uniqueProblems)
        {
            List<Submission> problemSubmissions = filterByProblem(filteredSubmissions, problem);
            if (!problemSubmissions.isEmpty())
                result.add(getBestSubmission(problemSubmissions));
        }
        sortSubmissionsByProblemAlphabetic(result);
        return result;
    }

    private static List<String> getUniqueProblems(List<Submission> submissions)
    {
        List<String> result = new ArrayList<String>();
        for (Submission sub : submissions)
            if (!result.contains(sub.getProblema()))
                result.add(sub.getProblema());
        return result;
    }

    private static Submission getBestSubmission(List<Submission> submissions)
    {
        sortSubmissions(submissions);
        Submission result = submissions.get(0);
        for (Submission sub : submissions)
            if ((sub.getPontos() > result.getPontos()) ||
                (sub.getPontos() == result.getPontos() && sub.getNumero() > result.getNumero()))
                result = sub;
        return result;
    }

    public static List<String> getTeams(List<Submission> submissions)
    {
        List<String> result = new ArrayList<String>();
        for (Submission sub : submissions)
            if (!result.contains(sub.getNomeEquipa()))
                result.add(sub.getNomeEquipa());
        result.sort((a, b) -> a.compareTo(b));
        return result;
    }

    public static void testUpdate()
    {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(streamReader);
        try
        {
            String filename = reader.readLine();
            List<Submission> submissions = readSubmissionsFromFile(filename);
            interactiveCommandLine(submissions);
            writeSubmissionsToFile(filename, submissions);

        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private static void interactiveCommandLine(List<Submission> submissions) throws IOException
    {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(streamReader);
        String input;
        while ((input = reader.readLine()) != null)
            processCommand(submissions, input);
    }

    private static void processCommand(List<Submission> submissions, String input)
    {
        String[] tokens = input.split(" ");
        try
        {
            if (tokens[0].toUpperCase().equals("UPDATE"))
                if (tokens.length >= 3)
                    updateCommand(submissions, tokens[1], tokens[2]);
                else
                    System.out.println("Missing args!");
            else
                System.out.println("Unrecognized command '" + tokens[0] + "'!");
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void updateCommand(List<Submission> submissions, String numberStr, String pointsStr)
    {
        try
        {
            int number = Integer.parseInt(numberStr);
            int points = Integer.parseInt(pointsStr);
            Submission found = findSubmission(submissions, number);

            if (found != null)
            {
                found.update(points);
                System.out.println(found.toString());
            } else
                System.out.println("Invalid Submission!");

        } catch (Exception e)
        {
            //throw new InvalidParameterException("Invalid arguments: '" + numberStr + "' '" + pointsStr + "'.");
            System.out.println("Invalid arguments: '" + numberStr + "' '" + pointsStr + "'.");
        }
    }

    private static Submission findSubmission(List<Submission> submissions, int number)
    {
        for (Submission sub : submissions)
            if (sub.getNumero() == number)
                return sub;
        return null;
    }
}


class Stats
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
        //System.out.println(Result.TIME_LIMIT_EXCEEDED.toString() + ": " + timeLimitExceeded);
        //System.out.println(Result.RUNTIME_ERROR.toString() + ": " + runtimeError);
        System.out.println("Run Time Error" + ": " + runtimeError);
        System.out.println(Result.COMPILE_TIME_ERROR.toString() + ": " + compileTimeError);
        System.out.println("Other Errors: " + other);
    }
}
