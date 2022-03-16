package src;

public class HalloweenFactory implements IFactory {

    @Override
    public ITableclothPatternProvider getTableclothPatternProvider() {
        return new HalloweenTableclothPatternProvider();
    }

    @Override
    public IWallHangingProvider getWallHangingProvider() {
        return new HalloweenWallHangingProvider();
    }

    @Override
    public IYardOrnamentProvider getYardOrnamentProvider() {
        return new HalloweenYardOrnamentProvider();
    }
}
