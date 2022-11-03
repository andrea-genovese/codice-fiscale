package dev.andreagenovese.CodiceFiscale;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Names {
    static Map<String, Integer> maleNames;
    static Map<String, Integer> femaleNames;
    static {
        try (ObjectInputStream ois = new ObjectInputStream(
                Names.class.getClassLoader().getResourceAsStream("names.ser"))) {
            maleNames = (Map<String, Integer>) ois.readObject();
            femaleNames = (Map<String, Integer>) ois.readObject();
        } catch (NullPointerException | IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
            loadNames();
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("./src/main/resources/names.ser"))) {
                oos.writeObject(maleNames);
                oos.writeObject(femaleNames);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadNames() {
        maleNames = new HashMap<>();
        femaleNames = new HashMap<>();
        InputStream stream = Names.class.getClassLoader().getResourceAsStream("nomi.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        br.lines()
                .forEach(str -> {
                    String[] attributes = str.split(",");
                    Map<String, Integer> relevantMap = attributes[3].equals("m") ? maleNames : femaleNames;
                    String name = capitalize(attributes[0]);
                    Integer prevCount = relevantMap.get(name);

                    int currCount = Integer.parseInt(attributes[2]);
                    if (prevCount == null) {
                        relevantMap.put(name, currCount);
                    } else {
                        relevantMap.put(name, prevCount + currCount);
                    }
                });
    }

    private static String capitalize(String str) {
        String[] names = str.split(" ");
        String result = "";
        char[] arr = names[0].toLowerCase().toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        result += new String(arr);
        for (int i = 1; i < names.length; i++) {
            arr = names[i].toLowerCase().toCharArray();
            if(arr.length == 0) {
                System.out.println(" ");
            }
            arr[0] = Character.toUpperCase(arr[0]);
            result += ' ' +  new String(arr);
        }
        return result;
    }
}
