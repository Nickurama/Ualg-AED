import java.util.*;
import aed.tables.ForgettingCuckooHashTable;
import aed.tables.ForgettingCuckooHashTable;

import java.util.*;

public class CuckooHashTableTests
{

    private static final int LARGE = 150000;
    private static final int HUGE = 500000;
    private static final String info = "TEST INFO: ";


    //todas estas chaves produzem o mesmo hashcode em Java
    //podemos trocar qualquer Aa por um BB, e iremos obter o mesmo hashcode
    private static final String A1 = "AaAaAaAa";
    private static final String A2 = "AaAaAaBB";
    private static final String A3 = "AaAaBBAa";
    private static final String A4 = "AaAaBBBB";
    private static final String A5 = "AaBBAaAa";
    private static final String A6 = "AaBBAaBB";
    private static final String A7 = "AaBBBBAa";
    private static final String A8 = "AaBBBBBB";
    private static final String A9 = "BBAaAaAa";
    private static final String A10 = "BBAaAaBB";
    private static final String A11 = "BBBBBBBB";


    //creates a random generator with a specific seed
    private static final Random pseudoRandom = new Random(5789);

    public static List<Runnable> getAllTests()
    {
        ArrayList<Runnable> tests = new ArrayList<Runnable>();
        tests.add(CuckooHashTableTests::test1);
        tests.add(CuckooHashTableTests::test2);
        tests.add(CuckooHashTableTests::test3);
        tests.add(CuckooHashTableTests::test4);
        tests.add(CuckooHashTableTests::test5);
        tests.add(CuckooHashTableTests::test6);
        tests.add(CuckooHashTableTests::test7);
        tests.add(CuckooHashTableTests::test8);
        tests.add(CuckooHashTableTests::test9);
        tests.add(CuckooHashTableTests::test10);
        tests.add(CuckooHashTableTests::test11);
        tests.add(CuckooHashTableTests::test12);
        tests.add(CuckooHashTableTests::test13);
        tests.add(CuckooHashTableTests::test14);
        tests.add(CuckooHashTableTests::test15);
        tests.add(CuckooHashTableTests::test16);
        tests.add(CuckooHashTableTests::test17);

        return tests;
    }

    public static void test1()
    {
        System.out.println(info + "Testes simples com uma tabela pequena");
        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<String, Integer>();

        hashTable.put(new String("ABC"), 10);
        hashTable.put("Xpto", 189);
        hashTable.put("hello", 2746);
        hashTable.put("102", 102);
        hashTable.put("five", 5);

        System.out.println("Size " + hashTable.size());
        System.out.println("isEmpty: " + hashTable.isEmpty());
        System.out.println("Capacity: " + hashTable.getCapacity());
        System.out.println("LoadFactor: " + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()));

        System.out.println("Contains key \"ABC\": " + hashTable.containsKey(new String("ABC")));
        System.out.println("Contains key \"five\": " + hashTable.containsKey("five"));
        System.out.println("Contains key \"abc\": " + hashTable.containsKey("abc"));
        System.out.println("get \"ABC\": " + hashTable.get(new String("ABC")));
        System.out.println("get \"five\": " + hashTable.get("five"));
        System.out.println("get \"102\": " + hashTable.get("102"));
        System.out.println("get \"102\": " + hashTable.get("102"));
        System.out.println("get \"103\": " + hashTable.get("103"));
    }

    public static void test2()
    {
        //Teste não disponibilizado
    }

    public static void test3()
    {
        System.out.println(info + "testando chaves com o mesmo hashcode");
        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<String, Integer>();
        hashTable.put(new String("Aa"), 10);
        hashTable.put("BB", 189);
        hashTable.put(A1, 2746);
        hashTable.put(A2, 102);

        try
        {
            hashTable.put(A3, 181);
        } catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException thrown: true");
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        System.out.println("Contains key \"Aa\": " + hashTable.containsKey("Aa"));
        System.out.println("Contains key \"BB\": " + hashTable.containsKey("BB"));
        System.out.println("Contains key \"" + A1 + "\": " + hashTable.get(A1));
        System.out.println("Contains key \"" + A3 + "\": " + hashTable.get(A3));
        System.out.println("Contains key \"BBAaAaAa\": " + hashTable.containsKey("BBAaAaAa"));
        System.out.println("get \"Aa\": " + hashTable.get("Aa"));
        System.out.println("get \"BB\": " + hashTable.get("BB"));
        System.out.println("get \"" + A1 + "\": " + hashTable.get(A1));
        System.out.println("get \"" + A2 + "\": " + hashTable.get(A2));
        System.out.println("get \"" + A3 + "\": " + hashTable.get(A3));
    }

    public static void test4()
    {
        //Teste não disponibilizado
    }

    public static void test5()
    {
        System.out.println(info + "Parecido ao teste anterior, mas agora começamos de uma tabela pequena e temos de fazer resize");
        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<String, Integer>(0);
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        String[] strings = new String[1500];
        for (int i = 0; i < 1500; i++)
        {
            strings[i] = generateRandomString(7);
        }

        for (int i = 0; i < 500; i++)
        {
            hashTable.put(strings[i], pseudoRandom.nextInt(1500));
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        for (int i = 500; i < 1000; i++)
        {
            hashTable.put(strings[i], pseudoRandom.nextInt(1500));
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        for (int i = 1000; i < 1500; i++)
        {
            hashTable.put(strings[i], pseudoRandom.nextInt(1500));
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        System.out.println("get " + strings[1] + ": " + hashTable.get(strings[1]));
        for (int i = 0; i < 1500; i += 250)
        {
            System.out.println("get " + strings[i] + ": " + hashTable.get(strings[i]));
        }
        String rand = generateRandomString(7);
        System.out.println("get " + rand + ": " + hashTable.get(rand));
    }


    public static void test6()
    {
        //Teste não disponibilizado
    }

    public static void test7()
    {
        System.out.println(info + "Testes simples à funcionalidade de apagar");
        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<String, Integer>();

        hashTable.put(new String("ABC"), 10);
        hashTable.put("Xpto", 189);
        hashTable.put("hello", 2746);
        hashTable.put("102", 102);
        hashTable.put("five", 5);
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        System.out.println("get \"five\": " + hashTable.get("five"));
        System.out.println("get \"Xpto\": " + hashTable.get("Xpto"));
        System.out.println("get \"102\": " + hashTable.get("102"));
        System.out.println("get \"hello\": " + hashTable.get("hello"));

        hashTable.delete(new String("ABC"));
        hashTable.delete("Xpto");
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        hashTable.delete("hello");
        hashTable.delete("ah ah");
        hashTable.delete("yuupiii");
        hashTable.delete("very good");
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        System.out.println("Contains key \"ABC\": " + hashTable.containsKey(new String("ABC")));
        System.out.println("Contains key \"five\": " + hashTable.containsKey("five"));
        System.out.println("Contains key \"Xpto\": " + hashTable.containsKey("Xpto"));
        System.out.println("Contains key \"hello\": " + hashTable.containsKey("hello"));
        System.out.println("get \"five\": " + hashTable.get("five"));
        System.out.println("get \"Xpto\": " + hashTable.get("Xpto"));
        System.out.println("get \"102\": " + hashTable.get("102"));
        System.out.println("get \"hello\": " + hashTable.get("hello"));
    }

    public static void test8()
    {
        //Teste não disponibilizado
    }

    public static void test9()
    {
        System.out.println(info + "Teste ao redimensionamento para baixo");
        ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<Integer, Integer>(0);

        for (int i = 0; i < 500; i++)
        {
            hashTable.put(i, pseudoRandom.nextInt(1500));
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        for (int i = 500; i < 1000; i++)
        {
            hashTable.put(i, pseudoRandom.nextInt(1500));
        }

        for (int i = 1000; i < 1500; i++)
        {
            hashTable.put(i, pseudoRandom.nextInt(1500));
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        for (int i = 0; i < 1500; i += 250)
        {
            System.out.println("get " + i + ": " + hashTable.get(i));
        }

        for (int i = 0; i < 500; i++)
        {
            hashTable.delete(i);
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        for (int i = 500; i < 750; i++)
        {
            hashTable.delete(i);
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        for (int i = 750; i < 1000; i++)
        {
            hashTable.delete(i);
        }
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        for (int i = 1000; i < 1250; i++)
        {
            hashTable.delete(i);
        }
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");

        System.out.println("get " + 1000 + ": " + hashTable.get(1000));
        System.out.println("get " + 1300 + ": " + hashTable.get(1300));
        for (int i = 1250; i < 1450; i++)
        {
            hashTable.delete(i);
        }
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        for (int i = 1450; i < 1500; i++)
        {
            hashTable.delete(i);
        }
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
    }



    private static boolean equivalentSets(Iterable<String> it, String[] keys)
    {
        int iteratorSize = 0;
        //isto não é eficiente, mas não quero saber, é só usado para os testes
        List<String> aKeys = new ArrayList<String>();
        for (String s : keys)
        {
            aKeys.add(s);
        }

        //isto não é eficiente, mas não quero saber, é só usado para os testes
        for (String s : it)
        {
            iteratorSize++;
            if (!aKeys.contains(s))
                return false;
            aKeys.remove(s);
        }

        return iteratorSize == keys.length;
    }

    public static void test10()
    {
        //Teste não disponibilizado
    }



    public static void test11()
    {
        System.out.println(info + "Testando tudo junto, insert, delete, insert e iterador");
        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<String, Integer>();

        String[] keysExamples = new String[100];
        String[] keysExamplesToDelete = new String[100];
        String[] keysExamplesAll = new String[202];
        String randomString;

        int i = 0;
        for (; i < 100; i++)
        {

            randomString = generateRandomString(7);
            hashTable.put(randomString, pseudoRandom.nextInt(100));
            keysExamples[i] = randomString;
            keysExamplesAll[i] = randomString;
        }

        for (int j = 0; j < 100; i++, j++)
        {

            randomString = generateRandomString(7);
            hashTable.put(randomString, pseudoRandom.nextInt(100));
            keysExamplesToDelete[j] = randomString;
            keysExamplesAll[i] = randomString;
        }

        hashTable.put(A1, 64);
        hashTable.put(A2, 123);
        keysExamplesAll[200] = A1;
        keysExamplesAll[201] = A2;


        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        System.out.println("equal sets (order does not matter): " + equivalentSets(hashTable.keys(), keysExamplesAll));
        hashTable.delete(A1);
        hashTable.delete(A2);

        for (i = 0; i < 100; i++)
        {
            hashTable.delete(keysExamplesToDelete[i]);
        }

        hashTable.put(keysExamples[1], 1234);
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        hashTable.delete(keysExamples[10]);
        hashTable.delete(keysExamples[24]);
        hashTable.put(keysExamples[10], 2837987);
        hashTable.put(keysExamples[24], 2837987);
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        System.out.println(info + " comparing iterator with the following set:");
        printArray(keysExamples, 100);
        System.out.println("equal sets (order does not matter): " + equivalentSets(hashTable.keys(), keysExamples));

        System.out.println("------");
        Iterable<String> it = hashTable.keys();
        for (String key : it)
            System.out.print(key + ",");
        System.out.println();

        System.out.println("get " + keysExamples[1] + ": " + hashTable.get(keysExamples[1]));
        System.out.println("get " + keysExamples[10] + ": " + hashTable.get(keysExamples[10]));
        System.out.println("get " + keysExamples[24] + ": " + hashTable.get(keysExamples[24]));
    }

    public static void test12()
    {
        System.out.println(info + "Testando funcionalidades de logging de trocas");
        ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<Integer, Integer>(0);

        hashTable.put(0, pseudoRandom.nextInt(1500));
        hashTable.put(1, pseudoRandom.nextInt(1500));
        hashTable.put(-1, pseudoRandom.nextInt(1500));
        hashTable.put(2, pseudoRandom.nextInt(1500));
        hashTable.put(-2, pseudoRandom.nextInt(1500));

        System.out.println("Swap average: " + String.format(Locale.US, "%.02f", hashTable.getSwapAverage()));
        System.out.println("Swap variation: " + String.format(Locale.US, "%.02f", hashTable.getSwapVariation()));

        hashTable = new ForgettingCuckooHashTable<Integer, Integer>(0);
        hashTable.setSwapLogging(true);

        hashTable.put(1, pseudoRandom.nextInt(1500));
        hashTable.put(-1, pseudoRandom.nextInt(1500));
        hashTable.put(2, pseudoRandom.nextInt(1500));
        hashTable.put(-2, pseudoRandom.nextInt(1500));

        System.out.println("Swap average: " + String.format(Locale.US, "%.02f", hashTable.getSwapAverage()));
        System.out.println("Swap variation: " + String.format(Locale.US, "%.02f", hashTable.getSwapVariation()));

        hashTable.put(1, 123);
        hashTable.put(2, 2134);

        System.out.println("Swap average: " + String.format(Locale.US, "%.02f", hashTable.getSwapAverage()));
        System.out.println("Swap variation: " + String.format(Locale.US, "%.02f", hashTable.getSwapVariation()));


        for (int i = 3; i < 20; i++)
        {
            hashTable.put(i, pseudoRandom.nextInt(1500));
        }

        System.out.println("Swap average: " + String.format(Locale.US, "%.02f", hashTable.getSwapAverage()));
        System.out.println("Swap variation: " + String.format(Locale.US, "%.02f", hashTable.getSwapVariation()));
    }

    public static void test13()
    {
        //Teste não disponibilizado
    }

    public static void test14()
    {
        System.out.println(info + "Testando funcionalidade de esquecimento");
        ForgettingCuckooHashTable<Integer, Integer> hashTable = new ForgettingCuckooHashTable<Integer, Integer>(0);
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        for (int i = 1; i < 8; i++)
        {
            hashTable.put(i, i);
        }
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        hashTable.advanceTime(25);
        System.out.println(info + "Lazy forgetting, keys should not be immediately deleted");
        System.out.println("get 1: " + hashTable.get(1));
        System.out.println(info + "Forgetting Sarah Marshall, when we need space, old keys should be forgotten");
        for (int i = 1; i < 8; i++)
        {
            hashTable.put(-i, i);
        }
        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + String.format(Locale.US, "%.02f", hashTable.getLoadFactor()) +
            ")");
        System.out.println("get 1: " + hashTable.get(1));
        System.out.println("get 2: " + hashTable.get(2));
        System.out.println("get 3: " + hashTable.get(3));
        System.out.println("get 7: " + hashTable.get(7));

    }

    public static void test15()
    {
        //Teste não disponibilizado
    }

    public static void test16()
    {
        //Espero que seja óbvio que os tempos que estão aqui registados são os tempos ajustados para o Mooshak
        //Nas vossas máquinas isto será executado em muito menos tempo (a menos que tenham uma torradeira como computador)
        System.out.println(info + "Testing efficiency of Hashtable (search miss/insert), when close to 50% load factor");
        System.out.println(info + "Testing searches with table of 150 000 random keys");

        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<String, Integer>();

        String[] keysExamples = new String[LARGE];
        String randomString;
        for (int i = 0; i < LARGE; i++)
        {
            randomString = generateRandomString(7);
            hashTable.put(randomString, pseudoRandom.nextInt(LARGE));
            keysExamples[i] = randomString;
        }

        System.out.println("Size/Capacity(LoadFactor): " + hashTable.size() + "/" + hashTable.getCapacity() + "(" + hashTable.getLoadFactor() + ")");
        System.out.println(info + "just to make sure that the table is not empty");
        for (int i = 0; i < 3; i++)
        {
            System.out.println("get " + keysExamples[i * 1311] + ": " + hashTable.get(keysExamples[i * 1311]));
        }

        System.out.println(info + "Performing 5000 searches (for a random key that is not there), and getting average time");
        long elapsedTime = TimeAnalysisUtils.getAverageCPUTime(() -> {
            for (int j = 0; j < 5000; j++)
            {
                hashTable.get(generateRandomString(4));
            }
        });

        randomString = generateRandomString(4);
        System.out.println("get " + randomString + ": " + hashTable.get(randomString));
        System.out.println("Tempo de execução médio CPU <= 5.0ms: " + ((elapsedTime / 1E6) < 5.0f));

        System.out.println(info + "Performing 5000 searches (for a random key that is there), and getting average time");
        elapsedTime = TimeAnalysisUtils.getAverageCPUTime(() -> {
            for (int j = 0; j < 5000; j++)
            {
                hashTable.get(keysExamples[pseudoRandom.nextInt(LARGE)]);
            }
        });

        System.out.println("get " + keysExamples[0] + ": " + hashTable.get(keysExamples[0]));
        System.out.println("Tempo de execução médio CPU <= 3.0ms: " + ((elapsedTime / 1E6) <= 3.0f));

        System.out.println(info + "Performing 1000 puts (of new keys), and getting average time");
        elapsedTime = TimeAnalysisUtils.getAverageCPUTime(
            hashTable,
            //este lambda é usado para construir a tabela com 150 000 elementos que vai ser usada no teste seguinte
            //o tempo desta construção não é contabilizado no tempo do teste seguinte
            //Se usasse sempre a mesma tabela, a tabela ia crescendo à medida que fazemos os testes (este teste vai ser repetido 30x),
            //e não queremos isso pois eu quero sempre testar o tempo de execução de inseir 1000 chaves numa tabela de 150 000.
            (t) -> {
                ForgettingCuckooHashTable<String, Integer> table = new ForgettingCuckooHashTable<String, Integer>();
                for (int i = 0; i < LARGE; i++)
                {
                    table.put(keysExamples[i], pseudoRandom.nextInt(LARGE));
                }
                return table;
            },
            (t) -> {
                for (int j = 0; j < 1000; j++)
                {
                    t.put(generateRandomString(7), j);
                }
            });

        System.out.println("Tempo de execução médio CPU <= 1.5ms: " + ((elapsedTime / 1E6) <= 1.5f));
    }


    public static void test17()
    {
        //Teste não disponibilizado
    }


    private static void printArray(Comparable[] a, int n)
    {
        for (int i = 0; i < n; i++)
        {
            System.out.print(a[i] + ",");
        }

        System.out.println();
    }



    public static String generateRandomString(int size)
    {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'


        String generatedString = pseudoRandom.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(size)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        return generatedString;
    }

}
