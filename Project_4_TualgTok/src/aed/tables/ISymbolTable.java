package aed.tables;

public interface ISymbolTable<Key,Value>
{
    //puts a new value in the Symbol Table, associated to the received K. The value will be stored with the Key,
    //and any future searches must be made using the key. There are no duplicate keys in the table. If a new
    //value is added using a previously existing key, the new value received will replace the previous value
    void put(Key k, Value v);

    //searchs the Symbol Table for the given Key, and returns the Value associated with the key, if the key exists.
    //If the key is not found in the symbol table, this method returns null.
    Value get(Key k);

    //deletes a key, and its corresponding value from the Symbol Table
    void delete(Key k);

    //verifies if a given key is currently stored in the Symbol Table. Returns true if the key is found,
    //and false otherwise
    boolean containsKey(Key k);

    //returns true if there are not key/value currently stored in the Symbol Table, and false otherwise
    boolean isEmpty();

    //returns the number of keys stored in the Symbol Table
    int size();

    //returns an Iterable object (usually a collection) that can be used to iterate through all keys currently
    //stored in the Symbol Table. There are no guarantees about the order in which the iterator will traverse
    //the keys
    Iterable<Key> keys();
}
