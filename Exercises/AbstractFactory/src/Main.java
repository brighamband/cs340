package src;

public class Main {

    public static void main(String[] args) {

        // Halloween

        DecorationPlacer halloweenDecorationPlacer = new DecorationPlacer(new HalloweenFactory());

        System.out.println(halloweenDecorationPlacer.placeDecorations());

        // Christmas

        DecorationPlacer christmasDecorationPlacer = new DecorationPlacer(new ChristmasFactory());

        System.out.println(christmasDecorationPlacer.placeDecorations());

    }
}
