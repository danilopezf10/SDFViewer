package com.danil.sdfviewer.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danil
 */
public class SDFParser {
    /**
     * This method will generate a json file in the same path and with the same name as the input sd file
     * @param filepath path of the input sd file
     * @throws java.lang.Exception
     */
    public static void generateJsonFile(String filepath) throws Exception {
        File f = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<String> lines = new ArrayList<>();
        
        String st = br.readLine();
        lines.add("[");
        while (st != null) {
            lines.add("  {");
            String auxStr = "    \"Structure\": \"" + st;
            while ((st = br.readLine()).compareTo("M  END") != 0) {   //Read "molfile", write it to "Structure" field
                auxStr = auxStr+st+"\\n";
            }
            lines.add(auxStr+st+"\",");
            
            st = br.readLine();
            while (st.compareTo("$$$$") != 0) {     //Read the rest of items
                auxStr = "    \"" + st.substring(st.indexOf('<') + 1, st.lastIndexOf('>')) + "\": \"";
                while ((st = br.readLine()).compareTo("") != 0) {
                    auxStr= auxStr + st + " ";
                }
                if((st = br.readLine()).compareTo("$$$$") != 0){    // We've got more items in the compound
                    lines.add(auxStr.trim()+"\",");
                }else{
                    lines.add(auxStr.trim()+"\"");
                }
            }
            if ((st = br.readLine()) != null) {     //We've got more compounds to read
                lines.add("  },");
            } else {                              //We are done reading the file
                lines.add("  }");
            }
        }
        lines.add("]");
        Path file = Paths.get(filepath.replaceAll(".sdf", ".json"));
        Files.write(file, lines, StandardCharsets.UTF_8);
    }
}
