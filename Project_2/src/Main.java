import java.util.*;
import aed.collections.*;

public class Main
{
    public static void main(String[] args)
    {
        StingyList<Integer> list = new StingyList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.clear();
        list.print();
        list.add(2);
        list.print();
    }
}
