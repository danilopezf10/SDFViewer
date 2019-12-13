package com.danil.sdfviewer.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author danil
 */
@Stateful
public class SDFBean {

    public static void generateJsonFile(InputStream fileContent) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(fileContent));
        List<String> lines = new ArrayList<>();

        String st = br.readLine();
        lines.add("[");
        while (st != null) {
            lines.add("  {");
            String structure = "    \"Structure\": \"" + st;
            while ((st = br.readLine()).compareTo("M  END") != 0) {   //Read "molfile", write it to "Structure" field
                structure = structure + st + "\\n";
            }
            lines.add(structure + st + "\",");

            st = br.readLine();
            while (st.compareTo("$$$$") != 0) {     //Read the rest of items
                String itemName = "    \"" + st.substring(st.indexOf('<') + 1, st.lastIndexOf('>')) + "\": \"";
                String itemValues = "";
                while ((st = br.readLine()).compareTo("") != 0) {
                    itemValues = itemValues + st + " ";
                }
                if ((st = br.readLine()).compareTo("$$$$") != 0) {    // We've got more items in the compound
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
        Path file = Paths.get(System.getProperty("java.io.tmpdir") + "\\sdfv_output.json");
        Files.write(file, lines, StandardCharsets.UTF_8);
    }
}
