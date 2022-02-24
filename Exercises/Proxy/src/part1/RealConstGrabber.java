package part1;

public class RealConstGrabber implements ConstGrabber {
    private static final double PI = 3.14;
    private static final double E = 2.71;

    public double getPi() {
        return PI;
    }

    public double getE() {
        return E;
    }
}
