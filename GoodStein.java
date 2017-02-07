import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

public class GoodStein
{
    //main method is what you actually run
    public static void main(String[] args) {

        //Read the starting number from the user
        BigInteger n = new BigInteger(args[0]);

        //Also read how many terms in the GoodStein sequence the user wants
        int e = Integer.parseInt(args[1]);

        //The first base when expanding to hereditary base form
        int base = 2;

        //Start the loop
        for (int i=0; i<e; i++) {
            //Print the number in the goldstein sequence including the starting number
            System.out.println(n);

            //Expand the number into hereditary base form and increment the base
            PairTree pt = expand(n,base++);

            //Evaluate the number with the incremented base and subtract one
            n = collapse(pt,base).subtract(BigInteger.ONE);

            //Stop the sequence if it hits zero
            if (n.compareTo(BigInteger.ZERO) < 0) {
                break;
            }
        }
    }

    //Expands the number x into its hereditary base b form
    //This expanded form is represented as a tree where each node contains a pair of numbers
    private static PairTree expand(BigInteger x, int b) {
        BigInteger bigIntegerB = BigInteger.valueOf(b);
        PairTree pt = new PairTree(x);
        ArrayList<Pair> alp = new ArrayList<Pair>();
        int position = 0;
        if (x.compareTo(bigIntegerB) >= 0) {
            while(true) {
                BigInteger[] quotRem = x.divideAndRemainder(bigIntegerB);
                alp.add(new Pair(position++,quotRem[1].intValue()));
                x = quotRem[0];
                if (x.equals(BigInteger.ZERO)) {
                    break;
                }
            }
            for (Pair pair : alp) {
                PairTree.PairNode pn = new PairTree.PairNode(pair);
                recursiveExpand(pn,b);
                pt.root.children.add(pn);
            }
        }
        return pt;
    }

    private static void recursiveExpand(PairTree.PairNode n, int b) {
        if (n.data.position >= b) {
            ArrayList<Pair> alp = convertToPositionArray(n.data.position,b);
            for (Pair pair : alp) {
                PairTree.PairNode pn = new PairTree.PairNode(pair);
                recursiveExpand(pn,b);
                n.children.add(pn);
            }
        }
    }

    //Switches the base of any number x to base b
    private static ArrayList<Pair> convertToPositionArray(int x, int b) {
        ArrayList<Pair> returnThis = new ArrayList<Pair>();
        int position = 0;
        while(true) {
            returnThis.add(new Pair(position++,x%b));
            x /= b;
            if (x == 0) {
                break;
            }
        }
        return returnThis;
    }

    //Evaluates the expanded hereditary base b form to an integer
    private static BigInteger collapse(PairTree t, int b) {
        if (t.root.children.size() == 0) {
            return t.root.data;
        }
        BigInteger total = BigInteger.valueOf(0);
        for (PairTree.PairNode pn : t.root.children) {
            recursiveCollapse(pn,b);
            total = total.add(BigInteger.valueOf(pn.data.value).multiply(BigInteger.valueOf(b).pow(pn.data.position)));
        }
        t.root.children = new ArrayList<PairTree.PairNode>();
        t.root.data = total;
        return t.root.data;
    }

    private static void recursiveCollapse(PairTree.PairNode n, int b) {
        if (n.children.size() == 0) {
            return;
        }
        int total = 0;
        for (PairTree.PairNode pn : n.children) {
            recursiveCollapse(pn,b);
            BigInteger result = BigInteger.valueOf(pn.data.value).multiply(BigInteger.valueOf(b).pow(pn.data.position));
            //If any of the numbers are too big for integers to represent then the program should crash
            if (result.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                 throw new RuntimeException("Overflow occured");
            } else if (result.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                 throw new RuntimeException("Underflow occured");
            } else {
                total += result.intValue();
            }
        }
        n.children = new ArrayList<PairTree.PairNode>();
        n.data.position = total;
    }

    //This data structure stores the expanded heriditary base form of a number
    private static class PairTree {
        private RootNode root;
        public PairTree(BigInteger n) {
            root = new RootNode(n);
        }
        private static class PairNode {
            //Each node in the tree contains a pair of numbers
            private Pair data;
            private List<PairNode> children;
            public PairNode(Pair d) {
                data = d;
                children = new ArrayList<PairNode>();
            }
        }
        private static class RootNode {
            //The root node is different because it only contains one BIG number
            private BigInteger data;
            private List<PairNode> children;
            public RootNode(BigInteger d) {
                data = d;
                children = new ArrayList<PairNode>();
            }
        }
    }

    //Used to store a number of the form a*(b^c)
    //where c is the position and a is the value
    //b isn't needed till this number is expanded or collapsed
    private static class Pair {
        int position;
        int value;
        public Pair(int p, int v) {
            position = p;
            value = v;
        }
    }
}
