public class FlightDeltaReporter implements FlightObserver {
    @Override
    public void update(Flight flight) {
        System.out.println("FLIGHT DELTA REPORTER");
        System.out.println(flight.toString());
    }
}
