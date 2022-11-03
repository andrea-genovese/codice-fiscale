package dev.andreagenovese.CodiceFiscale;

import java.time.LocalDate;

public class Person {
        private String name;
        private String surname;
        private boolean isMale;
        private LocalDate dateOfBirth;
        private CodiceCatastale placeOfBirth;
        public Person(String name, String surname, boolean isMale, LocalDate dateOfBirth, CodiceCatastale placeOfBirth) {
                this.name = name;
                this.surname = surname;
                this.isMale = isMale;
                this.dateOfBirth = dateOfBirth;
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
        public LocalDate getDateOfBirth() {
                return dateOfBirth;
        }
        public CodiceCatastale getPlaceOfBirth() {
                return placeOfBirth;
        }
        
}
