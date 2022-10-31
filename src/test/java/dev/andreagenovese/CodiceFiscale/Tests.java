package dev.andreagenovese.CodiceFiscale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.TreeMap;

import org.junit.Test;


public class Tests {
    Person p = new Person("Andrea Giovanni", "Genovese", true, 13, 7, 2001, "I690");

    @Test
    public void controlChar() {
        assertEquals('I', CodiceFiscale.controlChar("GNVNRG01L13I690"));
    }
    @Test
    public void names(){
        System.out.println(Names.maleNames);
        System.out.println(Names.femaleNames);
        assertNotNull(Names.maleNames);
        assertNotNull(Names.femaleNames);        
    }
    
    @Test
    public void codiceFiscale() {
        assertEquals("GNVNRG01L13I690I", CodiceFiscale.calculate(p));
    }
    @Test
    public void comune(){
        assertEquals("I690", CodiceCatastale.getCodiceCatastale("Sesto San Giovanni"));
    }
    @Test
    public void stato(){
        assertEquals("Z102", CodiceCatastale.getCodiceCatastale("Austria"));
        assertEquals("GERMANIA", CodiceCatastale.getName("Z112"));
    }
    @Test
    public void revertion(){
        TreeMap<Double, String> map = CodiceFiscale.revert("GNVNRG01L13I690I").getNames();
        assertNotNull(map);
    }

}
