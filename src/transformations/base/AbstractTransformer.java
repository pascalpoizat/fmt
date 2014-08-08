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

import models.base.AbstractModel;
import models.base.ModelFactory;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class AbstractTransformer implements Transformer {

    // NEXT RELEASE a generaliser en prenant en compte des transformations n->m et non plus 1->1

    protected boolean verbose;            // verbose model (on/off) for the model transformation
    protected File workingDirectory;     // working directory (path)
    protected String basename;            // base name (file)
    protected File inputFile;               // input File
    protected File outputFile;              // output File
    protected AbstractModel inputModel;             // input model (read from path/file.in_suffix)
    protected AbstractModel outputModel;            // output model (written to path/file.out_suffix)
    protected ModelFactory inputFactory;    // factory to create input model
    protected ModelFactory outputFactory;   // factory to create output model

    public AbstractTransformer(final ModelFactory fin, final ModelFactory fout) {
        verbose = false;
        workingDirectory = null;
        basename = null;
        inputFile = null;
        outputFile = null;
        inputFactory = fin;
        outputFactory = fout;
        inputModel = inputFactory.create();
        outputModel = outputFactory.create();
    }

    @Override
    public void setResources(final String inputResourcePath) throws IllegalResourceException {
        if (inputResourcePath == null) {
            final IllegalResourceException exception = new IllegalResourceException("Wrong resource path (null)");
            error(exception.getMessage());
            throw exception;
        }
        if (!inputResourcePath.endsWith("." + inputModel.getSuffix())) {
            final IllegalResourceException exception = new IllegalResourceException("Wrong file suffix (should be " + inputModel.getSuffix() + ")");
            error(exception.getMessage());
            throw exception;
        }
        inputFile = new File(inputResourcePath).getAbsoluteFile();
        workingDirectory = inputFile.getParentFile();
        basename = inputFile.getName().substring(0, inputFile.getName().length() - inputModel.getSuffix().length() + 1);
        outputFile = new File(workingDirectory, basename + "." + outputModel.getSuffix()).getAbsoluteFile();
        inputModel.setResource(inputFile);
        outputModel.setResource(outputFile);
        message("Resources set");
        message("\tworking dir.:\t" + workingDirectory);
        message("\tbase name:\t\t" + basename);
        message("\tinput file:\t\t" + inputFile);
        message("\toutput file:\t" + outputFile);
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        try {
            message("Loading input model (" + inputModel.getSuffix() + ")");
            inputModel.load();
            message("Input model loaded");
        } catch (IllegalResourceException | IllegalModelException | IOException e) {
            error(e.getMessage());
            throw e;
        }
    }

    @Override
    public abstract void transform() throws IllegalModelException;

    @Override
    public void dump() throws IOException, IllegalResourceException {
        try {
            message("Generating output model (" + outputModel.getSuffix() + ")");
            outputModel.dump();
            message("Output model generated");
        } catch (IOException | IllegalResourceException e) {
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
    }
}
