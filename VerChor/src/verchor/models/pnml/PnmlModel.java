package verchor.models.pnml;

import fr.lip6.move.pnml.ptnet.*;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import verchor.models.base.IllegalResourceException;
import verchor.models.base.Model;

import java.io.*;
import java.util.List;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class PnmlModel extends Model {

    private PetriNet model;
    private PtnetFactory factory;

    public PnmlModel() {
        super();
        factory = PtnetFactory.eINSTANCE;
        model = factory.createPetriNet();
    }

    @Override
    public String getSuffix() {
        return "pnml";
    }

    @Override
    public boolean isLoaded() {
        return (model != null);
    }

    @Override
    public void load() {
        // TODO implement the loading of a PNML model from a file
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        FileWriter fw = new FileWriter(resource.getAbsolutePath());
        if (fw == null) {
            throw new IllegalResourceException("Cannot open output resource");
        }
        fw.write(model.toPNML());
        fw.close();
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

    public List<Page> getPages() {
        return model.getPages();
    }

}

