package transformations.bpmn2cif;

import models.choreography.bpmn.BpmnFactory;
import models.choreography.cif.CifFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import transformations.base.ITransformer;

/**
 * Created by pascalpoizat on 01/03/2014.
 */
public class Bpmn2CifTransformerTest {
    BpmnFactory bpmnFactory;
    CifFactory cifFactory;
    ITransformer trans;

    @DataProvider
    public Object[][] inputData() {
        return new Object[][] {
                {"/Users/pascalpoizat/IdeaProjects/fmt/examples/atomic_test_cases/simple.bpmn"},
                {"/Users/pascalpoizat/IdeaProjects/fmt/examples/atomic_test_cases/repeated_exchange.bpmn"},
                {"/Users/pascalpoizat/IdeaProjects/fmt/examples/comanche/comanche.bpmn"},
                {"/Users/pascalpoizat/IdeaProjects/fmt/examples/online_shopping/online_shopping_v1.bpmn"},
                {"/Users/pascalpoizat/IdeaProjects/fmt/examples/online_shopping/online_shopping_v2.bpmn"}
        };
    }

    @BeforeMethod // could possibly be @BeforeClass
    public void setUp() throws Exception {
        bpmnFactory = BpmnFactory.getInstance();
        cifFactory = CifFactory.getInstance();
        trans = new Bpmn2CifTransformer(bpmnFactory, cifFactory);
        trans.setVerbose(true);
        trans.about();
    }

    @Test(dataProvider = "inputData")
    public void testTransform(String filename) {
        try {
        trans.setResources(filename);
        trans.load();
        trans.transform();
        trans.dump();
        }
        catch(Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    @AfterMethod
    public void tearDown() throws Exception {
        trans.cleanUp();
    }
}
