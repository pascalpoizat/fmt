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

package models.lts;

import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.*;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class LtsModel extends AbstractModel {
    // only directed graphs for now on
    // Next Release : support more of the graphviz format

    private String name;
    private Map<String, LtsState> states; // state id -> state
    private List<LtsTransition> transitions;

    public LtsModel() {
        this(null);
    }

    public LtsModel(String name) {
        super();
        this.name = name;
        this.states = new HashMap<String, LtsState>();
        this.transitions = new ArrayList<LtsTransition>();
    }

    public String getName() {
        return name;
    }

    public Collection<LtsState> getStates() {
        return states.values();
    }

    public List<LtsTransition> getTransitions() {
        return transitions;
    }

    public Collection<String> getStateIds() { return states.keySet(); }

    @Override
    public String getSuffix() {
        return "lts";
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        throw new NotImplementedException();
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        // defaults to DOT format
        try {
            return this.modelToString(new DotLtsWriter());
        } catch (IllegalResourceException e) {
            return null;
        } // impossible
    }

    @Override
    public void cleanUp() {
        this.name = null;
        this.states = null;
        this.transitions = null;
        super.cleanUp();
    }


    public LtsState addState(String id) {
        LtsState rtr = new LtsState(id);
        states.put(id, rtr);
        return rtr;
    }

    public LtsTransition addTransition(String source, String target, LtsLabel label) {
        LtsTransition rtr = new LtsTransition(source, target, label);
        transitions.add(rtr);
        return rtr;
    }

}
