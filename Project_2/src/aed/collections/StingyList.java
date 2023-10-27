package aed.collections;

import java.util.Iterator;

public class StingyList<T> implements Iterable<T> {

    //representamos null como o long 0L.
    private static final long NULL = 0L;

    private int size;
    private long first;
    private long last;

    public StingyList()
    {
        //TODO: Implement
    }

    // Embora não seja obrigatório, aconselho-vos a implementar estes 3 métodos seguintes, pois o código da StingyList pode
    // ser implementado de forma simples com base nestes métodos.

    // Dado um long que representa o endereço do nó atual (node), e um segundo long que representa o endereço do nó
    // de onde viemos numa sequência antes de chegar ao nó atual, devolve uma referência para o nó onde queremos ir a seguir.
    // Este método funciona quer estejamos a "viajar" da esquerda para a direita, ou da direita para a esquerda,
    // como podemos ver no seguinte diagrama:
    //       from -- to --> Node -- to --> beyond
    //       beyond <-- to -- Node <-- to -- from
    //
    long getBeyond(long node, long fromAddr)
    {
        //Optional: Implement
        return 0;
    }

    // Atualiza uma das referências do nó (pode ser usado para atualizar o previous ou o next).
    // Recebe como argumento um endereço para o nó, um endereço para a ligação que queremos atualizar (previous ou next),
    // e o novo endereço a usar. Se passármos o previous, este método atualiza apenas o ponteiro para o previous
    // mantendo o ponteiro para o next, e vice-versa.
    static void updateNodeReference(long node, long oldAddr, long newAddr)
    {
        //Optional: Implement
    }

    //Atualiza ambas as referências do nó em simultâneo (previous e next). Útil quando queremos atualizar ambas,
    //e já temos as referências para o novo previous e o novo next. Recebe como argumentos o novo nó previous para o
    // qual queremos apontar, e o novo nó next para o qual queremos apontar.
    void updateBothNodeReferences(long node, long prevAddr, long nextAddr)
    {
        //Optional: Implement
    }


    //Stingy List Methods

    public void add(T item)
    {
        //TODO: Implement
    }

    public T remove()
    {
        //TODO: Implement
        //DO NOT FORGET to FREE memory
        return null;
    }

    public T get()
    {
        //TODO: Implement
        return null;
    }

    public T get(int i)
    {
        //TODO: Implement
        return null;
    }

    public T getSlow(int i)
    {
        //TODO: Implement
        return null;
    }

    public void addAt(int i, T item)
    {
        //TODO: Implement
    }

    public T removeAt(int i)
    {
        //TODO: Implement
        //DO NOT FORGET to FREE memory
        return null;
    }

    public void reverse()
    {
        //TODO: Implement
    }

    public StingyList<T> reversed()
    {
        //TODO: Implement
        return null;
    }

    public void clear()
    {
        //TODO: Implement
        //DO NOT FORGET to FREE memory
        return null;
    }

    public boolean isEmpty()
    {
        //TODO: Implement
        return false;
    }

    public int Size()
    {
        //TODO: Implement
        return 0;
    }

    public Object[] toArray()
    {
        //TODO: Implement
        return null;
    }

    @Override
    public Iterator<T> iterator()
    {
        //TODO: Implement
        return null;
    }
}
