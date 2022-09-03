package dev.andreagenovese;

import static dev.andreagenovese.CFUtils.*;

import java.util.ArrayList;
import java.util.List;


/**
 * CodiceFiscale
 */
public class CodiceFiscale {
    // the indexes of digits
    private static byte[] digitIdxs = { 14, 13, 12, 10, 9, 7, 6 };

    public static String calculate(String name, String surname, boolean isMale,
            int day, int month, int year, String codiceCatastale) {
        String CF = calcolaCognome(surname) + calcolaNome(name);
        CF += pad2(year);
        CF += getMonthChar(month);
        if (!isMale) {
            day += 40;
        }
        CF += pad2(day);
        CF += codiceCatastale;
        CF += controlChar(CF);
        return CF;
    }

    public static String calculate(Person p) {
        return calculate(p.getName(), p.getSurname(), p.isMale(), p.getDayOfBirth(), p.getMonthOfBirth(), p.getYearOfBirth(),
                p.getPlaceOfBirth());
    }

    public static List<String> calculate(String name, String surname, boolean isMale,
            int day, int month, int year, String codiceCatastale, byte variations) {

        String stdCF = calculate(name, surname, isMale, day, month, year, codiceCatastale);

        try {
            return variations(stdCF, variations);
        } catch (InvalidControlCodeException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<String> calculate(Person p, byte variations) {
        return calculate(p.getName(), p.getSurname(), p.isMale(), p.getDayOfBirth(), p.getMonthOfBirth(), p.getDayOfBirth(),
                p.getPlaceOfBirth(), variations);
    }

    public static List<String> calculateAll(String name, String surname, boolean isMale,
            int day, int month, int year, String codiceCatastale) {
        return calculate(name, surname, isMale, day, month, year, codiceCatastale, (byte) 127);
    }

    public static List<String> calculateAll(Person p) {
        return calculate(p.getName(), p.getSurname(), p.isMale(), p.getDayOfBirth(), p.getMonthOfBirth(), p.getDayOfBirth(),
                p.getPlaceOfBirth(), (byte) 127);
    }

    public static Person revert(String CF) throws InvalidControlCodeException, WrongLengthException {
        verifyControlChar(CF);
        CF = toStandardCF(CF);
        String surname = CF.substring(0, 3);
        String name = CF.substring(3, 6);
        int year = Integer.parseInt(CF.substring(6, 8));
        int month = revertMonth(CF.charAt(8));
        int day = Integer.parseInt(CF.substring(9, 11));
        boolean isMale = true;
        if (day > 40) {
            isMale = false;
            day -= 40;
        }
        String placeOfBirthCode = CF.substring(11, 15);

        return new Person(name, surname, isMale, day, month, year, placeOfBirthCode);
    }

    public static List<String> variations(String CF, byte variations) throws InvalidControlCodeException {

        if (variations < 0) {
            throw new RuntimeException("variations must be non negative");
        }
        List<String> CFs = new ArrayList<>(variations + 1);
        CFs.add(CF);
        char[] CFarr = CF.substring(0, 15).toCharArray();
        for (int i = 0; i < 7; i++) {
            loopOverIndexes(i, CFs, CFarr, variations);
        }
        return CFs;
    }

    public static boolean test(String CF, Person p) throws InvalidControlCodeException {
        return test(CF, p.getName(), p.getSurname(), p.isMale(), p.getDayOfBirth(), p.getMonthOfBirth(), p.getDayOfBirth(),
                p.getPlaceOfBirth());
    }

    public static boolean test(String CF, String name, String surname, boolean isMale, int dayOfBirth, int monthOfBirth,
            int yearOfBirth, String placeOfBirth) {
        if (CF.length() != 16) {
            return false;
        }
        try {
            verifyControlChar(CF);
        } catch (InvalidControlCodeException | WrongLengthException e) {
            return false;
        }
        String stdCF = calculate(name, surname, isMale, dayOfBirth, monthOfBirth, yearOfBirth, placeOfBirth);

        return stdCF.equals(toStandardCF(CF));
    }

    private static void verifyControlChar(String CF) throws InvalidControlCodeException, WrongLengthException {
        if (CF.length() != 16) {
            throw new WrongLengthException("Length must be 16");
        }
        char expected = controlChar(CF.substring(0, 15));
        if (expected != CF.charAt(15)) {
            throw new InvalidControlCodeException(
                    "Expected '" + expected + "' to be the last character, but found '" + CF.charAt(15) + "'");
        }
    }

    /**
     * Revert special code issued for people with code collision to standard code
     * 
     * @param CF A code which may or may not have been issued because of collision
     * @return The standard code from which the special one has been derived
     */
    private static String toStandardCF(String CF) {
        char[] arr = CF.toCharArray();
        for (int idx : digitIdxs) {
            if (!Character.isDigit(arr[idx])) {
                arr[idx] = revertToDigit(arr[idx]);
            }
        }
        String stdCF = new String(arr);

        stdCF = stdCF.substring(0, 15);
        stdCF += controlChar(stdCF);
        return stdCF;
    }

    /**
     * This method add to {@code CFs}, in the order specified by Agenzia Delle
     * Entrate all the variations of the given fiscal code
     * {@code CF} that change a given number of characters.
     * The variations are ordered by likeliness to be assigned in case of people
     * having the same code (Omocodia).
     * 
     * @see https://www.agenziaentrate.gov.it/portale/web/guest/schede/istanze/richiesta-ts_cf/informazioni-codificazione-pf
     * 
     */
    private static void loopOverIndexes(int indexesToChange, List<String> CFs, char[] CF, byte maxVariations) {
        loopOverIndexes(0, indexesToChange, new int[indexesToChange + 1], 0, CFs, CF, maxVariations);
    }

    private static void loopOverIndexes(int currLevel, int targetLevel, int[] idxs, int baseIndex, List<String> CFs,
            char[] CFarr, byte maxVariations) {
        for (int i = baseIndex; i < digitIdxs.length; i++) {
            if (CFs.size() > maxVariations) {
                return;
            }
            int[] copy = idxs.clone();
            copy[currLevel] = i;
            if (currLevel == targetLevel) {

                CFs.add(calculateVariation(CFarr.clone(), copy));
            } else {
                loopOverIndexes(currLevel + 1, targetLevel, copy, i + 1, CFs, CFarr, maxVariations);
            }
        }
    }

    public static String calculateVariation(char[] CFarr, int[] indexesToChange) {
        for (int idx : indexesToChange) {
            int realIdx = digitIdxs[idx];
            CFarr[realIdx] = transformDigit(CFarr[realIdx]);
        }
        String newCF = new String(CFarr);
        return newCF + controlChar(newCF);
    }

    public static char controlChar(String CF) {
        int somma = 0;
        for (int i = 0; i < CF.length(); i += 2) {
            somma += valoreDispari(CF.charAt(i));
        }
        for (int i = 1; i < CF.length() && i < 15; i += 2) {
            somma += valorePari(CF.charAt(i));
        }
        return (char) ('A' + somma % 26);
    }

}