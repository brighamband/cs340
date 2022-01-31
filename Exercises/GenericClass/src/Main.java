public class Main {
    public static void main(String[] args) {
        // Maximizer test sample
        Maximizer<String> strMaximizer = new Maximizer<>();
        strMaximizer.updateValue("a");
        strMaximizer.updateValue("z");
        strMaximizer.updateValue("m");
        String maxStr = strMaximizer.getValue();
        System.out.println(maxStr);

        Maximizer<Integer> intMaximizer = new Maximizer<>();
        intMaximizer.updateValue(-22);
        intMaximizer.updateValue(10000);
        intMaximizer.updateValue(33);
        Integer maxInt = intMaximizer.getValue();
        System.out.println(maxInt);

        // Algorithms test sample
        String[] strArr = new String[] { "z", "a", "m" };
        Stats<String> strStats = Algorithms.calcStats(strArr);
        System.out.println(String.format("min: %s, max: %s",
                strStats.min, strStats.max));

        Integer[] intArr = new Integer[] { 10000, 33, -22 };
        Stats<Integer> intStats = Algorithms.calcStats(intArr);
        System.out.println(String.format("min: %d, max: %d", intStats.min, intStats.max));
    }
}
