import java.util.*;
import aed.collections.Cordel;
import aed.sorting.RecursiveStringSort;
import aed.sorting.Sort;

public class main
{
    public static void main(String[] args)
    {
        Cordel cord0 = new Cordel("Ola, ");
        Cordel cord1 = new Cordel("isto e");
        Cordel cord2 = new Cordel(" um ");
        Cordel cord3 = new Cordel("cor");
        Cordel cord4 = new Cordel("del!");

        cord0 = cord0.append("isto e").append(" um ").append("cor").append("del!");
        //cord0.println();

        //System.out.println(cord0.length());

        cord0 = new Cordel("Ola, ");

        Cordel layer2_1 = new Cordel(cord1, cord2);
        Cordel layer1_0 = new Cordel(cord0, layer2_1);
        Cordel layer1_1 = new Cordel(cord3, cord4);
        Cordel layer0 = new Cordel(layer1_0, layer1_1);
        // layer0.println();
        // layer0.printInfo();

        // for (int i = 0; i < layer0.length(); i++)
        //     System.out.print(layer0.charAt(i));

        // Cordel[] splitTest = layer0.split(22);
        // splitTest[0].println();
        // splitTest[1].println();

        // System.out.println(layer0.length());
        // String cocat = "Ola, " + "isto e" + " um " + "cor" + "del!";
        // System.out.println(cocat.length());

        // layer0.println();
        // layer0 = layer0.delete(0, 22);
        // layer0.println();

        // for (String s : layer0)
        //     System.out.println(s);

        layer0.println();
        System.out.print("\"");
        layer0.print(15, 21);
        System.out.print("\"");
    }
}
