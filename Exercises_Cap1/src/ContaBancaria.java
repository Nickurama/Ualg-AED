//uma classe em Java define um novo tipo de dados, que pode ter membros (também designados de campos) e métodos
//a definição funciona como uma espécie de "blueprint" ou "template" para a criação
//de novos objetos do tipo ContaBancaria
public class ContaBancaria
{
    //estes membros são declarados privados para que código fora da classe não consiga
    //aceder diretamente a estes membros
    private double saldo;
    private long nib;


    //Construtor utilizado para inicializar uma nova instância da classe ContaBancária.
    //Um método construtor de uma classe em Java é um método especial que não retorna nada,
    //apenas inicializa um novo objeto do tipo da classe), e tem obrigatóriamente
    //o mesmo nome que a classe.
    //Em java apenas é necessário inicializar o objeto criado. Não é preciso alocar memória, pq o Java aloca
    //automaticamente a memória para nós. É muito simpático o Java.
    public ContaBancaria(long nib) {
        //a utilização de this.nib permite-te nos ajudar o Java a distinguir entre o membro
        //nib da classe, e o argumento nib recebido pelo método, que são dois objetos computacionais
        //distintos para o java (mas que têm o mesmo nome).
        //this vai corresponder a um ponteiro para o objecto (instância da classe) sobre o qual este método
        //vai ser invocado
        this.nib = nib;
        //nesta linha não é obrigatório usar o this.saldo, porque o Java sabe que saldo só se
        //pode referir ao campo saldo da classe. Ou seja aqui não existe ambiguidade sobre qual o significado do
        //nome "saldo".
        saldo = 0;
        //no entanto, há quem prefira usar sempre o "this." para aceder internamente a mebros de uma classe,
        //pois torna mais explicíto para o programador que é isto o que pretendemos
        //usando o this, a linha anterior ficaria:
        this.saldo = 0;
    }

    //podemos ter mais do que um construtor disponível, recebendo diferentes argumentos
    //quando um programador quiser criar novos objetos do tipo conta bancária, pode usar
    //apenas os construtores definidos, usando-os juntamente com o operador "new"
    //Por exemplo, tendo apenas estes 2 construtores, um programador pode fazer:
    //ContaBancaria c = new ContaBancaria(1231223);
    //ou
    //ContaBancaria c = new ContaBancaria(1231232,0);
    //No entanto, como só existem estes 2 construtores, não é possível por exemplo
    //criar uma conta bancária sem especificar o NIB
    //Se um programador tentar fazer
    //ContaBancaria c = new ContaBancaria();
    //Irá obter um erro de compilação a indicar que não existe nenhum construtor que não receba
    //argumentos
    public ContaBancaria(long nib, double saldoInicial)
    {
        this.nib = nib;
        saldo = saldoInicial;
    }

    //Este tipo de métodos, que permitem aceder de fora a um membro interno/privado da classe, designam-se
    //por selectores (getters em Inglês).
    //Neste caso temos um selector para o NIB, e o seletor é publico.
    //Pode parecer estúpido numa classe tão simples, mas o objetivo da
    //utilização de seletores é permitir ao programador do tipo decidir se
    //e como é que os outros programadores irão aceder
    //aos dados deste tipo. É muito importante para assegurar as barreiras de abstração de dados.
    public long getNIB()
    {
        return this.nib;
    }

    //Outro seletor para o campo saldo
    public double getSaldo()
    {
        return this.saldo;
    }

    //Este método é um método que já corresponde a um comportamento dos objetos da classe.
    //Eu posso levantar dinheiro (desde que tenha saldo)
    public void levantamento(double valor)
    {
        if(valor >= 0 && saldo >= valor)
        {
            saldo -= valor;
        }
    }

    //Método que permite depositar um valor, desde que o montante a depositar não seja negativo
    public void deposito(double valor)
    {
        if(valor > 0)
        {
            saldo += valor;
        }
    }
}
