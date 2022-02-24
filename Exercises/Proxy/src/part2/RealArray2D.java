package part2;

import java.io.*;

public class RealArray2D implements Array2D {
    public int[][] array;

    public RealArray2D(int row, int col) {
        this.array = new int[row][col];
    }

    public RealArray2D(String fileName) {
        load(fileName);
    }

    @Override
    public void set(int row, int col, int value) {
        array[row][col] = value;
    }

    @Override
    public int get(int row, int col) {
        return array[row][col];
    }

    /**
     * Saves the object’s state to a file
     * @param fileName
     */
    public void save(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads the object’s state from a file
     * @param fileName
     */
    public void load(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream iis = new ObjectInputStream(fis);
            array = (int[][]) iis.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
