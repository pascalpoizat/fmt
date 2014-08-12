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

package transformations.base;

import java.io.File;

import models.base.*;

import java.io.IOException;

public abstract class AbstractTransformer implements Transformer {

    // NEXT RELEASE a generaliser en prenant en compte des transformations n->m et non plus 1->1

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

    @Override
    public final void setResources(AbstractModel inputModel, AbstractModel outputModel, AbstractModelReader reader, AbstractModelWriter writer) throws IllegalResourceException {
        if(inputModel==null || outputModel==null || reader==null || writer==null || inputModel.getResource()==null || outputModel.getResource()==null) {
            throw new IllegalResourceException("Resources are not correctly set");
        }
        this.inputModel = inputModel;
        this.outputModel = outputModel;
        this.reader = reader;
        this.writer = writer;
        message("Input model: "+inputModel.getResource().getAbsolutePath());
        message(String.format("-- reader: %s (%s suffix)",reader.getClass().toString(),reader.getSuffix()));
        message("Output model: "+outputModel.getResource().getAbsolutePath());
        message(String.format("-- writer: %s (%s suffix)",writer.getClass().toString(),writer.getSuffix()));
        message("** Resources set");
    }

    @Override
    public final void load() throws IOException, IllegalResourceException, IllegalModelException {
        try {
            inputModel.modelFromFile(reader);
            message("** Input model loaded");
        } catch (IllegalResourceException | IllegalModelException | IOException e) {
            error(e.getMessage());
            throw e;
        }
    }

    @Override
    public abstract void transform() throws IllegalModelException;

    @Override
    public final void dump() throws IOException, IllegalResourceException, IllegalModelException {
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
    public abstract void about();

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
