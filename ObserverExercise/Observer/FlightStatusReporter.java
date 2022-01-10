public class FlightStatusReporter implements FlightObserver {
    @Override
    public void update(Flight flight) {
        System.out.println(flight.toString());
    }
}
