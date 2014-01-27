package verchor.models.bpmn;

// java base
import java.io.IOException;
import java.util.List;

// jar Eclipse : org.eclipse.bpmn2_0.7.0.[...].jar
import org.eclipse.bpmn2.*;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;

// jar Eclipse : org.eclipse.emf.ecore_2.9.1.[...].jar
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

// jar Eclipse : org.eclipse.emf.common_2.9.1.[...].jar
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;

// jar Eclipse : org.eclipse.emf.ecore.xmi_2.9.1.[...].jar
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl; // do not remove

// VerChor
import verchor.models.base.IllegalModelException;
import verchor.models.base.IllegalResourceException;
import verchor.models.base.Model;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class BpmnModel extends Model {

    public static String MAIN_CLASS = "Choreography";

    private Choreography model;

    public BpmnModel() {
        super();
        model = null; // TODO implement
    }

    @Override
    public String getSuffix() {
        return "bpmn";
    }

    @Override
    public boolean isLoaded() {
        return (model != null);
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        loadEMF();
    }

    public void loadEMF() throws IOException, IllegalResourceException, IllegalModelException {
        // works as a stand-alone application and within the Eclipse IDE (for this, it requires jars from the BPMN2 modeller + EMF)
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSuffix(), new Bpmn2ResourceFactoryImpl());
        }
        // load resource
        URI uri = URI.createURI(resource.getPath());
        Resource res = new ResourceSetImpl().getResource(uri, true);
        EObject root = res.getContents().get(0);
        // search for the choreography instance
        Definitions definitions;
        boolean found = false;
        if (root instanceof DocumentRoot) {
            definitions = ((DocumentRoot) root).getDefinitions();
        } else {
            definitions = (Definitions) root;
        }
        if (definitions.eContents().size() > 0) {
            for (EObject definition : definitions.eContents()) {
                if (definition.eClass().getName().equals(MAIN_CLASS)) {
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

    public void dumpEMF() throws IOException, IllegalResourceException {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSuffix(), new Bpmn2ResourceFactoryImpl());
        }
        // save resource
        URI uri = URI.createURI(resource.getPath());
        Resource res = new ResourceSetImpl().createResource(uri);
        res.getContents().add(model);
        res.save(null);
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
    public void finalize() {

    }

}

