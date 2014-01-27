package verchor.transformations.base;

import java.io.File;

import verchor.models.base.IllegalModelException;
import verchor.models.base.IllegalResourceException;
import verchor.models.base.Model;
import verchor.models.base.ModelFactory;

import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class ATransformer implements ITransformer {

    // TODO a generaliser en prenant en compte des transformations n->m et non plus 1->1

    protected boolean verbose;            // verbose model (on/off) for the model transformation
    protected File working_directory;     // working directory (path)
    protected String basename;            // base name (file)
    protected File in_file;               // input File
    protected File out_file;              // output File
    protected Model in_model;             // input model (read from path/file.in_suffix)
    protected Model out_model;            // output model (written to path/file.out_suffix)
    protected ModelFactory factory_in;    // factory to create input model
    protected ModelFactory factory_out;   // factory to create output model

    public ATransformer(ModelFactory fin, ModelFactory fout) {
        verbose = false;
        working_directory = null;
        basename = null;
        in_file = null;
        out_file = null;
        factory_in = fin;
        factory_out = fout;
        in_model = factory_in.create();
        out_model = factory_out.create();
    }

    @Override
    public void setResources(String path_to_input_resource) throws IllegalResourceException {
        if (path_to_input_resource == null) {
            IllegalResourceException e = new IllegalResourceException("Wrong resource path (" + path_to_input_resource + ")");
            error(e.getMessage());
            throw e;
        }
        if (!path_to_input_resource.endsWith("." + in_model.getSuffix())) {
            IllegalResourceException e = new IllegalResourceException("Wrong file suffix (should be " + in_model.getSuffix() + ")");
            error(e.getMessage());
            throw e;
        }
        in_file = new File(path_to_input_resource).getAbsoluteFile();
        working_directory = in_file.getParentFile();
        basename = in_file.getName().substring(0, in_file.getName().length() - (in_model.getSuffix().length() + 1));
        out_file = new File(working_directory, basename + "." + out_model.getSuffix()).getAbsoluteFile();
        in_model.setResource(in_file);
        out_model.setResource(out_file);
        message("Resources set");
        message("\tworking dir.:\t" + working_directory);
        message("\tbase name:\t\t" + basename);
        message("\tinput file:\t\t" + in_file);
        message("\toutput file:\t" + out_file);
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        try {
            message("Loading input model (" + in_model.getSuffix() + ")");
            in_model.load();
            message("Input model loaded");
        } catch (IllegalResourceException e) {
            error(e.getMessage());
            throw e;
        } catch (IllegalModelException e) {
            error(e.getMessage());
            throw e;
        } catch (IOException e) {
            error(e.getMessage()); // TODO catch FileNotFoundException eg in case of a BPMN->CIF transformation with a non existing file the exception is not catched.
            throw e;
        }
    }

    @Override
    public abstract void transform() throws IllegalModelException;

    @Override
    public void dump() throws IOException, IllegalResourceException {
        try {
            message("Generating output model (" + out_model.getSuffix() + ")");
            out_model.dump();
            message("Output model generated");
        } catch (IOException e) {
            error(e.getMessage());
            throw e;
        } catch (IllegalResourceException e) {
            error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void setVerbose(boolean mode) {
        verbose = mode;
    }

    @Override
    public void message(String msg) {
        if (this.verbose) {
            System.out.println("" + msg);
        }
    }

    @Override
    public void error(String msg) {
        System.out.println("ERROR: " + msg);
    }

    @Override
    public void warning(String msg) {
        System.out.println("WARNING: " + msg);
    }

    @Override
    public abstract void about();

    @Override
    public void finalize() {
        if (in_model != null) {
            in_model.finalize();
        }
        if (out_model != null) {
            out_model.finalize();
        }
    }
}
