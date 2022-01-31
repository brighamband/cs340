public class Maximizer<T extends Comparable<T>> {
    T currentMax;

    public Maximizer() {
        currentMax = null;
    }

    /**
     * Pass in a value that you'll want to run maximizer on
     */
    public void updateValue(T value) {
        if (currentMax == null || value.compareTo(currentMax) > 0) {
            currentMax = value;
        }
    }

    /**
     * Retrieve the maximum value that was passed to its updateValue method
     * @return max value
     */
    public T getValue() {
        return currentMax;
    }
}
