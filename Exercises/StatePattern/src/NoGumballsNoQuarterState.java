public class NoGumballsNoQuarterState implements State {
    @Override
    public void addGumballs(GumballMachine context, int count) {
        context.setState(new HasGumballsNoQuarterState());
    }

    @Override
    public void insertQuarter(GumballMachine context) {
        // Don't let quarter be inserted, no gumballs are available
        System.out.println("Quarter wasn't accepted because no gumballs are available");
    }

    @Override
    public void removeQuarter(GumballMachine context) {
        // Don't let quarter be removed, no quarters
        System.out.println("No quarters are in the machine, so quarter couldn't be removed");
    }

    @Override
    public void turnHandle(GumballMachine context) {
        System.out.println("The gumball machine is out of gumballs and you did not insert a quarter, so you can't get a gumball yet");
    }
}
