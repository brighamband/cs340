package src;

public class DecorationPlacer {

    private IFactory factory;

    public DecorationPlacer(IFactory factory) {
        this.factory = factory;
    }

    public String placeDecorations() {
        return "Everything was ready for the party. The " + factory.getYardOrnamentProvider().getOrnament()
                + " was in front of the house, the " + factory.getWallHangingProvider().getHanging()
                + " was hanging on the wall, and the tablecloth with " + factory.getTableclothPatternProvider().getTablecloth()
                + " was spread over the table.";
    }
}
