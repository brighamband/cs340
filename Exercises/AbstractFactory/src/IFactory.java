package src;

public interface IFactory {
    ITableclothPatternProvider getTableclothPatternProvider();
    IWallHangingProvider getWallHangingProvider();
    IYardOrnamentProvider getYardOrnamentProvider();
}
