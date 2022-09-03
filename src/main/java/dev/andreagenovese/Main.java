package dev.andreagenovese;

public class Main {
    public static void main(String[] args) {
        Person p = new Person("Andrea Giovanni", "Genovese", true, 13, 7, 2001,
                CodiciCatastali.getCodiceCatastale("Sesto San Giovanni"));
        System.out.println(p);
        System.out.println(CodiceFiscale.calculate(p));
    }
}
