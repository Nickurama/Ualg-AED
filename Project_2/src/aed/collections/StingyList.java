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

        public long next()
        {
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

        public boolean isOutOfBounds()
        {
            return current == NULL;
        }

        public long previous()
        {
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

    private long getNextAddr(long currentAddr, long previousAddr)
    {
        return UNode.get_prev_next_addr(currentAddr) ^ previousAddr;
    }

    private static void updateNodeReference(long nodeAddr, long oldAddr, long newAddr)
    {
        long pointers = UNode.get_prev_next_addr(nodeAddr);
        long opposite = pointers ^ oldAddr; // gets the opposite (old: next -> previous) (old: previous -> next) 
        UNode.set_prev_next_addr(nodeAddr, opposite ^ newAddr); // gets a new addres with previous/next + newAddr
    }

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

    private void throwIfEmpty() throws IndexOutOfBoundsException
    {
        if (isEmpty())
            throw new IndexOutOfBoundsException("List is empty.");
    }

    public T get() throws IndexOutOfBoundsException
    {
        throwIfEmpty();
        return get(size - 1);
    }

    public T get(int i) throws IndexOutOfBoundsException
    {
        throwIfInvalidIndex(i);
        return getAdjacentAddresses(i).get();
    }

    public T getSlow(int i) throws IndexOutOfBoundsException
    {
        throwIfInvalidIndex(i);

        AdjacentAddresses adjacent = adjacentSingleton.getInstance(first);
        while (i-- > 0)
            adjacent.next();
        return adjacent.get();
    }

    private AdjacentAddresses getAdjacentAddresses(int i) throws IndexOutOfBoundsException
    {
        AdjacentAddresses adjacent = setupAdressesFromIndex(i);
        int iterations = getIterationsFromIndex(i);

        while (iterations-- > 0)
            adjacent.next();
        if (adjacent.isOutOfBounds())
            throw new IndexOutOfBoundsException("Got out of bounds.");

        return adjacent;
    }

    private AdjacentAddresses setupAdressesFromIndex(int i) throws IndexOutOfBoundsException
    {
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

    public void addAt(int i, T item) throws IndexOutOfBoundsException
    {
        if (i == this.size)
            add(item);
        else
        {
            throwIfInvalidIndex(i);
            getAdjacentAddresses(i).add(item);
        }
    }

    public T remove() throws IndexOutOfBoundsException
    {
        throwIfEmpty();
        return removeAt(this.size - 1);
    }

    public T removeAt(int i) throws IndexOutOfBoundsException
    {
        throwIfInvalidIndex(i);
        return getAdjacentAddresses(i).remove();
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

        AdjacentAddresses adjacent = adjacentSingleton.getInstance(this.first);
        while (adjacent.hasNext())
            adjacent.remove();
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

        int i = 0;
        for (T item : this)
            result[i++] = item;
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
