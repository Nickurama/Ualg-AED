import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            List<Submission> subs = SubmissionUtils.readSubmissionsFromFile("exemplo_submissoes.tsv");
            //SubmissionUtils.writeSubmissionsToFile("write_test.tsv", subs);
            //List<Submission> filtered = SubmissionUtils.filterByProblem(subs, "A");
            //SubmissionUtils.printSubmissions(filtered, 10);
            SubmissionUtils.printProblemStats(subs, "G");
        } catch (Exception e)
        {
            System.out.println("Error reading submissions: " + e);
        }
    }
}
