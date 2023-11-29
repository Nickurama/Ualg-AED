package aed.collections;

import java.util.Iterator;
import java.util.Stack;

public class Cordel implements Iterable<String>
{
    private class CordelIterator implements Iterator<String>
    {
        Stack<Cordel> state;

        public CordelIterator()
        {
            state = new Stack<Cordel>();
            state.push(Cordel.this);
        }

        public boolean hasNext()
        {
            return !state.empty();
        }

        public String next()
        {
            Cordel current = state.pop();

            if (current.isLeaf())
                return current.string;

            state.push(current.right);
            state.push(current.left);
            return next();
        }
    }

    private final Cordel left;
    private final Cordel right;
    private final int leftLength;
    private final int rightLength;
    private final String string;

    public Cordel(String s)
    {
        if (s == null)
            throw new IllegalArgumentException("Can't create leaf with null string.");

        this.left = null;
        this.right = null;
        this.leftLength = s.length(); // saves some efficiency off length()
        this.rightLength = 0;
        this.string = s;
    }

    public Cordel(Cordel left, Cordel right)
    {
        this.left = left;
        this.right = right;
        this.leftLength = left.length();
        this.rightLength = right.length();
        this.string = null;
    }

    private boolean isLeaf()
    {
        return this.string != null;
    }

    public int length()
    {
        return this.leftLength + this.rightLength;
    }

    public Cordel append(String s)
    {
        return this.append(new Cordel(s));
    }

    public Cordel append(Cordel c)
    {
        return new Cordel(this, c);
    }

    public Cordel prepend(String s)
    {
        return this.prepend(new Cordel(s));
    }

    public Cordel prepend(Cordel c)
    {
        return new Cordel(c, this);
    }

    public void print()
    {
        if (this.isLeaf())
        {
            System.out.print(this.string);
            return;
        }

        this.left.print();
        this.right.print();
    }

    public void println()
    {
        print();
        System.out.println();
    }

    public void printInfo()
    {
        if (this.isLeaf())
        {
            System.out.print("LEAF:\"" + this.string + "\"");
            return;
        }

        System.out.print("CONCAT[" + leftLength + "," + rightLength + "]:{");
        this.left.printInfo();
        System.out.print(",");
        this.right.printInfo();
        System.out.print("}");

    }

    public void printInfoNL()
    {
        this.printInfo();
        System.out.println();
    }

    public char charAt(int i)
    {
        if (this.isLeaf())
            return this.string.charAt(i);

        if (i >= this.leftLength)
            return this.right.charAt(i - this.leftLength);
        return this.left.charAt(i);
    }

    public Cordel[] split(int i)
    {
        if (this.isLeaf())
            return new Cordel[] {new Cordel(string.substring(0, i)), new Cordel(string.substring(i))};

        if (this.leftLength == i)
            return new Cordel[] {this.left, this.right};

        if (this.leftLength > i)
        {
            Cordel[] leftSplit = this.left.split(i);
            leftSplit[1] = leftSplit[1].append(this.right);
            return leftSplit;
        }

        Cordel[] rightSplit = this.right.split(i - this.leftLength);
        rightSplit[0] = rightSplit[0].prepend(this.left);
        return rightSplit;
    }

    public Cordel insertAt(int i, String s)
    {
        if (i == 0)
            return this.prepend(s);

        if (i == this.length())
            return this.append(s);

        Cordel[] split = this.split(i);
        return split[0].append(new Cordel(s)).append(split[1]);
    }

    public Cordel delete(int i, int length)
    {
        if (i == 0)
            return this.split(length)[1];

        Cordel left = this.split(i)[0];
        Cordel right = this.split(i + length)[1];
        return left.append(right);
    }

    @Override
    public Iterator<String> iterator()
    {
        return new CordelIterator();
    }

    public void print(int i, int length)
    {
        if (this.isLeaf())
            System.out.print(this.string.substring(i, Math.min(i + length, this.string.length())));
        else if (i < this.leftLength)
        {
            this.left.print(i, length);
            if (i + length >= this.leftLength)
                this.right.print(0, length - (this.leftLength - i));
        } else
            this.right.print(i - this.leftLength, length);
    }

    public void println(int i, int length)
    {
        print(i, length);
        System.out.println();
    }

    public static void main(String[] args)
    {
        //colocar aqui testes efetuados
    }
}
