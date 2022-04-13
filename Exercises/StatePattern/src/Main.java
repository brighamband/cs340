public class Main {
    public static void main(String[] args) {
        GumballMachine machine = new GumballMachine();

        // Test no gumball no quarter turn handle
        machine.turnHandle();

        // Test no gumball has quarter turn handle
        machine.insertQuarter();
        machine.turnHandle();

        // Test has gumball no quarter turn handle
        machine.removeQuarter();
        machine.addGumballs(10);
        machine.turnHandle();

        // Test removing quarter when no quarter
        machine.removeQuarter();

        // Test inserting quarter when there's already a quarter inserted
        machine.insertQuarter();
        machine.insertQuarter();

        // Test valid transaction with turn handle
        machine.turnHandle();

        // Make sure that gumballs left is 9, no quarter in slot, and total revenue is $0.25
        System.out.println("There are now " + machine.numGumballs + " left");
        if (machine.quarterIsInSlot)
            System.out.println("There is a quarter in the slot");
        else
            System.out.println("No quarter is in the slot");
        System.out.println("Total revenue is $" + machine.totalRevenue);
    }
}
