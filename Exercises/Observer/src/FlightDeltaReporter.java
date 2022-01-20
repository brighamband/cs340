public class FlightDeltaReporter implements FlightObserver {

    Flight lastFlight = new Flight();

    @Override
    public void update(Flight newFlight) {
        float deltaLongitude = newFlight.longitude - lastFlight.longitude;
        float deltaLatitude = newFlight.latitude - lastFlight.latitude;
        float deltaVelocity = newFlight.velocity - lastFlight.velocity;
        float deltaAltitude = newFlight.geo_altitude - lastFlight.geo_altitude;

        System.out.format("Deltas -- lon: %s, lat: %s, vel: %s, alt: %f\n",
                deltaLongitude, deltaLatitude, deltaVelocity, deltaAltitude);

        // Update last flight
        lastFlight = newFlight;
    }
}
