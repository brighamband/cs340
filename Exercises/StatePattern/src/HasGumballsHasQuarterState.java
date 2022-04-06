public class HasGumballsHasQuarterState implements State {
    @Override
    public void addGumballs(GumballMachine context, int count) {
        // Nothing to do here (parent handles numGumballs change, will stay the same state)
    }

    @Override
    public void insertQuarter(GumballMachine context) {
        // Don't let quarter be inserted, a quarter is already in the slot
        System.out.println("Quarter wasn't accepted because there's already a quarter in the slot");
    }

    @Override
    public void removeQuarter(GumballMachine context) {
        context.quarterIsInSlot = false;
        context.setState(new HasGumballsNoQuarterState());
        System.out.println("Quarter removed.  Now there's gumballs and no quarter");
    }

    @Override
    public void turnHandle(GumballMachine context) {
        context.quarterIsInSlot = false;    // Consume quarter
        context.addQuarterToRevenue();
        context.removeGumball();    // Remove gumball that the user will get

        if (context.numGumballs == 0) {
            context.setState(new NoGumballsNoQuarterState());
            return;
        }
        context.setState(new HasGumballsNoQuarterState());

        System.out.println("Transaction successful!  Here's your gumball!");
    }
}
