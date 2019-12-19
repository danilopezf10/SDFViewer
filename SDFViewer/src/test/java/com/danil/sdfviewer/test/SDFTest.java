package com.danil.sdfviewer.test;

import com.danil.sdfviewer.bean.InvalidSDFileException;
import org.junit.Test;
import static org.junit.Assert.*;
import com.danil.sdfviewer.bean.SDFBean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.simple.parser.ParseException;

/**
 *
 * @author danil
 */
public class SDFTest {

    @Test
    public void jsonGetterNeverReturnsNull() {
        assertNotNull(SDFBean.getCompoundsArray());
    }

    @Test
    public void jsonCreatedAfterReadingValidSDFile() throws IOException, ParseException, InvalidSDFileException {
        String str = "filler\n"     
                + "M  END\n"
                + "> <ID>\n"            //Fake input representing a valid .sdf file
                + "0001-0001\n"
                + "\n"
                + "$$$$";
        InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        SDFBean.generateJsonFile(is);
        assertNotNull(SDFBean.getCompoundsArray());
        assert (SDFBean.getCompoundsArray().size() > 0);
    }

    @Test(expected = InvalidSDFileException.class)
    public void readingInvalidSDFileThrowsInvalidSDFileException() throws IOException, ParseException, InvalidSDFileException {
        String str = "filler\n"
                + //  "M  END\n"+
                "> <ID>\n"
                + "0001-0001\n"
                + "\n"
                + "$$$$";
        InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        SDFBean.generateJsonFile(is);
    }

    @Test(expected = InvalidSDFileException.class)
    public void readingEmptySDFileThrowsInvalidSDFileException() throws IOException, ParseException, InvalidSDFileException {
        String str = "\n";
        InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        SDFBean.generateJsonFile(is);
    }
}
