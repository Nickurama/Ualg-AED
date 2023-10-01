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

    public static List<Submission> readSubmissionsFromFile(String fileName) throws FileNotFoundException
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
            throw new FileNotFoundException("File '" + fileName + "' does not exist: " + e);
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

    public static void writeSubmissionsToFile(String fileName, List<Submission> submissions) throws IOException
    {
        try
        {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(FILE_HEADER + "\n");
            for (Submission sub : submissions)
                writer.write(sub.toTabString() + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e)
        {
            throw new IOException("File '" + fileName + "' could not be created: " + e);
        }
    }

    public static void sortSubmissions(List<Submission> submissions)
    {
        submissions.sort((a, b) -> a.compareTo(b));
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
        // TODO: implement
        return null;
    }

    public static List<String> getTeams(List<Submission> submissions)
    {
        // TODO: implement
        return null;
    }

    public static void testUpdate()
    {
        // TODO: implement
    }
}
