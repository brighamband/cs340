public class GumballMachine {

    private State currentState;
    public int numGumballs;
    public double totalRevenue;
    public boolean quarterIsInSlot;

    public GumballMachine() {
        this.currentState = new NoGumballsNoQuarterState();
        numGumballs = 0;
        totalRevenue = 0.0;
        quarterIsInSlot = false;
    }

    public void setState(State state) {
        currentState = state;
    }

    /**
     * Adds more gumballs to the machine
     */
    public void addGumballs(int count) {
        numGumballs += count;
        currentState.addGumballs(this, count);
        System.out.println("Gumballs added!  There are " + numGumballs + " now.");
    }

    public void removeGumball() {
        numGumballs--;
    }

    /**
     * Inserts a quarter into the slot
     */
    public void insertQuarter() {
        currentState.insertQuarter(this);
    }

    /**
     * Removes the quarter currently in the slot (user changed their mind)
     */
    public void removeQuarter() {
        currentState.removeQuarter(this);
    }

    public void addQuarterToRevenue() {
        double QUARTER_VALUE = 0.25;
        totalRevenue += QUARTER_VALUE;
    }

    /**
     * Consumes quarter and dispenses gumballs
     */
    public void turnHandle() {
        currentState.turnHandle(this);
    }
}
