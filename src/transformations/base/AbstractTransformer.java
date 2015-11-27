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
 * fmt
 * Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 * emails: pascal.poizat@lip6.fr
 */

package transformations.base;

import models.base.*;

import java.io.File;
import java.io.IOException;

public abstract class AbstractTransformer implements Transformer {

    // TODO: checkModel could be static
    // NEXT RELEASE to generalize using n->m transformations instead of 1->1 ones

    protected boolean verbose;
    protected AbstractModel inputModel;
    protected AbstractModel outputModel;
    protected AbstractModelReader reader;
    protected AbstractModelWriter writer;

    public AbstractTransformer() {
        verbose = false;
        inputModel = null;
        outputModel = null;
        reader = null;
        writer = null;
    }

    // checks if all is ok with a model before working with it
    public boolean checkModel(AbstractModel model, Class expectedClass) throws IllegalResourceException, IllegalModelException {
        if (model == null) {
            throw new IllegalResourceException("Model is null");
        }
        if (model.getResource() == null) {
            throw new IllegalResourceException("Resource is not set");
        }
        if (!expectedClass.isAssignableFrom(model.getClass())) {
            throw new IllegalModelException(String.format("Wrong kind of model (%s), should be %s or a subclass of it",
                    model.getClass().toString(),
                    expectedClass.toString()));
        }
        return true;
    }

    @Override
    public final void setResources(AbstractModel inputModel, AbstractModel outputModel, AbstractModelReader reader, AbstractModelWriter writer) throws IllegalResourceException {
        if (inputModel == null || outputModel == null || reader == null || writer == null || inputModel.getResource() == null || outputModel.getResource() == null) {
            throw new IllegalResourceException("Resources are not correctly set");
        }
        this.inputModel = inputModel;
        this.outputModel = outputModel;
        this.reader = reader;
        this.writer = writer;
        message("Input model: " + inputModel.getResource().getAbsolutePath());
        message(String.format("-- reader: %s (%s suffix)", reader.getClass().toString(), reader.getSuffix()));
        message("Output model: " + outputModel.getResource().getAbsolutePath());
        message(String.format("-- writer: %s (%s suffix)", writer.getClass().toString(), writer.getSuffix()));
        message("** Resources set");
    }

    /**
     * Checks if input resource is set (does not check if the file exists)
     *
     * @return true if so, else false
     */
    @Override
    public final boolean hasInputResourceSet() {
        return (inputModel != null && inputModel.getResource() != null);
    }

    /**
     * Checks if output resource is set (does not check if the file exists)
     *
     * @return true if so, else false
     */
    @Override
    public final boolean hasOutputResourceSet() {
        return (outputModel != null && outputModel.getResource() != null);
    }

    /**
     * Checks if both resources are set (does not check if the files exist)
     *
     * @return true if so, else false
     */
    @Override
    public final boolean hasResourcesSet() {
        return (hasInputResourceSet() && hasOutputResourceSet());
    }

    /**
     * Performs a transformation at the file level (reads input model file -> performs transformations -> writes output model file)
     * In lazy mode (lazy==true) performs this only if the output model file does not exist or if it is older than the input model file
     * Else (lazy==false) performs this in any case (ie even if the output file already exists and is up to date)
     *
     * @param lazy sets up lazy mode
     * @throws IOException
     * @throws IllegalResourceException
     * @throws IllegalModelException
     */
    @Override
    public final void run(boolean lazy) throws IOException, IllegalResourceException, IllegalModelException {
        if (!hasInputResourceSet()) {
            IllegalResourceException e = new IllegalResourceException("input model is not set");
            error(e.getMessage());
            throw e;
        }
        if (!hasOutputResourceSet()) {
            IllegalResourceException e = new IllegalResourceException("output model is not set");
            error(e.getMessage());
            throw e;
        }
        File fin = inputModel.getResource();
        File fout = outputModel.getResource();
        if (!fin.exists()) {
            IllegalResourceException e = new IllegalResourceException("specified input model file does not exist");
            error(e.getMessage());
            throw e;
        }
        if (!lazy || !fout.exists() || (fin.lastModified() >= fout.lastModified())) {
            load();
            transform();
            dump();
        } else {
            message("** Lazy mode and output file more recent than the input one");
        }
    }

    @Override
    public final void load() throws IOException, IllegalResourceException, IllegalModelException {
        if (!hasInputResourceSet()) {
            IllegalResourceException e = new IllegalResourceException("input model is not set");
            error(e.getMessage());
            throw e;
        }
        try {
            inputModel.modelFromFile(reader);
            message("** Input model loaded");
        } catch (IllegalResourceException | IllegalModelException | IOException e) {
            error(e.getMessage());
            throw e;
        }
    }

    @Override
    public final void dump() throws IOException, IllegalResourceException, IllegalModelException {
        if (!hasOutputResourceSet()) {
            IllegalResourceException e = new IllegalResourceException("output model is not set");
            error(e.getMessage());
            throw e;
        }
        try {
            outputModel.modelToFile(writer);
            message("** Output model generated");
        } catch (IOException | IllegalResourceException | IllegalModelException e) {
            error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void setVerbose(final boolean mode) {
        verbose = mode;
    }

    @Override
    public void message(final String msg) {
        if (this.verbose) {
            System.out.println(msg);
        }
    }

    @Override
    public void error(final String msg) {
        System.out.println("ERROR: " + msg);
    }

    @Override
    public void warning(final String msg) {
        System.out.println("WARNING: " + msg);
    }

    @Override
    public void cleanUp() {
        if (inputModel != null) {
            inputModel.cleanUp();
        }
        if (outputModel != null) {
            outputModel.cleanUp();
        }
        reader = null;
        writer = null;
    }
}
