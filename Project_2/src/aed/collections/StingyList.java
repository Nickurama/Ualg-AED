package aed.collections;

import java.util.Iterator;

public class StingyList<T> implements Iterable<T>
{
    public class StingyListIterator implements Iterator<T>
    {
        // An iterator of this class is by definition faulty.
        // To get the next or the previous nodes from the current node without calculating everything
        // from scratch, the iterator must "stop" on whatever it is iterating.
        // If the iterator did not save the previous or the next node, it could not move at all.
        // Thus if the list changes the previous or the next, then the iterator will give a wrong value.
        // This cannot be prevented with this implementation of a list
        AdjacentAddresses adj;

        public StingyListIterator()
        {
            adj = new AdjacentAddresses(first);
        }

        public boolean hasNext()
        {
            return adj.hasNext();
        }

        public T next()
        {
            T value = adj.get();
            adj.next();
            return value;
        }
    }
    private class AdjacentAddressesSingleton
    {
        private AdjacentAddresses adjacentAddresses;

        private AdjacentAddressesSingleton()
        {
            adjacentAddresses = null;
        }

        public AdjacentAddresses getInstance(long startAddr)
        {
            if (adjacentAddresses == null)
                adjacentAddresses = new AdjacentAddresses(startAddr);
            else
                adjacentAddresses.reset(startAddr);

            return adjacentAddresses;
        }
    }
    private class AdjacentAddresses
    {
        private long previous;
        private long current;
        private long next;
        private boolean isReversed;

        public AdjacentAddresses(long startAddr) throws IndexOutOfBoundsException
        {
            reset(startAddr);
        }

        public void reset(long startAddr)
        {
            this.previous = NULL;
            this.current = NULL;
            this.next = startAddr;
            this.isReversed = startAddr == last;
            next();
        }

        public long next()// throws IndexOutOfBoundsException
        {
            // if (next == NULL)
            //     throw new IndexOutOfBoundsException("The next node is null");
            previous = current;
            current = next;
            try
            {
                next = getNextAddr(current, previous);
            } catch (Exception e)
            {
                next = NULL;
            }
            return current;
        }

        public boolean hasNext()
        {
            return current != NULL;
        }

        public long previous()// throws IndexOutOfBoundsException
        {
            // if (previous == NULL)
            //     throw new IndexOutOfBoundsException("The previous node is null");
            next = current;
            current = previous;
            try
            {
                previous = getNextAddr(current, next);
            } catch (Exception e)
            {
                previous = NULL;
            }

            return current;
        }

        public long getAddress()
        {
            return this.current;
        }

        public void reverse()
        {
            long cache = previous;
            previous = next;
            next = cache;

            isReversed = !isReversed;
        }

        public T remove() throws IndexOutOfBoundsException
        {
            if (current == NULL)
                throw new IndexOutOfBoundsException("No node at current index");

            T result = get();
            linkPreviousAndNext();
            long obsoleteCurrent = current;
            deleteCurrent();
            adjust(obsoleteCurrent);
            size--;
            return result;
        }

        private T get()
        {
            return UNode.get_item(current);
        }

        private void linkPreviousAndNext()
        {
            if (previous != NULL)
                updateNodeReference(previous, current, next);
            if (next != NULL)
                updateNodeReference(next, current, previous);
        }

        private void deleteCurrent()
        {
            UNode.free_node(current);
            current = NULL;
        }

        private void adjust(long obsoleteCurrent) throws IndexOutOfBoundsException
        {
            if (next == NULL && previous == NULL) // O O O
            {
                updateFirstAndLast(obsoleteCurrent, NULL);
            } else if (next == NULL) // X O O -> endpoint - update first/last
            {
                updateFirstAndLast(obsoleteCurrent, previous);
                previous();
            } else if (previous == NULL) // O O X -> endpoint - update first/last
            {
                updateFirstAndLast(obsoleteCurrent, next);
                next();
            } else // X O X -> O X X
            {
                current = previous;
                next();
            }
        }

        private void updateFirstAndLast(long obsoleteCurrent, long newAddr)
        {
            if (obsoleteCurrent == first)
                first = newAddr;
            if (obsoleteCurrent == last)
                last = newAddr;
        }

        public void add(T value)
        {
            boolean hasReversed = false;
            if (this.isReversed) // adding only works from left -> right because of assimetry (adding between previous and current)
            {
                hasReversed = true;
                reverse();
            }
            long newAddr = UNode.create_node(value, previous, current);
            linkPreviousAndCurrentTo(newAddr);
            updateFirstAndLastForAdd(newAddr);
            next = current;
            current = newAddr;
            size++;
            if (hasReversed)
                reverse();
        }

        private void linkPreviousAndCurrentTo(long addr)
        {
            if (previous != NULL)
                updateNodeReference(previous, current, addr);
            if (current != NULL)
                updateNodeReference(current, previous, addr);
        }

        private void updateFirstAndLastForAdd(long newAddr)
        {
            if (previous == NULL) // then current might be first/last
                updateFirstOrLast(current, newAddr);
            if (current == NULL) // then previous might be first/last
                updateFirstOrLast(previous, newAddr);
        }

        private void updateFirstOrLast(long obsoleteCurrent, long newAddr)
        {
            if (obsoleteCurrent == first)
                first = newAddr;
            else if (obsoleteCurrent == last)
                last = newAddr;
        }
    }

    private static final long NULL = 0L;

    private int size;
    private long first;
    private long last;
    AdjacentAddressesSingleton adjacentSingleton;

    public StingyList()
    {
        this.size = 0;
        this.first = NULL;
        this.last = NULL;
        adjacentSingleton = new AdjacentAddressesSingleton();
    }

    public StingyList(long first, long last, int size)
    {
        this.size = size;
        this.first = first;
        this.last = last;
        adjacentSingleton = new AdjacentAddressesSingleton();
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
    private long getNextAddr(long currentAddr, long previousAddr)
    {
        return UNode.get_prev_next_addr(currentAddr) ^ previousAddr;
    }

    // Atualiza uma das referências do nó (pode ser usado para atualizar o previous ou o next).
    // Recebe como argumento um endereço para o nó, um endereço para a ligação que queremos atualizar (previous ou next),
    // e o novo endereço a usar. Se passármos o previous, este método atualiza apenas o ponteiro para o previous
    // mantendo o ponteiro para o next, e vice-versa.
    private static void updateNodeReference(long nodeAddr, long oldAddr, long newAddr)
    {
        long pointers = UNode.get_prev_next_addr(nodeAddr);
        long opposite = pointers ^ oldAddr; // gets the opposite (old: next -> previous) (old: previous -> next) 
        UNode.set_prev_next_addr(nodeAddr, opposite ^ newAddr); // gets a new addres with previous/next + newAddr
    }

    //Atualiza ambas as referências do nó em simultâneo (previous e next). Útil quando queremos atualizar ambas,
    //e já temos as referências para o novo previous e o novo next. Recebe como argumentos o novo nó previous para o
    // qual queremos apontar, e o novo nó next para o qual queremos apontar.
    private void updateBothNodeReferences(long nodeAddr, long prevAddr, long nextAddr)
    {
        UNode.set_prev_next_addr(nodeAddr, prevAddr ^ nextAddr);
    }


    //Stingy List Methods

    public void add(T item) throws IllegalArgumentException
    {
        if (item == null)
            throw new IllegalArgumentException("Argument is null");

        long nodeAddr;
        if (size == 0)
        {
            nodeAddr = UNode.create_node(item, NULL, NULL);
            first = nodeAddr;
        } else
        {
            nodeAddr = UNode.create_node(item, last, NULL);
            updateNodeReference(last, NULL, nodeAddr);
        }
        last = nodeAddr;
        this.size++;
    }

    public T remove() throws IndexOutOfBoundsException
    {
        try
        {
            return removeAt(this.size - 1);
        } catch (IndexOutOfBoundsException e)
        {
            throw new IndexOutOfBoundsException("List is empty: " + e.getMessage());
        }
    }

    public T get() throws IndexOutOfBoundsException
    {
        if (isEmpty())
            throw new IndexOutOfBoundsException("List is empty.");
        return get(size - 1);
    }

    public T get(int i) throws IndexOutOfBoundsException
    {
        throwIfInvalidIndex(i);
        return getAdjacentAddresses(i).get();
    }

    private AdjacentAddresses getAdjacentAddresses(int i) throws IndexOutOfBoundsException
    {
        AdjacentAddresses adjacent = setupAdressesFromIndex(i);
        int iterations = getIterationsFromIndex(i);

        while (iterations-- > 0)
            adjacent.next();

        return adjacent;
    }

    private AdjacentAddresses setupAdressesFromIndex(int i) throws IndexOutOfBoundsException
    {
        //return isOnUpperHalf(i) ? new AdjacentAddresses(last) : new AdjacentAddresses(first);
        return isOnUpperHalf(i) ? adjacentSingleton.getInstance(last) : adjacentSingleton.getInstance(first);
    }

    private boolean isOnUpperHalf(int i)
    {
        return i + 1 > this.size / 2;
    }

    private int getIterationsFromIndex(int i)
    {
        return isOnUpperHalf(i) ? this.size - (i + 1) : i;
    }

    private void throwIfInvalidIndex(int i) throws IndexOutOfBoundsException
    {
        if (i < 0 || i >= this.size)
            throw new IndexOutOfBoundsException("Index " + i + " out of range: " + (this.size - 1));
    }

    public T getSlow(int i) throws IndexOutOfBoundsException
    {
        throwIfInvalidIndex(i);

        AdjacentAddresses adjacent = new AdjacentAddresses(first);
        while (i-- > 0)
            adjacent.next();
        return adjacent.get();
    }

    public void addAt(int i, T item) throws IndexOutOfBoundsException
    {
        if (i == this.size)
            add(item);
        else
        {
            throwIfInvalidIndex(i);
            AdjacentAddresses a = getAdjacentAddresses(i);
            a.add(item);
        }
    }

    public T removeAt(int i) throws IndexOutOfBoundsException
    {
        throwIfInvalidIndex(i);
        try
        {
            return getAdjacentAddresses(i).remove();
        } catch (IndexOutOfBoundsException e)
        {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    public void reverse()
    {
        long cache = this.first;
        this.first = this.last;
        this.last = cache;
    }

    public StingyList<T> reversed()
    {
        return new StingyList<T>(this.last, this.first, this.size);
    }

    public void clear()
    {
        if (this.size == 0)
            return;

        AdjacentAddresses adjacent = new AdjacentAddresses(this.first);
        while (adjacent.hasNext())
            adjacent.remove();
    }

    public boolean isEmpty()
    {
        return this.size == 0;
    }

    public int size()
    {
        return this.size;
    }

    public Object[] toArray()
    {
        Object[] result = new Object[this.size];
        if (this.size == 0)
            return result;

        Iterator<T> it = iterator();
        for (int i = 0; i < this.size; i++)
            result[i] = it.next();
        return result;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new StingyListIterator();
    }

    public void print()
    {
        for (T item : this)
            System.out.println(item);
    }
}
