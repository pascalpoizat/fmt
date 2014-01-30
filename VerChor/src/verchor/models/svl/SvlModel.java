package verchor.models.svl;

import verchor.models.base.IllegalModelException;
import verchor.models.base.IllegalResourceException;
import verchor.models.base.Model;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class SvlModel extends Model {

    private String model;   // simple version : a String
    // TODO create classes for SVL

    public SvlModel() {
        super();
        model = "";
    }

    @Override
    public String getSuffix() {
        return "svl";
    }

    @Override
    public boolean isLoaded() {
        return (model!=null);
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        // TODO implement the loading of an SVL model from a resource
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
            FileWriter fw = new FileWriter(resource.getAbsolutePath());
            if (fw == null) {
                throw new IllegalResourceException("Cannot open output resource");
            }
            fw.write(model);
            fw.close();
    }

    @Override
    public void finalize() {

    }
}
