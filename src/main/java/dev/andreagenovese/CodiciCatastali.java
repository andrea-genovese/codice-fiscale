package dev.andreagenovese;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/* sourced from https://sparql-noipa.mef.gov.it/
 * with query:
 * 
 * 
  SELECT ?label ?codice
    WHERE {
  {?uri rdfs:label ?label.
  ?uri npont:hasCodice ?codice.
  ?uri rdf:type npont:StatoEstero.}
  UNION
  { ?uri rdfs:label ?label.
  ?uri npont:hasCodice ?codice.
  ?uri rdf:type npont:Comune.
   }
  }
ORDER BY ?codice
 */
public class CodiciCatastali {
    private static Map<String, String> map;
    static {
        map = new HashMap<String, String>();
        ClassLoader classLoader = CodiciCatastali.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("codiciCatastali.ser");
        try {
            ObjectInputStream objInput = new ObjectInputStream(inputStream);
            Object obj = objInput.readObject();
            map = (Map<String, String>) obj;

        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                System.out.println("codiciCatastali.ser not found");
                Map<String, String> parsedFile = new HashMap<>();
                URI uri = classLoader.getResource("codiciCatastali.csv").toURI();

                Scanner sc = new Scanner(new File(uri), "UTF-8");
                while (sc.hasNextLine()) {
                    String[] comuneData = sc.nextLine().split(",");
                    parsedFile.put(comuneData[0].toUpperCase(Locale.ITALIAN), comuneData[1]);
                }
                FileOutputStream fos = new FileOutputStream("codiciCatastali.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                map = parsedFile;
                oos.writeObject(map);
                oos.close();

                
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    public static String getCodiceCatastale(String luogo) {
        return map.get(luogo.toUpperCase(Locale.ITALIAN));
    }

    public static String getName(String code) {
        for (Entry<String, String> entry : map.entrySet()) {
            if (code.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
