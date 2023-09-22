public class SimpleMain {

    //ficheiro usado para o exercício 1.1.2
    public static void main(String[] args)
    {

        //Uma vez definida a classe ContaBancaria, podemos começar a criar novos objetos
        //do tipo ContaBancaria, que correspondem a instâncias da classe
        //Criacao de 2 contas bancarias de exemplo
        ContaBancaria c1 = new ContaBancaria(1);
        ContaBancaria c2 = new ContaBancaria(1,20);


        //cada conta (c1 e c2) é um objeto computacional diferente, e portanto se alterarmos o saldo
        //de uma não alteraremos o saldo de outra
        //Para invocarmos um método de uma classe (que não seja static) precisamos de ter uma instância
        // (um objecto dessa classe). Fazemos <variavel quem contém objeto>.<nome do método da classe>(<argumentos>)
        //Faz um depósito com valor 5 no objeto que corresponde à conta c1
        c1.deposito(5);
        //Faz um depósito com valor 10 no objeto que corresponde à conta c2
        c2.deposito(10);

        //vamos verificar os saldos, usando o seletor getSaldo
        //nota, se tentarmos usar diretamente o campo saldo, obtemos erro, pois o campo saldo
        //é privado
        //Em Java eu posso concatenar strings (cadeias de caracteres) usando o +
        //o método getSaldo retorna um double mas o Java sabe transformar doubles
        //em strings automaticamente
        System.out.println("Saldo C1:" + c1.getSaldo());
        System.out.println("Saldo C2:" + c2.getSaldo());

        //vamos levantar dinheiro e ver o que acontece
        c2.levantamento(7.5);
        System.out.println("Saldo C1:" + c1.getSaldo());
        System.out.println("Saldo C2:" + c2.getSaldo());
    }
}
