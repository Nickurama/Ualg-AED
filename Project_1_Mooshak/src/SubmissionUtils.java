import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubmissionUtils
{
    public static Submission parseSubmission(String line) throws IllegalArgumentException
    {
        //dica: usar este formatador para fazer a leitura do tempo. Ver método estático parse da classe LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String[] tokens = line.split("\t");
        Submission result;

        try
        {
            result = new Submission(Integer.parseInt(tokens[0]), LocalDateTime.parse(tokens[1], formatter),
                    Integer.parseInt(tokens[2]),tokens[3], tokens[4],
                    tokens[5], tokens[6], tokens[7],
                    Result.parseSubmissionResult(tokens[8]), State.parseSubmissionState(tokens[9]));
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Error parsing submission: " + e);
        }

		return result;
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
