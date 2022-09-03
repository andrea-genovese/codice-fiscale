package dev.andreagenovese;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
/**
 * Unit test for simple App.
 */
public class Tests {
    Person p = new Person("Andrea Giovanni", "Genovese", true, 13, 7, 2001, "I690");

    @Test
    public void controlChar() {
        assertEquals('I', CodiceFiscale.controlChar("GNVNRG01L13I690"));
    }

    /**
     * Rigorous Test :-)
     */
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


}
