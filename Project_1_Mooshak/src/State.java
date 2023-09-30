import java.util.Locale;

public enum State {
    //valores possíveis para o tipo enumerado
    PENDING,
    FINAL;

    @Override
    public String toString()
    {
        return this.name().toLowerCase(Locale.ROOT);
    }

    //este enum é ligeiramente mais fácil de implementar, porque o nome externo é sempre
    //uma versão em letras minúsculas do nome interno do enumerado
    public static State parseSubmissionState(String s)
    {
        for(State state : State.values())
        {
            if(state.name().equalsIgnoreCase(s)) return state;
        }
        return null;
    }
}

