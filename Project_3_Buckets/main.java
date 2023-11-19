import java.util.*;
import aed.sorting.RecursiveStringSort;

public class main
{
    public static void main(String[] args)
    {
        //String[] strsArray = {"Hello", "String2", "aHello", "Space Test"};
        //String[] strsArray = {"b", "C", "B", "c", "D", "A", "a", "aae", "aac", "aag", "aad", "aaab", "aaa", "aaaa", "aaf", "aab", "aaa"};
        //String[] strsArray = {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa", "aaaaaaaaaaa", "aaaaaaaaaaaa", "aaaaaaaaaaaaa",
        //        "aaaaaaaaaaaaaa", "aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaa"};
        String[] strsArray = {"Zeus", "alameda", "alfredo", "chouriço", "joão", "maria", "ola", "ola", "teste", "único"};
        //RecursiveStringSort.insertionSort(Arrays.asList(strsArray));
        RecursiveStringSort.sort(strsArray);
        for (String s : strsArray)
            System.out.println(s);
    }
}
