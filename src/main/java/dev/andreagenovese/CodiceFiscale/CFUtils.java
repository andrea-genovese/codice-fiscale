package dev.andreagenovese.CodiceFiscale;

import java.util.Locale;

public class CFUtils {
    static char transformDigit(char digit) {
        if (digit > '2') {
            digit++;
        }
        return (char) (digit - '0' + 'L');
    }

    static char revertToDigit(char character) {
        if (character > 'O') {
            character--;
        }
        return (char) (character - 'L' + '0');
    }

    static String calculateSurname(String surname) {
        surname = surname.toUpperCase(Locale.ITALIAN);
        String result = "";
        for (int i = 0; i < surname.length() && result.length() < 3; i++) {
            char c = surname.charAt(i);
            if (isConsonant(c)) {
                result += c;
            }
        }
        if (result.length() == 3) {
            return result;
        }
        for (int i = 0; i < surname.length() && result.length() < 3; i++) {
            char c = surname.charAt(i);
            if (isVowel(c)) {
                result += c;
            }
        }
        if (result.length() == 3) {
            return result;
        }
        while (result.length() < 3) {
            result += 'X';
        }
        return result;
    }

    static String calculateName(String firstName) {
        firstName = firstName.toUpperCase(Locale.ITALIAN);
        char[] nomeArray = firstName.toCharArray();
        String result = "";
        String consonants = "";
        for (char c : nomeArray) {
            if (isConsonant(c)) {
                consonants += c;
            }
        }
        if (consonants.length() > 3) {
            result += consonants.charAt(0);
            result += consonants.charAt(2);
            result += consonants.charAt(3);
            return result;
        }
        if (consonants.length() == 3) {
            return consonants;
        }
        result = consonants;
        for (int i = 0; i < firstName.length() && result.length() < 3; i++) {
            char c = firstName.charAt(i);
            if (isVowel(c)) {
                result += c;
            }
        }
        if (result.length() == 3) {
            return result;
        }
        while (result.length() < 3) {
            result += 'X';
        }
        return result;
    }

    private static boolean isVowel(char c) {
        switch (c) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return true;
        }
        return false;
    }

    private static boolean isConsonant(char c) {
        if (isVowel(c))
            return false;
        return (c > 'A' && c <= 'Z') || (c > 'a' && c <= 'z');
    }

    static char getMonthChar(int month) {
        switch (month) {
            case 1:
                return 'A';
            case 2:
                return 'B';
            case 3:
                return 'C';
            case 4:
                return 'D';
            case 5:
                return 'E';
            case 6:
                return 'H';
            case 7:
                return 'L';
            case 8:
                return 'M';
            case 9:
                return 'P';
            case 10:
                return 'R';
            case 11:
                return 'S';
            case 12:
                return 'T';
            default:
                throw new RuntimeException("Invalid month: " + month);
        }
    }

    static int oddValue(char c) {
        switch (c) {
            case '1':
                return 0;
            case '0':
                return 1;
            case '2':
                return 5;
            case '3':
                return 7;
            case '4':
                return 9;
            case '5':
                return 13;
            case '6':
                return 15;
            case '7':
                return 17;
            case '8':
                return 19;
            case '9':
                return 21;
            case 'A':
                return 1;
            case 'B':
                return 0;
            case 'C':
                return 5;
            case 'D':
                return 7;
            case 'E':
                return 9;
            case 'F':
                return 13;
            case 'G':
                return 15;
            case 'H':
                return 17;
            case 'I':
                return 19;
            case 'J':
                return 21;
            case 'K':
                return 2;
            case 'L':
                return 4;
            case 'M':
                return 18;
            case 'N':
                return 20;
            case 'O':
                return 11;
            case 'P':
                return 3;
            case 'Q':
                return 6;
            case 'R':
                return 8;
            case 'S':
                return 12;
            case 'U':
                return 16;
            case 'T':
                return 14;
            case 'V':
                return 10;
            case 'W':
                return 22;
            case 'X':
                return 25;
            case 'Y':
                return 24;
            case 'Z':
                return 23;
            default:
                throw new RuntimeException("carattere non valido: " + c);
        }
    }

    static int evenValue(char c) {
        if (Character.isDigit(c)) {
            return c - '0';
        }

        if (c >= 'A' && c <= 'Z') {
            return c - 'A';
        }
        throw new RuntimeException("carattere non valido: " + c);

    }

    static String pad2(int n) {
        if (n > 99) {
            n %= 100;
        }
        if (n > 9) {
            return Integer.toString(n);
        }
        return "0" + n;

    }

    static int revertMonth(char month) {
        switch (month) {
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            case 'E':
                return 5;
            case 'H':
                return 6;
            case 'L':
                return 7;
            case 'M':
                return 8;
            case 'P':
                return 9;
            case 'R':
                return 10;
            case 'S':
                return 11;
            case 'T':
                return 12;
            default:
                throw new RuntimeException("Invalid month: " + month);
           
        }
    }
}
