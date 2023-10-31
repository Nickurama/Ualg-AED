import java.util.*;
import aed.collections.*;

public class Main
{
    public static void main(String[] args)
    {
        StingyList<String> list = new StingyList<String>();
        list.add("0");
        list.add("1");
        list.add("2");
        //list.print();
        // StingyList<String> list2 = list.reversed();
        // System.out.println(list.isEmpty());
        // list.clear();
        // System.out.println(list.isEmpty());
        // try
        // {
        //     list.addAt(-1, "X");
        // } catch (Exception e)
        // {

        // }
        // list.addAt(0, "X");
        // list.print();
        // list.clear();
        // Object[] array = list.toArray();
        // for (int i = 0; i < array.length; i++)
        //     System.out.println(array[i]);
        // list.print();

        // list.clear();
        // Object[] array = list.toArray();
        // for (int i = 0; i < array.length; i++)
        //     System.out.println(array[i]);

        // ArrayList<Integer> al = new ArrayList<Integer>();
        // Iterator<Integer> it = al.iterator();
        // Iterator<String> it2 = list.iterator();

        // System.out.println("---");
        // list.print();
        // System.out.println("---");
        // list.remove();
        // list.print();
        // System.out.println("---");
        // list.remove();
        // list.print();
        // System.out.println("---");
        // list.remove();
        // list.print();
        // System.out.println("---");
        // list.remove();

        StingyList<Integer> listInts = new StingyList<Integer>();
        listInts.add(1);
        listInts.add(2);
        listInts.add(3);
        listInts.add(4);
        listInts.add(5);
        listInts.add(6);
        listInts.add(7);
        listInts.add(8);
        listInts.add(9);
        listInts.add(10);


        try
        {
            listInts.addAt(11, 0);
        } catch (Exception e)
        {

        }
        listInts.addAt(0, 0);
        System.out.println("IsEmpty: " + listInts.isEmpty());
        System.out.println("Size: " + listInts.size());
        Object[] array = listInts.toArray();
        for (int i = 0; i < array.length; i++)
            System.out.println(array[i]);
        listInts.print();
    }
}
