/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * {description}
 * Copyright (C) 2014  pascalpoizat
 * emails: pascal.poizat@lip6.fr
 */

package models.choreography.bpmn;

// java base
import java.io.IOException;
import java.util.List;

// jar Eclipse : org.eclipse.bpmn2_0.7.0.[...].jar
import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import org.eclipse.bpmn2.*;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;

// jar Eclipse : org.eclipse.emf.ecore_2.9.1.[...].jar
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

// jar Eclipse : org.eclipse.emf.common_2.9.1.[...].jar
import org.eclipse.emf.common.util.URI;

// jar Eclipse : org.eclipse.emf.ecore.xmi_2.9.1.[...].jar


// VerChor


/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class BpmnModel extends AbstractModel {

    public static final String MAINCLASS = "Choreography";

    private Choreography model;

    public BpmnModel() {
        super();
        model = null; // NEXT RELEASE : construct base model
    }

    @Override
    public String getSuffix() {
        return "bpmn";
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        loadEMF();
    }

    private void loadEMF() throws IOException, IllegalResourceException, IllegalModelException {
        // works as a stand-alone application and within the Eclipse IDE
        // note : requires jars from the BPMN2 modeller + EMF
        final URI uri = URI.createURI(getResource().getPath());
        final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        reg.getExtensionToFactoryMap().put(getSuffix(), new Bpmn2ResourceFactoryImpl());
        final Resource res = new ResourceSetImpl().getResource(uri,true);
        res.load(null);
// OLD
//        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
//            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSuffix(), new Bpmn2ResourceFactoryImpl());
//        }
//        URI uri = URI.createURI(getResource().getPath());
//        Resource res = new ResourceSetImpl().getResource(uri, true);
        final EObject root = res.getContents().get(0);
        // search for the choreography instance
        Definitions definitions;
        boolean found = false;
        if (root instanceof DocumentRoot) {
            definitions = ((DocumentRoot) root).getDefinitions();
        } else {
            definitions = (Definitions) root;
        }
        if (definitions.eContents().size() > 0) {
            for (final EObject definition : definitions.eContents()) {
                if (definition.eClass().getName().equals(MAINCLASS)) {
                    model = (Choreography) definition;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalModelException("No choreography definition found in the model");
            }
        } else {
            throw new IllegalModelException("No definitions found in the model");
        }
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        dumpEMF();
    }

    private void dumpEMF() throws IOException, IllegalResourceException {
        /* tbc, should save the whole BPMN model
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSuffix(), new Bpmn2ResourceFactoryImpl());
        }
        // save resource
        URI uri = URI.createURI(getResource().getPath());
        Resource res = new ResourceSetImpl().createResource(uri);
        res.getContents().add(...); // more than model, the whole document read
        res.save(null);
        */
        throw new UnsupportedOperationException();
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

    @Override
    public void cleanUp() {
        model = null; // NEXT RELEASE : construct base model
        super.cleanUp();
    }

}

