import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            List<Submission> subs;
            subs = SubmissionUtils.readSubmissionsFromFile("exemplo_submissoes.tsv");
            //SubmissionUtils.writeSubmissionsToFile("write_test.tsv", subs);
            SubmissionUtils.printSubmissions(subs, 2);
        } catch (Exception e)
        {
            System.out.println("Error reading submissions: " + e);
        }
    }
}
