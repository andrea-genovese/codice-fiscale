package dev.andreagenovese.CodiceFiscale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
/**
 * This class contains methods to get the Codice Catastale Nazionale (also known
 * as Codice Belfiore) assigned to geographical entities (Comuni and
 * countries) and viceversa
 * 
 */
@SuppressWarnings("all")
public class CodiceCatastale {
    private static Map<String, String> map;
    static {
        map = new HashMap<String, String>();
        ClassLoader classLoader = CodiceCatastale.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("codiciCatastali.ser");
        try {
            ObjectInputStream objInput = new ObjectInputStream(inputStream);
            Object obj = objInput.readObject();
            map = (Map<String, String>) obj;

        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
            try {
                map = parseFile("codiciCatastali.csv");
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    private static Map<String, String> parseFile(String fileName) throws IOException, URISyntaxException {
        Map<String, String> parsedFile = new HashMap<>();
        InputStream stream = CodiceCatastale.class.getClassLoader().getResourceAsStream(fileName);

        Scanner sc = new Scanner(stream, "UTF-8");
        while (sc.hasNextLine()) {
            String[] columns = sc.nextLine().split(",");
            parsedFile.put(columns[0].toUpperCase(Locale.ITALIAN), columns[1].toUpperCase(Locale.ITALIAN));
        }
        FileOutputStream fos = new FileOutputStream("codiciCatastali.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(map);
        oos.close();
        return parsedFile;

    }

    /**
     * This method returns the Codice Catastale for a given geographical name (case
     * insensitive)
     * 
     * 
     * @param luogo name of the entity
     * @return The corresponding Codice Catastale
     */
    public static String getCodiceCatastale(String luogo) {
        return map.get(luogo.toUpperCase(Locale.ITALIAN));
    }
    /**This method returns the name of an entity based on the supplied code
     * 
     * @param code A Codice Catastale
     * @return The name of the entity
     */
    public static String getName(String code) {
        code = code.toUpperCase(Locale.ITALIAN);
        for (Entry<String, String> entry : map.entrySet()) {
            if (code.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}