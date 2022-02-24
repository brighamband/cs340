package part1;

public class Main {
    public static void main(String[] args) {
        RealConstGrabber realConstGrabber = new RealConstGrabber();
        ProxyConstGrabber proxy = new ProxyConstGrabber(realConstGrabber);

        double pi = 0.00;
        double e = 0.00;
        try {
            pi = proxy.getPi();
            e = proxy.getE();

            System.out.println("Constant Grabber called from proxy.");
            System.out.println("Pi is " + pi);
            System.out.println("E is " + e);

        } catch (Exception ex) {
            System.out.println("Invalid date time");
        }
    }
}
