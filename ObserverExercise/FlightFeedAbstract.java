import java.util.ArrayList;
import java.util.List;

public abstract class FlightFeedAbstract {
    private List<FlightObserver> flightObserverList = new ArrayList<>();

    public void add(FlightObserver flightObs) {
        this.flightObserverList.add(flightObs);
    }

    public void notifyObservers(Flight flight) {
        for (FlightObserver flightObs: flightObserverList) {
            flightObs.update(flight);
        }
    }
}
