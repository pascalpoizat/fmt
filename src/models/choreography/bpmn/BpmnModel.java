package models.choreography.bpmn;

import java.util.List;
import models.base.IllegalModelException;
import models.base.AbstractModel;
import org.eclipse.bpmn2.*;

public class BpmnModel extends AbstractModel {

    private Choreography model;

    public BpmnModel() {
        super();
        model = null; // NEXT RELEASE implement
    }

    public String getName() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("BPMN model is incorrect (unset)");
        } else if (model.getName() == null) {
            throw new IllegalModelException("BPMN model is incorrect (no choreography name)");
        } else {
            return model.getName();
        }
    }

    public String getId() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("BPMN model is incorrect (unset)");
        } else if (model.getId() == null) {
            throw new IllegalModelException("BPMN model is incorrect (no choreography id)");
        } else {
            return model.getId();
        }
    }

    public List<Participant> getParticipants() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("BPMN is incorrect (unset)");
        } else if (model.getParticipants() == null) {
            throw new IllegalModelException("BPMN model is incorrect (no participant list)");
        } else {
            return model.getParticipants();
        }

    }

    public List<FlowElement> getFlowElements() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("BPMN is incorrect (unset)");
        } else if (model.getFlowElements() == null) {
            throw new IllegalModelException("BPMN model is incorrect (no flow element list)");
        } else {
            return model.getFlowElements();
        }

    }

    public void setModel(Choreography definition) {
        model = definition;
    }

    public Choreography getModel() {
        return model;
    }
}

