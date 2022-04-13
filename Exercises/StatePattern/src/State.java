public interface State {
    void addGumballs(GumballMachine context, int count);
    void insertQuarter(GumballMachine context);
    void removeQuarter(GumballMachine context);
    void turnHandle(GumballMachine context);
}
