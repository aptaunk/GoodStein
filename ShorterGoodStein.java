import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

public class ShorterGoodStein
{
    public static void main(String[] args) {
        BigInteger n = new BigInteger(args[0]);
        int e = Integer.parseInt(args[1]);
        int base = 2;
        for (int i=0; i<e; i++) {
            System.out.println(n);
            n = step(n,base++).subtract(BigInteger.ONE);
            if (n.compareTo(BigInteger.ZERO) < 0) {
                break;
            }
        }
    }

    private static BigInteger step(BigInteger x, int b) {
        BigInteger returnThis = BigInteger.valueOf(0);
        for (Pair p : convertToPositionArray(x,b)) {
            if (p.position < b) {
                returnThis = returnThis.add(BigInteger.valueOf(p.value).multiply(BigInteger.valueOf(b+1).pow(p.position)));
            } else {
                BigInteger temp = step(BigInteger.valueOf(p.position),b);
                if (temp.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                    throw new RuntimeException("Overflow Occurred");
                }
                returnThis = returnThis.add(BigInteger.valueOf(p.value).multiply(BigInteger.valueOf(b+1).pow(temp.intValue())));
            }
        }
        return returnThis;
    }

    private static ArrayList<Pair> convertToPositionArray(BigInteger x, int b) {
        ArrayList<Pair> returnThis = new ArrayList<Pair>();
        int position = 0;
        while(true) {
            BigInteger[] temp = x.divideAndRemainder(BigInteger.valueOf(b));
            if (temp[1].compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new RuntimeException("Overflow Occurred");
            }
            returnThis.add(new Pair(position++,temp[1].intValue()));
            x = temp[0];
            if (x.equals(BigInteger.ZERO)) {
                break;
            }
        }
        return returnThis;
    }

    private static class Pair {
        int position;
        int value;
        public Pair(int p, int v) {
            position = p;
            value = v;
        }
    }
}
