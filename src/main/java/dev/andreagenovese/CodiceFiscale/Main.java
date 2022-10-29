package dev.andreagenovese.CodiceFiscale;

public class Main {
    public static void main(String[] args) {
        Person p = new Person("Andrea Giovanni", "Genovese", true, 13, 7, 2001,
                CodiceCatastale.getCodiceCatastale("Sesto San Giovanni"));
        System.out.println(p);
        System.out.println(CodiceFiscale.calculate(p));
    }
}
