import java.util.*;
import aed.sorting.RecursiveStringSort;
import aed.sorting.Sort;

public class main
{
    public static void main(String[] args)
    {
        //String[] strsArray = {"Hello", "String2", "aHello", "Space Test"};
        //String[] strsArray = {"b", "C", "B", "c", "D", "A", "a", "aae", "aac", "aag", "aad", "aaab", "aaa", "aaaa", "aaf", "aab", "aaa"};
        // String[] strsArray = {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa", "aaaaaaaaaaa", "aaaaaaaaaaaa", "aaaaaaaaaaaaa",
        //         "aaaaaaaaaaaaaa", "aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaa"};
        //String[] strsArray = {"\0", "ʜǚƇο˝ƊŢ>μȴƑʮƄǁœŹ", "\"͎ȔȽǍûʃ", "\"Σɼ͒ˢǵ΃ǫ±̆įʠʊȡ̐¸ơ", "&ǮȌǠ!ήǑǟ"};
        //String[] strsArray = {"\0", "ʜǚƇο˝ƊŢ>μȴƑʮƄǁœŹ", "\"͎ȔȽǍûʃ", "\"Σɼ͒ˢǵ΃ǫ±̆įʠʊȡ̐¸ơ", "&ǮȌǠ!ήǑǟ", Character.toString(127),
        //        " ", "  ", Character.toString(0)};
        //String[] strsArray = {"Zeus", "alameda", "alfredo", "chouriço", "joão", "maria", "ola", "ola", "teste", "único"};
        //RecursiveStringSort.insertionSort(Arrays.asList(strsArray));
        //RecursiveStringSort.fasterSort2(strsArray);
        String[] strsArray = generate(1000);
        String[] strs0 = strsArray.clone();
        String[] strs1 = strsArray.clone();
        RecursiveStringSort.fasterSortEnglish(strs0);
        RecursiveStringSort.fasterSort2(strs1);
        // for (String s : strsArray)
        //     System.out.println(s);
        System.out.println("recursive isSorted: " + Sort.isSorted(strs0));
        System.out.println("faster isSorted: " + Sort.isSorted(strs1));
    }

    private static String[] generate(Integer size)
    {
        Random R = new Random();
        final int maxLen = 1000;

        String[] strings = new String[size];
        for (int i = 0; i < size; i++)
        {
            int len = 1 + R.nextInt(maxLen);
            StringBuilder buffer = new StringBuilder(len);
            for (int j = 0; j < len; j++)
                buffer.append((char) R.nextInt(1000));
            strings[i] = buffer.toString();
        }
        return strings;
    }
}
