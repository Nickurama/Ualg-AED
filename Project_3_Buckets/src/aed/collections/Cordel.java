package aed.collections;

import java.util.Iterator;
import java.util.Stack;

public class Cordel implements Iterable<String>{

    //Nesta implementação, iremos usar a própria classe Cordel como nó, em vez de termos uma classe separada para representar
    //os nós.
    //Há algumas razões para fazer isto, a mais importante é que pretendemos fazer uma implementação imutável (não destrutiva)
    //de cordeis, o que quer dizer que uma vez criado um cordel, os seus campos não vão mais ser alterados, e portanto
    //quer dizer que parte dos nossos métodos vão ter que retornar cordéis, pois é a única forma de conseguirmos construir
    //cordéis mais elaborados. Por exemplo, quando quero concatenar uma string a um cordel, vou ter que retornar um
    // novo cordel (não posso alterar o cordel original) que corresponde a um nó de concatenação entre o cordel original e um
    // novo cordel folha com a string recebida.

    //Devido a esta característica, e ao facto de toda a informação que precisamos estar guardada no nó, não
    //se justifica estar a criar uma classe interna Node, e fazer com que a Classe cordel tenha apenas um membro root
    // (iríamos gastar mais memória sem nenhuma vantagem).

    //Estes membros são declarados como final, para o compilador de Java garantir que uma vez definidos no construtor
    //nunca mais podem ser alterados.
    private final Cordel left;
    private final Cordel right;
    //Importante: normalmente, os cordéis implementam-se usando apenas informação sobre o tamanho da subárvore esquerda
    private final int leftLength;
    //Mas no nosso caso, queremos também guardar informação sobre o tamanho da subárvore direita para tornar a contatenação
    //ligeiramente mais eficiente (se não tivessemos este membro, teríamos de pagar O(log n) para calcular o tamanho do
    //lado direito)
    private final int rightLength;
    private final String string;

    //Cria e inicializa um cordel folha (LEAF) com a string s recebida
    //A string s não pode ser nula
    public Cordel(String s)
    {
        //todos os membros têm de ser inicializados, dado que foram declarados como final
        //lembrem-se que um nó folha não tem filhos.
        //TODO: implement
    }

    //Cria e inicializa um cordel que corresponde a um nó de concatenação (CONCAT)
    //e que concatena os dois cordéis recebidos
    public Cordel(Cordel left, Cordel right)
    {
        //TODO: implement
    }

    //Retorna o tamanho de um cordel, ou seja, o número de caracteres total guardado no cordel.
    public int length()
    {
        //TODO: implement
		return 0;
    }

    //Dado uma string, retorna um novo cordel que corresponde à concatenação à direita da string recebida com o cordel.
    public Cordel append(String s)
    {
        //TODO: implement
		return null;
    }

    //Dado um Cordel c, retorna um novo cordel que corresponde à concatenação à direita do cordel c recebido com o cordel.
    public Cordel append(Cordel c)
    {
		//TODO: implement
        return null;
    }

    //Dado uma string, retorna um novo cordel que corresponde à concatenação à esquerda da string recebida com o cordel.
    public Cordel prepend(String s)
    {
        //TODO: implement
		return null;
    }

    //Dado um Cordel c, retorna um novo cordel que corresponde à concatenação à esquerda do cordel c recebido com o cordel.
    public Cordel prepend(Cordel c)
    {
		//TODO: implement
        return null;
    }

    //Imprime as strings guardadas dentro deste cordel, pela ordem da string mais à esquerda para a mais à direita.
    //Não imprime mudanças de linha.
    public void print()
    {
        //TODO: implement
    }

    //Igual ao anterior, mas imprime uma nova linha no fim
    public void println()
    {
        print();
        System.out.println();
    }

    //Imprime informação sobre cada um dos nós da árvore.
    public void printInfo()
    {
        //TODO: implement
    }

    //igual ao anterior, mas imprime uma nova linha no fim
    public void printInfoNL()
    {
        this.printInfo();
        System.out.println();
    }

    //Dado um índice, devolve o caracter correspondente ao índice i do cordel.
    public char charAt(int i)
    {
        //TODO: implement
		return 0;
    }

    //Dado um índice, parte o cordel em dois cordéis no índice i. É devolvido um array com 2 cordéis.
    // O primeiro cordel contém as strings com todos os caracteres desde o índice 0 até o índice i-1,
    // e o segundo cordel contém as strings com todos os caracteres desde o índice i até ao fim.
    public Cordel[] split(int i)
    {
        //TODO: implement
		return null;
    }

    //Dado um índice, e uma string que não pode ser nula, retorna um cordel que corresponde ao
    // resultado de inserirmos a string s, na posição i do cordel.
    public Cordel insertAt(int i, String s)
    {
        //TODO: implement
		return null;
    }

    //Dado um índice i, e um tamanho, apaga a partir do índice i do cordel
    // um número de caracteres correspondentes ao tamanho recebido.
    public Cordel delete(int i, int length)
    {
       //TODO: implement
	   return null;
    }

    

    //Devolve um iterador que vai percorrer todas as strings guardadas em nós folha deste cordel,
    // pela ordem correta (i.e. da esquerda para a direita).
    @Override
    public Iterator<String> iterator() {
        //TODO: implement
		return null;
    }

    //Dado um índice i, e um tamanho, imprime um número de caracteres do cordel igual ao tamanho recebido,
    // a partir do índice i (inclusive).
    public void print(int i, int length)
    {
        //TODO: implement
    }

    public void println(int i, int length)
    {
        print(i,length);
        System.out.println();
    }

    public static void main(String[] args)
    {
        //colocar aqui testes efetuados
    }
}
