package dev.andreagenovese.CodiceFiscale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.TreeMap;

import org.junit.Test;


public class Tests {
    Person p = new Person("Mario Luigi", "Rossi", true, LocalDate.of(1999, 5, 13), new CodiceCatastale("F205"));

    @Test
    public void controlChar() {
        assertEquals('A', CodiceFiscale.controlChar("RSSMLG99E13F205"));
    }
    @Test
    public void names(){
        assertNotNull(Names.maleNames);
        assertNotNull(Names.femaleNames);        
    }
    
    @Test
    public void codiceFiscale() {
        assertEquals("RSSMLG99E13F205A", CodiceFiscale.calculate(p));
    }
    @Test
    public void comune(){
        assertEquals("I690", CodiceCatastale.fromPlaceName("Sesto San Giovanni").toString());
    }
    @Test
    public void stato(){
        assertEquals("Z102", CodiceCatastale.fromPlaceName("Austria").toString());
        assertEquals("GERMANIA", (new CodiceCatastale("Z112").getPlaceName()));
    }
    @Test
    public void revertion(){
        TreeMap<Double, String> names = CodiceFiscale.revert("RSSMLG99E13F205A").getNames();
        assertTrue(Names.maleNames.containsKey("Mario Luigi"));
        assertNotNull(names);
        assertTrue(names.containsValue("Mario Luigi"));
    }

}
