package models.dot;

import models.base.IllegalResourceException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class DotModelTest {

    private DotModel model;

    @BeforeClass
    public void setUp() throws Exception {
        model = new DotModel("foo");
    }

    @AfterClass
    public void tearDown() throws Exception {
        model = null;
    }

    @Test
    public void testToString() {
        try {
            model.setResource(new File("/tmp/test.dot"));
            model.addState("s0", null);
            model.addState("s1", null);
            model.addTransition("t1", "s0", "s1", null);
            model.dump();
        } catch (IllegalResourceException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
