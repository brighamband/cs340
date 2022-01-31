public class Algorithms {
    /**
     * Calculates the minimum and maximum values in an array of any data type that implements the Comparable interface
     * @return statsObj Stats object containing min and max values
     */
    public static <T extends Comparable<T>> Stats<T> calcStats(T[] statsList) {
        Stats<T> statsObj = new Stats<>();

        for (T statItem : statsList) {
            if (statsObj.min == null || statItem.compareTo(statsObj.min) < 0) {
                statsObj.min = statItem;
            }
            if (statsObj.max == null || statItem.compareTo(statsObj.max) > 0) {
                statsObj.max = statItem;
            }
        }

        return statsObj;
    }
}
