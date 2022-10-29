package dev.andreagenovese.CodiceFiscale;

public class Person {
        private String name;
        private String surname;
        private boolean isMale;
        private int dayOfBirth;
        private int monthOfBirth;
        private int yearOfBirth;
        private String placeOfBirth;
        public Person(String name, String surname, boolean isMale, int dayOfBirth, int monthOfBirth, int yearOfBirth,
                        String placeOfBirth) {
                this.name = name;
                this.surname = surname;
                this.isMale = isMale;
                this.dayOfBirth = dayOfBirth;
                this.monthOfBirth = monthOfBirth;
                this.yearOfBirth = yearOfBirth;
                this.placeOfBirth = placeOfBirth;
        }
        public String getName() {
                return name;
        }
        public String getSurname() {
                return surname;
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
