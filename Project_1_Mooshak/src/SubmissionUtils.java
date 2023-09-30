import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubmissionUtils
{
    public static Submission parseSubmission(String line)
    {
        
        //dica: usar este formatador para fazer a leitura do tempo. Ver método estático parse da classe LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        //TODO: implement
		return null;
    }

    public static List<Submission> readSubmissionsFromFile(String fileName)
    {
        //TODO: implement
		return null;
    }

    public static void writeSubmissionsToFile(String fileName, List<Submission> submissions)
    {
        //TODO: implement
    }

    public static void sortSubmissions(List<Submission> submissions)
    {
        //TODO: implement
    }

    public static void printSubmissions(List<Submission> submissions, int n)
    {
        //TODO: implement
    }

    public static List<Submission> filterByProblem(List<Submission> submissions, String problem)
    {
        //TODO: implement
		return null;
    }

    public static Submission getSubmissionWithNumber(List<Submission> submissions, int submissionNumber)
    {
        //TODO: implement
		return null;
    }

    public static void printProblemStats(List<Submission> submissions, String problem)
    {
        //TODO: implement
    }
	
	public static List<Submission> getBestSubmissions(List<Submission> submissions, String teamName)
	{
		//TODO: implement
		return null;
	}
	
	public static List<String> getTeams(List<Submission> submissions)
	{
		//TODO: implement
		return null;
	}

    public static void testUpdate()
    {
        //TODO: implement
    }
}
