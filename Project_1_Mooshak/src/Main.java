import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            List<Submission> subs;
            subs = SubmissionUtils.readSubmissionsFromFile("exemplo_submissoes.tsv");
            for (Submission sub : subs)
                System.out.println(sub.toString());
        } catch (Exception e)
        {
            System.out.println("Error reading submissions: " + e);
        }
    }
}
