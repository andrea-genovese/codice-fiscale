package dev.andreagenovese.CodiceFiscale;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Surnames {
    static List<String> list;
    static {
        try (ObjectInputStream ois = new ObjectInputStream(
                Surnames.class.getClassLoader().getResourceAsStream("surnames.ser"))) {
            list = (List<String>) ois.readObject();
            System.out.println("loaded from ser");
        } catch (Exception e1) {
            e1.printStackTrace();
            loadFromCSV();
            System.out.println("loaded from csv");
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("./src/main/resources/surnames.ser"))) {
                oos.writeObject(list);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        System.out.println();
    }

    private static void loadFromCSV() {
        InputStream stream = Surnames.class.getClassLoader().getResourceAsStream("surnames.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            list = br.lines().toList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
