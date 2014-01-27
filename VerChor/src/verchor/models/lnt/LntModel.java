package verchor.models.lnt;

import verchor.models.base.IllegalModelException;
import verchor.models.base.IllegalResourceException;
import verchor.models.base.Model;

import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class LntModel extends Model {

    private String model;   // simple version : a String
    // TODO create classes for LNT

    public LntModel() {
        super();
        model = null;
    }

    @Override
    public String getSuffix() {
        return "lnt";
    }

    @Override
    public boolean isLoaded() {
        return (model!=null);
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        // TODO implement the loading of a LNT model from a resource
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        // TODO implement the dumping of an LNT model from a resource
    }

    @Override
    public void finalize() {

    }
}
