package part2;

public class Main {
    public static void main(String[] args) {
//        // Step 3 - Real
//
//        // Initialize array
//        int numRows = 2;
//        int numCols = 2;
//        RealArray2D realArray2D = new RealArray2D(numRows, numCols);
//
//        // Populate array
//        realArray2D.set(0, 0, 0);
//        realArray2D.set(0, 1, 0);
//        realArray2D.set(1, 0, 0);
//        realArray2D.set(1, 1, 0);
//
//        // Save to file
//        realArray2D.save("step3out.txt");

        // Step 4 - Proxy
        Array2D proxy = new ProxyArray2D("step3out.txt");


        // Test 1 -- Passing example
        int test1Row = 0;
        int test1col = 1;
        proxy.set(test1Row, test1col, 4);
        int firstVal = proxy.get(test1Row, test1col);
        verifyResult(firstVal, 4, test1Row, test1col);

        // Test 2 -- Failing example
        int test2Row = 0;
        int test2col = 1;
        proxy.set(test2Row, test2col, 7);
        int secondVal = proxy.get(test2Row, test2col);
        verifyResult(firstVal, 6, test2Row, test2col);
    }

    private static void verifyResult(int expected, int actual, int testRow, int testCol) {
        if (expected == actual) {
            System.out.println("Success: Array value at row " + testRow + " and column " + testCol + " was expected value.");
        } else {
            System.out.println("Failure: Array value at row " + testRow + " and column " + testCol + " was not the expected value.");
        }
    }
}
