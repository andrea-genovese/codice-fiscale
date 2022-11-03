package dev.andreagenovese.CodiceFiscale;

import static dev.andreagenovese.CodiceFiscale.CFUtils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The {@code CodiceFiscale} class contains several methods to calculate an
 * Italian fiscal code and its variants.
 * Note that the only legally valid fiscal code is the one issued by
 * the Italian fiscal authority and, while the standard algorithm is correctly
 * implemented,
 * the calculated code may differ from the issued one, for example in case of
 * two people having the same code
 * 
 * @author Andrea Genovese
 */
public class CodiceFiscale {
    // the indexes at which there are digits in reverse order
    private static final byte[] digitIdxs = { 14, 13, 12, 10, 9, 7, 6 };

    public static String calculate(String firstName, String surname, boolean isMale,
            LocalDate dateOfBirth, CodiceCatastale codiceCatastale) {
        return calculate(firstName, surname, isMale, dateOfBirth.getDayOfMonth(), dateOfBirth.getMonthValue(),
                dateOfBirth.getYear(), codiceCatastale);
    }

    public static List<String> calculate(String firstName, String surname, boolean isMale,
            LocalDate dateOfBirth, CodiceCatastale codiceCatastale, int variations) {
        return calculate(firstName, surname, isMale, dateOfBirth.getDayOfMonth(), dateOfBirth.getMonthValue(),
                dateOfBirth.getYear(), codiceCatastale, variations);
    }

    /**
     * This method calculate the fiscal code with the standard algorithm. Be aware
     * that in case of people with the same fiscal code the fiscal code may be
     * changed.
     * If you need many possible fiscal codes for a person, use
     * {@link CodiceFiscale#calculate(Person, int) calculate(Person, int)} or
     * {@link CodiceFiscale#calculateAll(Person) calculateAll(Person)}
     * 
     * @param firstName
     * @param surname
     * @param isMale
     * @param day             the day of birth
     * @param month           the month of birth (January is 1)
     * @param year            the year of birth (2-digits and 4-digits are both
     *                        accepted)
     * @param codiceCatastale 4 character Codice Belfiore of the place of birth
     *                        (Italian town or foreign country)
     * @return Codice Fiscale calculated with the standard algorithm
     * @see <a href=
     *      "https://it.wikipedia.org/wiki/Codice_catastale#Codice_nazionale">Codice
     *      Catastale Nazionale</a>
     */
    public static String calculate(String firstName, String surname, boolean isMale,
            int day, int month, int year, CodiceCatastale codiceCatastale) {
        String CF = calculateSurname(surname) + calculateName(firstName);
        CF += pad2(year);
        CF += getMonthChar(month);
        if (!isMale) {
            day += 40;
        }
        CF += pad2(day);
        if (codiceCatastale == null) {
            throw new NullPointerException("codiceCatastale cannot be null");
        }
        CF += codiceCatastale;
        CF += controlChar(CF);
        return CF;
    }

    /**
     * This method calculate the fiscal code with the standard algorithm. Be aware
     * that in case of people with the same fiscal code the fiscal code may be
     * changed.
     * If you need many possible fiscal codes for a person, use
     * {@link CodiceFiscale#calculate(Person, int) calculate(Person, int)} or
     * {@link CodiceFiscale#calculateAll(Person) calculateAll(Person)}
     * 
     * @param p The person the Fiscal Code belongs to
     * @return Codice Fiscale calculated with the standard algorithm
     */
    public static String calculate(Person p) {
        return calculate(p.getName(), p.getSurname(), p.isMale(), p.getDateOfBirth(), p.getPlaceOfBirth());
    }

    /**
     * This method calculate the fiscal code and some variations (max 127) of it for
     * the same person, ordered in the order the "Agenzia delle Entrate" would
     * assign them.
     * 
     * @param firstName
     * @param surname
     * @param isMale
     * @param day             the day of birth
     * @param month           the month of birth (January is 1)
     * @param year            the year of birth (2-digits and 4-digits are both
     *                        accepted)
     * @param codiceCatastale 4 character Codice Belfiore of the place of birth
     *                        (Italian town or foreign country)
     * @param variations      the number of variation of the standard fiscal code (0
     *                        return a list with only the standard code)
     * @return A {@code List} of fiscal codes ordered in the order they would be
     *         assigned
     * @see <a href=
     *      "https://it.wikipedia.org/wiki/Codice_catastale#Codice_nazionale">Codice
     *      Catastale Nazionale</a>
     */
    public static List<String> calculate(String name, String surname, boolean isMale,
            int day, int month, int year, CodiceCatastale codiceCatastale, int variations) {

        String stdCF = calculate(name, surname, isMale, day, month, year, codiceCatastale);

        try {
            return variations(stdCF, variations);
        } catch (InvalidControlCodeException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This method calculate the fiscal code and some variations (max 127) of it for
     * the same person, ordered in the order the "Agenzia delle Entrate" would
     * assign them.
     * 
     * @param firstName
     * @param surname
     * @param isMale
     * @param day             the day of birth
     * @param month           the month of birth (January is 1)
     * @param year            the year of birth (2-digits and 4-digits are both
     *                        accepted)
     * @param codiceCatastale 4 character Codice Belfiore of the place of birth
     *                        (Italian town or foreign country)
     * @param variations      the number of variation of the standard fiscal code (0
     *                        return a list with only the standard code)
     * @return A {@code List} of fiscal codes ordered in the order they would be
     *         assigned
     */
    public static List<String> calculate(Person p, int variations) {
        return calculate(p.getName(), p.getSurname(), p.isMale(), p.getDateOfBirth(),
                p.getPlaceOfBirth(), variations);
    }

    /**
     * This method calculate the fiscal code and all 127 variations of it for
     * the same person, ordered in the order the "Agenzia delle Entrate" would
     * assign them.
     * 
     * @param firstName
     * @param surname
     * @param isMale
     * @param day             the day of birth
     * @param month           the month of birth (January is 1)
     * @param year            the year of birth (2-digits and 4-digits are both
     *                        accepted)
     * @param codiceCatastale 4 character Codice Belfiore of the place of birth
     *                        (Italian town or foreign country)
     * @return A {@code List} of fiscal codes ordered in the order they would be
     *         assigned
     * @see <a href=
     *      "https://it.wikipedia.org/wiki/Codice_catastale#Codice_nazionale">
     *      Codice Catastale Nazionale</a>
     */
    public static List<String> calculateAll(String name, String surname, boolean isMale,
            int day, int month, int year, CodiceCatastale codiceCatastale) {
        return calculate(name, surname, isMale, day, month, year, codiceCatastale, 127);
    }

    public static List<String> calculateAll(Person p) {
        return calculate(p.getName(), p.getSurname(), p.isMale(), p.getDateOfBirth(),
                p.getPlaceOfBirth(), 127);
    }

    /**
     * This method extracts the following informations from a fiscal code:
     * <ul>
     * <li>3 characters from the first name</li>
     * <li>3 characters from the surname</li>
     * <li>Gender</li>
     * <li>Date of birth (year is in 2-digit format)</li>
     * <li>{@link CodiceCatastale} of the place of birth</li>
     * </ul>
     * 
     * @param CF the fiscal code
     * @return The {@code Person} the code belongs to (only 3 character from
     *         name and surname are available)
     * @throws InvalidControlCodeException If the control code (the last letter) is
     *                                     different from the expected one
     * @throws WrongLengthException        If the fiscal code length is not 16
     * @see <a href=
     *      "https://it.wikipedia.org/wiki/Codice_catastale#Codice_nazionale">
     *      Codice Catastale Nazionale</a>
     */
    public static RevertionResult revert(String CF) throws InvalidControlCodeException, WrongLengthException {
        CF = CF.toUpperCase(Locale.ITALIAN);
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

        return new RevertionResult(name, surname, isMale, LocalDate.of(year, month, day), placeOfBirthCode);
    }

    /**
     * This method returns many variations of the same fiscal code in the order they
     * would be assigned to people with the same code
     * 
     * @param CF         A standard fiscal code
     * @param variations The number of variations needed (max 127)
     * @return A {@code List} of fiscal codes
     * @throws InvalidControlCodeException If the control code (the last letter) is
     *                                     different from the expected one
     */
    public static List<String> variations(String CF, int variations) throws InvalidControlCodeException {

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

    /**
     * Test whether a fiscal code may appertain to a certain person
     * 
     * @param CF              the fiscal code
     * @param firstName
     * @param surname
     * @param isMale
     * @param day             the day of birth
     * @param month           the month of birth (January is 1)
     * @param year            the year of birth (2-digits and 4-digits are both
     *                        accepted)
     * @param codiceCatastale 4 character Codice Belfiore of the place of birth
     *                        (Italian town or foreign country)
     * @return {@code true} if the code may belong to the person, {@code false}
     *         otherwise
     */
    public static boolean test(String CF, Person p) {
        return test(CF, p.getName(), p.getSurname(), p.isMale(), p.getDateOfBirth(),
                p.getPlaceOfBirth());
    }

    public static boolean test(String CF, String name, String surname, boolean isMale, LocalDate dateOfBirth,
            CodiceCatastale placeOfBirth) {
        return test(CF, name, surname, isMale,
                dateOfBirth.getDayOfMonth(),
                dateOfBirth.getMonthValue(),
                dateOfBirth.getYear(),
                placeOfBirth);
    }

    /**
     * Test whether a fiscal code may appertain to a certain person
     * 
     * @param CF              the fiscal code
     * @param firstName
     * @param surname
     * @param isMale
     * @param day             the day of birth
     * @param month           the month of birth (January is 1)
     * @param year            the year of birth (2-digits and 4-digits are both
     *                        accepted)
     * @param codiceCatastale 4 character Codice Belfiore of the place of birth
     *                        (Italian town or foreign country)
     * @return {@code true} if the code may belong to the person, {@code false}
     *         otherwise
     */
    public static boolean test(String CF, String name, String surname, boolean isMale, int dayOfBirth, int monthOfBirth,
            int yearOfBirth, CodiceCatastale placeOfBirth) {
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
     * 
     * @see https://www.agenziaentrate.gov.it/portale/web/guest/schede/istanze/richiesta-ts_cf/informazioni-codificazione-pf
     * 
     */
    private static void loopOverIndexes(int indexesToChange, List<String> CFs, char[] CF, int maxVariations) {
        loopOverIndexes(0, indexesToChange, new int[indexesToChange + 1], 0, CFs, CF, maxVariations);
    }

    private static void loopOverIndexes(int currLevel, int targetLevel, int[] idxs, int baseIndex, List<String> CFs,
            char[] CFarr, int maxVariations) {
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

    private static String calculateVariation(char[] CFarr, int[] indexesToChange) {
        for (int idx : indexesToChange) {
            int realIdx = digitIdxs[idx];
            CFarr[realIdx] = transformDigit(CFarr[realIdx]);
        }
        String newCF = new String(CFarr);
        return newCF + controlChar(newCF);
    }

    public static char controlChar(String CF) {
        int sum = 0;
        for (int i = 0; i < CF.length(); i += 2) {
            sum += oddValue(CF.charAt(i));
        }
        for (int i = 1; i < CF.length() && i < 15; i += 2) {
            sum += evenValue(CF.charAt(i));
        }
        return (char) ('A' + sum % 26);
    }

}