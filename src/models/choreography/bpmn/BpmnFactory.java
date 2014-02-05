package models.choreography.bpmn;

import models.base.ModelFactory;
import models.base.Model;


/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class BpmnFactory extends ModelFactory {

    private static BpmnFactory instance;

    private BpmnFactory() {
    }

    public static BpmnFactory getInstance() {
        if (instance == null)
            instance = new BpmnFactory();
        return instance;
    }

    public Model create() {
        Model model = new BpmnModel();
        return model;
    }

}
