package com.danil.sdfviewer.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author danil
 */
@Stateful
public class SDFBean {

    private static final String FILEPATH = System.getProperty("java.io.tmpdir") + "\\sdfv_output.json";     //Path to temp-files directory
    private static JSONArray compoundsArray;

    public static void generateJsonFile(InputStream fileContent) throws IOException, ParseException, InvalidSDFileException {
        List<String> lines = readSDF(fileContent);
        Path file = Paths.get(FILEPATH);
        Files.write(file, lines, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        compoundsArray = (JSONArray) parser.parse(new FileReader(FILEPATH));
    }

    private static List<String> readSDF(InputStream fileContent) throws InvalidSDFileException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(fileContent));
        List<String> lines = new ArrayList<>();
        try {
            String st = br.readLine();
            lines.add("[");

            while (st != null) {
                lines.add("  {");
                String structure = "    \"Structure\": \"" + st + "\\r\\n";
                while ((st = br.readLine()).compareTo("M  END") != 0) {   //Read .mol file data, write it to "Structure" field
                    structure = structure + st + "\\r\\n";
                }
                lines.add(structure + st + "\\r\\n\",");

                st = br.readLine();
                while (st.compareTo("$$$$") != 0) {     //Read the rest of items
                    String itemName = "    \"" + st.substring(st.indexOf('<') + 1, st.lastIndexOf('>')) + "\": \"";
                    String itemValues = "";
                    while ((st = br.readLine()).compareTo("") != 0) {
                        itemValues = itemValues + st + " ";
                    }
                    if ((st = br.readLine()).compareTo("$$$$") != 0) {    //We've got more items in the compound
                        lines.add(itemName + itemValues.trim() + "\",");
                    } else {
                        lines.add(itemName + itemValues.trim() + "\"");
                    }
                }
                if ((st = br.readLine()) != null) {     //We've got more compounds to read
                    lines.add("  },");
                } else {                              //We are done reading the file
                    lines.add("  }");
                }
            }
            lines.add("]");

        } catch (NullPointerException ex) {
            throw new InvalidSDFileException("Invalid .sdf file, please check format errors");
        }
        if (lines.size() == 2) {
            throw new InvalidSDFileException("Uploaded file is empty");
        }

        return lines;
    }

    public static JSONArray getCompoundsArray() {
        if (compoundsArray == null) {
            compoundsArray = new JSONArray();
        }
        return compoundsArray;
    }

    public static void setCompoundsArray(JSONArray compoundsArray) {
        SDFBean.compoundsArray = compoundsArray;
    }
}
