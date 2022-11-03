package dev.andreagenovese.CodiceFiscale;

import java.time.LocalDate;
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
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private TreeMap<Double, String> names;
    private List<String> surnames;

    public RevertionResult(String partialName, String partialSurname, boolean isMale, LocalDate dateOfBirth,
            String placeOfBirth) {
        this.partialName = partialName;
        this.partialSurname = partialSurname;
        this.isMale = isMale;
        this.dateOfBirth = dateOfBirth;
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

    private static TreeMap<Double, String> getSuitableNames(String partialName, boolean isMale) {
        Map<String, Integer> names = isMale ? Names.maleNames : Names.femaleNames;
        TreeMap<Double, String> map = new TreeMap<>(Comparator.reverseOrder());
        Map<String, Integer> suitableNames = new HashMap<>();
        int total = 0;
        for (Entry<String, Integer> entry : names.entrySet()) {
            if(entry.getKey().equals("MARIO LUIGI")) {
                System.out.println(entry.getValue());
            }
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }
}