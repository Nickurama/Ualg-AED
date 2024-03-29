import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission
{

    // fields

    private Integer number;
    private Integer points;
    private LocalDateTime timeSubmitted;
    private String group;
    private String teamID;
    private String teamName;
    private String problem;
    private String language;
    private Result result;
    private State state;

    // methods

    public Submission(int number, LocalDateTime timeSubmitted,
        int points, String group, String teamID, String teamName,
        String problem, String language, Result result, State state)
    {
        this.number = number;
        this.points = points;
        this.timeSubmitted = timeSubmitted;
        this.group = group;
        this.teamID = teamID;
        this.teamName = teamName;
        this.problem = problem;
        this.language = language;
        this.result = result;
        this.state = state;
    }


    public int getNumero()
    {
        return this.number;
    }

    public LocalDateTime getTempo()
    {
        return this.timeSubmitted;
    }

    public int getPontos()
    {
        return this.points;
    }

    public String getGrupo()
    {
        return this.group;
    }

    public String getIdEquipa()
    {
        return this.teamID;
    }

    public String getNomeEquipa()
    {
        return this.teamName;
    }

    public String getProblema()
    {
        return this.problem;
    }

    public String getLinguagem()
    {
        return this.language;
    }

    public Result getResultado()
    {
        return this.result;
    }

    public State getEstado()
    {
        return this.state;
    }

    public void update(int points)
    {
        this.points = points;
        this.result = Result.ACCEPTED;
        this.state = State.FINAL;
    }

    public boolean equals(Submission that)
    {
        if (that == null)
            return false;
        return this.compareTo(that) == 0;
    }

    public int compareTo(Submission that)
    {

        return that.number - this.getNumero();
    }

    @Override
    // <número submissão>,<tempo>,<pontos>,<id da equipa>,<problema>,<resultado>,<estado>
    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String timeSubmittedFormatted = formatter.format(this.timeSubmitted);

        Object[] varsToPrint = {this.number, timeSubmittedFormatted,
                this.points, this.teamID, this.problem,
                this.result, this.state};

        return enumerateVars(varsToPrint, ",");
    }

    private static String enumerateVars(Object[] vars, String separator)
    {
        if (vars.length == 0)
            return "";

        StringBuilder builder = new StringBuilder(vars[0].toString());

        for (int i = 1; i < vars.length; i++) // i = 1
            builder.append(separator).append(vars[i].toString());

        return builder.toString();
    }

    // <número submissão><tempo><pontos><grupo><id da equipa><nome da equipa><problema><linguagem><resultado><estado>
    public String toTabString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String timeSubmittedFormatted = formatter.format(this.timeSubmitted);

        Object[] varsToPrint = {this.number, timeSubmittedFormatted,
                this.points, this.group, this.teamID,
                this.teamName, this.problem, this.language,
                this.result, this.state};

        return enumerateVars(varsToPrint, "\t");
    }
}

