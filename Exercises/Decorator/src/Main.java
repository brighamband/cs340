public class Main {
    public static void main(String[] args) {
        // Use abc source

        IStringSource abcStringSource = new AbcStringSource();

        System.out.println(abcStringSource.next());

        UppercaseDecorator uppercaseDecorator = new UppercaseDecorator(abcStringSource);
        System.out.println(uppercaseDecorator.next());

        ReverseDecorator reverseDecorator = new ReverseDecorator(abcStringSource);
        System.out.println(reverseDecorator.next());

        // Use rev source

        IStringSource randStringSource = new NumStringSource();

        System.out.println(randStringSource.next());

        uppercaseDecorator = new UppercaseDecorator(randStringSource);
        System.out.println(uppercaseDecorator.next());

        reverseDecorator = new ReverseDecorator(randStringSource);
        System.out.println(reverseDecorator.next());
    }
}
