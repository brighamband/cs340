package part2;

public class ProxyArray2D implements Array2D {
    private String fileName;
    private RealArray2D realArray2D;

    public ProxyArray2D(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void set(int row, int col, int value) {
        if(realArray2D == null) {
            initRealArray(row, col);
        }
        realArray2D.set(row, col, value);
    }

    @Override
    public int get(int row, int col) {
        if(realArray2D == null) {
            initRealArray(row, col);
        }
        return realArray2D.get(row, col);
    }

    private void initRealArray(int row, int col) {
        if (fileName == null) {
            realArray2D = new RealArray2D(row, col);
        } else {
            realArray2D = new RealArray2D(fileName);
        }
    }
}
