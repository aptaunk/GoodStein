import java.util.ArrayList;
import java.util.List;

public class GoodStein
{
    //main method is what you actually run
    public static void main(String[] args) {

        //Read the starting number from the user
        long n = Long.parseLong(args[0]);
        long base = 2;

        //Start the loop
        while (true) {
            //Print the number in the goldstein sequence including the starting number
            System.out.println(n);

            //Expand the number into hereditary base form and increment the base
            PairTree pt = expand(n,base++);

            //Evaluate the number with the incremented base and subtract one
            n = collapse(pt,base) - 1;

            //Stop the loop if the number is too large or zero is reached
            if (n < 0 || n == Long.MAX_VALUE-1) {
                break;
            }
        }
    }

    //Expands the number x into its hereditary base b form
    //This expanded form is represented as a tree where each node contains a pair of numbers
    private static PairTree expand(long x, long b) {
        PairTree.PairNode pn = new PairTree.PairNode(new Pair(x,1));
        recursiveExpand(pn,b);
        return new PairTree(pn);
    }

    private static void recursiveExpand(PairTree.PairNode n, long b) {
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
    private static ArrayList<Pair> convertToPositionArray(long x, long b) {
        ArrayList<Pair> returnThis = new ArrayList<Pair>();
        long position = 0;
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
    private static long collapse(PairTree t, long b) {
        recursiveCollapse(t.root,b);
        return t.root.data.position;
    }

    private static void recursiveCollapse(PairTree.PairNode n, long b) {
        if (n.children.size() == 0) {
            return;
        }
        long total = 0;
        for (PairTree.PairNode pn : n.children) {
            recursiveCollapse(pn,b);
            total += pn.data.value*Math.pow(b,pn.data.position);
        }
        n.children = new ArrayList<PairTree.PairNode>();
        n.data.position = total;
    }

    //This data structure stores the expanded heriditary base form of a number
    private static class PairTree {
        private PairNode root;
        public PairTree(PairNode n) {
            root = n;
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
    }

    //Used to store a number of the form a*(b^c)
    //where c is the position and a is the value
    //b isn't needed till this number is expanded or collapsed
    private static class Pair {
        long position;
        long value;
        public Pair(long p, long v) {
            position = p;
            value = v;
        }
    }
}
