
public class FlightMonitor {
	
	public static void main(String[] args) {
		FlightStatusReporter fsr = new FlightStatusReporter();
//		FlightDeltaReporter fdr = new FlightDeltaReporter();

		FlightFeed feed = new FlightFeed();
		feed.add(fsr);
//		feed.add(fdr);
		feed.start();
	}
	
}