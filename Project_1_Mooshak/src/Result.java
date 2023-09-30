public enum Result {
    //valores possíveis para o tipo enumerado
    ACCEPTED("Accepted"),
    PRESENTATION_ERROR("Presentation Error"),
    WRONG_ANSWER("Wrong Answer"),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded"),
    PROGRAM_SIZE_EXCEEDED("Program Size Exceeded"),
    INVALID_FUNCTION("Invalid Function"),
    RUNTIME_ERROR("Runtime Error"),
    COMPILE_TIME_ERROR("Compile Time Error"),
    INVALID_SUBMISSION("Invalid Submission"),
    REQUIRES_REEVALUATION("Requires Reevaluation"),
    EVALUATING("Evaluating");

    //os seguintes atributos e métodos são apenas necessários porque queremos usar nomes
    //para o enumerado diferentes dos nomes internos usado pelo Java
    //Por exemplo, PRESENTATION_ERROR é o nome usado internamente, enquanto que externamente
    //usamos Presentation Error
    private String externalName;

    //Este construtor é usado apenas internamente na definição dos valores enumerados
    private Result(String name)
    {
        this.externalName = name;
    }

    @Override
    public String toString()
    {
        return this.externalName;
    }

    public static Result parseSubmissionResult(String s)
    {
        for(Result r : Result.values())
        {
            if(r.externalName.equals(s)) return r;
        }
        return null;
    }
}


