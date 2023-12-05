package aed.tables;


public class ForgettingCuckooHashTable<Key,Value> implements ISymbolTable<Key,Value> {


    private static int[] primesTable0 = {
            7, 17, 37, 79, 163, 331,
            673, 1361, 2729, 5471, 10949,
            21911, 43853, 87719, 175447, 350899,
            701819, 1403641, 2807303, 5614657,
            11229331, 22458671, 44917381, 89834777, 179669557
    };

    private static int[] primesTable1 = {
            11, 19, 41, 83, 167, 337,
            677, 1367, 2731, 5477, 10957,
            21929, 43867, 87721, 175453, 350941,
            701837, 1403651, 2807323, 5614673,
            11229341, 22458677, 44917399, 89834821, 179669563
    };


    @SuppressWarnings("unchecked")
    public ForgettingCuckooHashTable(int primeIndex)
    {
        //TODO: implement
    }

    public ForgettingCuckooHashTable()
    {
       //TODO: implement
    }

    public int size()
    {
        //TODO: implement
		return 0;
    }

    @Override
    public boolean isEmpty() {
        //TODO: implement
		return true;
    }

    public int getCapacity()
    {
        //TODO: implement
		return 0;
    }

    public float getLoadFactor()
    {
        //TODO: implement
		return 0.0f;
    }


    public boolean containsKey(Key k)
    {
       
		//TODO: implement
        return false;
    }

    public Value get(Key k)
    {
        //TODO: implement
		return null;
    }

    public void put(Key k, Value v)
    {
        //TODO: implement
    }
    

    public void delete(Key k)
    {
        //TODO: implement
    }

    public Iterable<Key> keys() {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<Key>,Iterable<Key>
    {
        //TODO: implement

        KeyIterator()
        {
            //TODO: implement
        }

        public boolean hasNext() {
			//TODO: implement
			return false;
        }

        public Key next() {
            return null;
			//TODO: implement
        }

        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn't support removal");
        }

        @Override
        public Iterator<Key> iterator() {
            return this;
        }
    }



    public void setSwapLogging(boolean state)
    {
		//TODO: implement
    }

    public float getSwapAverage()
    {
        //TODO: implement
		return 0.0f;
    }

    public float getSwapVariation()
    {
        //TODO: implement
		return 0.0f;
    }

    public void advanceTime(int hours)
    {
        //TODO: implement
    }


}
