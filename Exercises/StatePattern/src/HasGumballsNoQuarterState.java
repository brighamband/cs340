public class HasGumballsNoQuarterState implements State {
    @Override
    public void addGumballs(GumballMachine context, int count) {
        // Nothing to do here (parent handles numGumballs change, will stay the same state)
    }

    @Override
    public void insertQuarter(GumballMachine context) {
        context.quarterIsInSlot = true;
        context.setState(new HasGumballsHasQuarterState());
        System.out.println("Quarter was inserted");
    }

    @Override
    public void removeQuarter(GumballMachine context) {
        // Don't let quarter be removed, no quarters
        System.out.println("No quarters are in the machine, so quarter couldn't be removed");
    }

    @Override
    public void turnHandle(GumballMachine context) {
        System.out.println("You have not inserted a quarter, so you can't get a gumball yet");
    }
}
