package dev.andreagenovese.CodiceFiscale;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class RevertionResult {
    private String partialName;
    private String partialSurname;
    private boolean isMale;
    private int dayOfBirth, monthOfBirth, yearOfBirth;
    private String placeOfBirth;
    private TreeMap<Double, String> names;
    private List<String> surnames;

    public RevertionResult(String partialName, String partialSurname, boolean isMale, int dayOfBirth, int monthOfBirth,
            int yearOfBirth,
            String placeOfBirth) {
        this.partialName = partialName;
        this.partialSurname = partialSurname;
        this.isMale = isMale;
        this.dayOfBirth = dayOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.surnames = getSuitableSurnames(partialSurname);
        this.names = getSuitableNames(partialName, isMale);
    }

    public static void main(String[] args) {
        System.out.println(CodiceFiscale.revert("GNVNRG01L13I690I").getSurnames());

    }

    public List<String> getSurnames() {
        return surnames;
    }

    public TreeMap<Double, String> getNames() {
        return names;
    }

    private TreeMap<Double, String> getSuitableNames(String partialName, boolean isMale) {
        Map<String, Integer> names = isMale ? Names.maleNames : Names.femaleNames;
        TreeMap<Double, String> map = new TreeMap<>(Comparator.reverseOrder());
        Map<String, Integer> suitableNames = new HashMap<>();
        int total = 0;
        for (Entry<String, Integer> entry : names.entrySet()) {
            if (partialName.equals(CFUtils.calculateName(entry.getKey()))) {
                suitableNames.put(entry.getKey(), entry.getValue());
                total += entry.getValue();
            }
        }
        for (Entry<String, Integer> entry : suitableNames.entrySet()) {
            double percentage = entry.getValue() * 100 / (double) total;
            map.put(percentage, entry.getKey());
        }

        return map;
    }

    private List<String> getSuitableSurnames(String partialSurname) {
        return Surnames.list.stream()
                .parallel()
                .filter(surname -> partialSurname.equals(CFUtils.calculateSurname(surname)))
                .toList();
    }

    public String getPartialName() {
        return partialName;
    }

    public String getPartialSurname() {
        return partialSurname;
    }

    public boolean isMale() {
        return isMale;
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public int getMonthOfBirth() {
        return monthOfBirth;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }
}