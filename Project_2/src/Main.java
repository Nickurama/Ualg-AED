import aed.collections.QueueArray;

public class Main
{
    public static void main(String[] args)
    {
        QueueArray<String> queue = new QueueArray<String>(5);
        queue.enqueue("1");
        queue.enqueue("2");
        queue.dequeue();
        queue.enqueue("3");
        queue.enqueue("4");
        queue.dequeue();
        queue.enqueue("5");
        queue.enqueue("6");
        queue.enqueue("7");

        System.out.println("Size: " + queue.size());
        System.out.println("-----");
        queue.print();

        QueueArray<String> queueCopy = queue.shallowCopy();

        System.out.println("Size: " + queueCopy.size());

        System.out.println("-----");
        queueCopy.print();

    }
}
