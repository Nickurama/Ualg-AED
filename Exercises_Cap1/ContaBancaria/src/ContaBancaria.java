public class ContaBancaria
{
    private double saldo;
    private static long lastNib = 0;
    private final long nib;

    public ContaBancaria()
    {
        this.nib = lastNib++;
        this.saldo = 0;
    }

    public ContaBancaria(long nib) {
        this.nib = nib;
        this.saldo = 0;
    }

    public ContaBancaria(long nib, double saldoInicial)
    {
        this.nib = nib;
        saldo = saldoInicial;
    }

    public double getNIB() { return this.nib; }
    public double getSaldo()
    {
        return this.saldo;
    }

    public void levantamento(double valor)
    {
        if(valor >= 0 && saldo >= valor)
        {
            saldo -= valor;
        }
    }

    public void deposito(double valor)
    {
        if(valor > 0)
        {
            saldo += valor;
        }
    }
}
