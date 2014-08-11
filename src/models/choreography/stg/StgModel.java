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

package models.choreography.stg;

import fr.lri.schora.solver.Z3SMT;
import fr.lri.schora.stg.STG;
import fr.lri.schora.util.parser.ParserException;
import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * Symbolic Transition Graphs for choreographies
 * events in labels are of the form msg[x,y], e.g., msg[x,y]., msg[x,y]?, msg[x,y]!
 * see http://github.com/pascalpoizat/schora for more information
 */
public class StgModel extends AbstractModel {

    private static String Z3PATH = null;
    private STG model;

    public StgModel() {
        super();
        model = null;
    }

    @Override
    public String getSuffix() {
        return "stg";
    }

    /**
     * returns the dot representation for the model
     * used by StgWriters
     * delegates to STG::toDotFormat()
     *
     * @return the dot representation for the model
     */
    String toDot() {
        return model.toDotFormat();
    }

    /**
     * returns the dot representation for the model
     * used by StgWriters
     * delegates to STG::toStgFormat()
     *
     * @return the stg representation for the model
     */
    String toStg() {
        return model.toStgFormat();
    }

    /**
     * returns the STG model that is parsed from a string in the stg format
     * users by StgReaders
     * delegates to STG::parser()
     *
     * @param stringModel   the dot representation of the model to parse
     * @param onlyReachable if true, performs reachability cleaning after parsing
     * @return
     * @throws IllegalResourceException
     */
    static STG fromStg(String stringModel, boolean onlyReachable) throws IllegalResourceException {
        STG readModel, reachableModel;
        try {
            readModel = STG.parser(stringModel);
            if (onlyReachable) {
                reachableModel = readModel.getReachableSTG(new Z3SMT(Z3PATH));
                return reachableModel;
            } else {
                return readModel;
            }
        } catch (ParserException e) {
            throw new IllegalResourceException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalResourceException(e.getMessage());
        }
    }

    /**
     * sets the model
     * used by StgReaders
     *
     * @param model the STG model to encapsulate
     */
    void setModel(STG model) {
        this.model = model;
    }

    /**
     * sets up the Z3 path, to be used while parsing models (if reachability cleaning is performed)
     *
     * @param path path to the z3 command
     */
    static void setZ3PATH(String path) {
        Z3PATH = path;
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        throw new NotImplementedException();
    }

    @Override
    public void load() throws IOException, IllegalResourceException {
        throw new NotImplementedException();
    }

    @Override
    public void cleanUp() {
        model = null;
        super.cleanUp();
    }
}
