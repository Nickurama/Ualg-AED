public class SimpleMain {

    public static void main(String[] args)
    {
        ContaBancaria c1 = new ContaBancaria(1);
        ContaBancaria c2 = new ContaBancaria(1,20);

        c1.deposito(5);
        c2.deposito(10);
        System.out.println("Saldo C1:" + c1.getSaldo());
        System.out.println("Saldo C2:" + c2.getSaldo());

        c2.levantamento(7.5);
        System.out.println("Saldo C1:" + c1.getSaldo());
        System.out.println("Saldo C2:" + c2.getSaldo());

        c1.deposito(5000);
        System.out.println("Saldo C1:" + c1.getSaldo());
        System.out.println("Saldo C2:" + c2.getSaldo());

        ContaBancaria c3 = new ContaBancaria();
        System.out.println("NIB C3:" + c3.getNIB());
        ContaBancaria c4 = new ContaBancaria();
        System.out.println("NIB C4:" + c4.getNIB());
        ContaBancaria c5 = new ContaBancaria();
        System.out.println("NIB C5:" + c5.getNIB());
    }
}
