package src;

public class ChristmasFactory implements IFactory {
    @Override
    public ITableclothPatternProvider getTableclothPatternProvider() {
        return new ChristmasTableclothPatternProvider();
    }

    @Override
    public IWallHangingProvider getWallHangingProvider() {
        return new ChristmasWallHangingProvider();
    }

    @Override
    public IYardOrnamentProvider getYardOrnamentProvider() {
        return new ChristmasYardOrnamentProvider();
    }
}
