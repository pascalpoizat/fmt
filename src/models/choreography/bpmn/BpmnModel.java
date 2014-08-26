/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * fmt
 * Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 * emails: pascal.poizat@lip6.fr
 */

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

