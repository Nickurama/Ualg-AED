import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            //List<Submission> subs = SubmissionUtils.readSubmissionsFromFile("exemplo_submissoes.tsv");
            //SubmissionUtils.writeSubmissionsToFile("write_test.tsv", subs);
            //List<Submission> filtered = SubmissionUtils.filterByProblem(subs, "A");
            //SubmissionUtils.printSubmissions(filtered, 10);
            //SubmissionUtils.printProblemStats(subs, "G");
            //List<Submission> best = SubmissionUtils.getBestSubmissions(subs, "Grey Rino 54806");
            //SubmissionUtils.printSubmissions(best, 100);
            //List<String> teams = SubmissionUtils.getTeams(subs);
            //for (String s : teams)
            //    System.out.println(s);
            SubmissionUtils.testUpdate();
        } catch (Exception e)
        {
            System.out.println("Error reading submissions: " + e);
        }
    }
}
