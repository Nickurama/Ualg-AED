package aed.collections;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

/*  Copyright (c) João Dias
    Implementado para a cadeira de AED, 2º ano MEI, FCT, UAlg
 */

// Chamo a esta coleção de métodos UNode. O nome vem de UnsafeNode (por causa da funcionalidade Unsafe do Java)
// e/ou  de UnmanagedNode. Eu até gosto mais de UnmanagedNode, que é a nomenclatura usada em C#.
// O C# chama a estes tipos unmanaged, pois estes tipos não são geridos pelo garbage collector. São geridos manualmente
// pelo programador "à lá C".

// Um Unode consiste num conjunto de métodos para criar nós para uma lista sovina.
// Os nós criados não são instâncias de classes. São mais parecidos com a forma de funcionar de uma struct em C, mas
// como o Java não suporta structs, tive que criar métodos para ler e modificar os membros de um nó.

// Para criar um nó, temos de usar o construtor create_node, que vai reservar memória FORA do HEAP para guardar
// um par de longs (8+8 bytes). O 1.º long guarda o XOR dos nós previous e next, e permite-nos avançar para trás e para
// a frente na sequência de nós. O 2.º long quarda o endereço de memória do objeto que queremos guardar no nó. O construtor
// devolve um long que consiste no endereço de memória (fora do Heap) do nó criado.

// Para ler os membros de um nó podemos usar os métodos get, e para alterar o seu valor podemos usar os métodos set.
// O método free_node liberta a memória ocupada pelo nó.

// Isto dá algum trabalho, mas permite-nos fazer uma lista duplamente ligada em Java gastando apenas 16 bytes por cada nó N.
// Uma lista duplamente ligada implementada de forma tradicional em Java gastaria 40 bytes (2,5x mais).

public class UNode {

    //representamos null como o long 0L.
    private static final long NULL = 0L;
    private static final Unsafe UNSAFE = getUnsafe();
    private static final long ITEM_OFFSET = 8L;

    //usado para ir buscar a funcionalidade Unsafe do Java. É isto que nos permite fazer coisas "à lá C" no Java.
    private static Unsafe getUnsafe()
    {
        try
        {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // Construtor de UNodes. Reserva memória fora do HEAP para guardar informação sobre o nó. Recebe o item a guardar,
    // o endereço de memória do nó anterior, e o endereço de memória do próximo nó. Guarda o xor entre o endereço de memória
    // do nós anterior, e o endereço de memória do próximo nó. Retorna o endereço de memória do nó criado.
    public static <T> long create_node(T item, long previous, long next)
    {
        assert UNSAFE != null;
        long addr = UNSAFE.allocateMemory(16L);
        set_prev_next_addr(addr, previous^next);
        set_item(addr, item);
        return addr;
    }

    // Dado um endereço de memória de um nó préviamente criado pelo construtor create_node, este método irá libertar a
    // memória ocupada pelo nó.
    public static void free_node(long nodeAddr)
    {
        assert UNSAFE != null;
        UNSAFE.freeMemory(nodeAddr);
    }

    // Dado um endereço de memória de um nó, devolve o valor guardado como xor dos endereços previous e next.
    public static long get_prev_next_addr(long nodeAddr) throws NullPointerException
    {
        assert UNSAFE != null;
        if(nodeAddr == NULL) throw new NullPointerException("Trying to get address of NULL address");
        return UNSAFE.getLong(nodeAddr);
    }

    // Dado um endereço de memória de um nó, e um long que representa o xor de 2 endereços quarda o novo valor de xor.
    public static void set_prev_next_addr(long nodeAddr, long prev_next_addr) throws NullPointerException
    {
        assert UNSAFE != null;
        if(nodeAddr == NULL) throw new NullPointerException("Trying to set address of NULL address");
        UNSAFE.putLong(null,nodeAddr,prev_next_addr);
    }

    // Dado um endereço de memória de um nó, devolve o item que foi guardado nesse nó.
    @SuppressWarnings("unchecked")
    public static <T> T get_item(long nodeAddr) throws NullPointerException
    {
        assert UNSAFE != null;
        if(nodeAddr == NULL) throw new NullPointerException("Trying to get item of NULL address");

        Object[] array = new Object[] {null};
        long itemAddr = UNSAFE.getLong(nodeAddr+ITEM_OFFSET);

        long baseOffset = UNSAFE.arrayBaseOffset(Object[].class);
        UNSAFE.putLong(array, baseOffset, itemAddr);
        return (T) array[0];
    }

    // Dado um endereço de memória de um nó, e um item do tipo genérico T, guarda o valor do item no nó apagando o valor
    // anterior que lá estava. Na realidade o que é guardado é o endereço de memória do item recebido.
    public static <T> void set_item(long nodeAddr, T item) throws NullPointerException
    {
        assert UNSAFE != null;
        if(nodeAddr == NULL) throw new NullPointerException("Trying to set item of NULL address");
        long itemAddr = UnsafeHelper.toAddress(item);
        UNSAFE.putLong(null,nodeAddr+ITEM_OFFSET,itemAddr);
    }
}


