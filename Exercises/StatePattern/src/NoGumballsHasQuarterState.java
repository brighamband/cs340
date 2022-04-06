public class NoGumballsHasQuarterState implements State {
    @Override
    public void addGumballs(GumballMachine context, int count) {
        context.setState(new HasGumballsHasQuarterState());
    }

    @Override
    public void insertQuarter(GumballMachine context) {
        // Don't let quarter be inserted, no gumballs are available
        System.out.println("Quarter wasn't accepted because no gumballs are available");
    }

    @Override
    public void removeQuarter(GumballMachine context) {
        context.quarterIsInSlot = false;
        context.setState(new NoGumballsNoQuarterState());
        System.out.println("Quarter removed.  Now there's no gumballs and no quarter");
    }

    @Override
    public void turnHandle(GumballMachine context) {
        System.out.println("The gumball machine is out of gumballs, so you can't get a gumball yet");
    }
}
