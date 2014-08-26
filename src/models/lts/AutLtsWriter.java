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
 * Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 * emails: pascal.poizat@lip6.fr
 */

package models.lts;

// Java 1.7
/*
import helpers.Map;
import helpers.StringJoiner;
import java.lang.reflect.Method;
import java.util.ArrayList;
*/

import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A writer to dump LTS in AUT (Aldebaran) format
 * format:
 * des(0,T,S)
 * (x,"l",y)
 * (x',"l'",y')
 * ...
 * where:
 * T is the number of transitions
 * S is the number of states
 * x, x', ... are source states of transitions (MUST BE INTEGERS)
 * y, y', ... are target states of transitions (MUST BE INTEGERS)
 * l, l', ... are labels of transitions
 * important:
 * some tools require that state ids (x,x',y,y',...) begin at 0 and go up to S-1 without any integer missing
 * this means state ids may have to be changed to support this (state_mapping is used for this)
 * Created by Pascal Poizat (@pascalpoizat) on 04/08/2014.
 */
public class AutLtsWriter extends AbstractStringLtsWriter {

    private Map<String, Integer> state_mapping = new HashMap<>();
    boolean state_mapping_is_built;

    public AutLtsWriter() {
        state_mapping_is_built = false;
    }

    public String getSuffix() {
        return "aut";
    }

    @Override
    public String modelToString(AbstractModel model) throws IllegalResourceException, IllegalModelException {
        checkModel(model, LtsModel.class);
        LtsModel ltsModel = (LtsModel) model;
        String transitions_as_string;
        int nb_transitions = ltsModel.getTransitions().size();
        // build the mapping between state ids and integers (required by the AUT format)
        build_state_mapping(ltsModel);
        // build string for states
        // none in this format only transitions are saved
        // build string for transitions
        try {
            // Java 1.8
            transitions_as_string = ltsModel.getTransitions().stream().map((x) -> x.modelToString(ltsModel, this)).collect(Collectors.joining("\n"));
        } catch (RuntimeException e) {
            return null; // impossible
        }
        return String.format("des (0,%d,%d)\n%s\n", nb_transitions, state_mapping.keySet().size(), transitions_as_string);
    }

    @Override
    String modelToString(LtsModel ltsModel, LtsState ltsState) {
        // nothing to do
        return "";
    }

    @Override
    String modelToString(LtsModel ltsModel, LtsTransition ltsTransition) {
        String rtr = "";
        if (!state_mapping_is_built) {
            // state_mapping MUST have been built before
            build_state_mapping(ltsModel);
        }
        rtr += String.format("(%s,\"%s\",%s)",
                state_mapping.get(ltsTransition.getSource()),
                ltsTransition.getLabel().modelToString(ltsModel, this),
                state_mapping.get(ltsTransition.getTarget()));
        return rtr;
    }

    @Override
    String modelToString(LtsModel ltsModel, LtsLabel ltsLabel) {
        return ltsLabel.getLabel();
    }

    private void build_state_mapping(LtsModel model) {
        // F = ()
        // i = 0
        // for each transition tt = s--l-->t
        //  if s not in dom(F), add (s,i) in F and i=i+1
        //  if t not in dom(F), add (t,i) in F and i=i+1
        // end for
        state_mapping = new HashMap<>();
        int i = 0;
        for (LtsTransition tt : model.getTransitions()) {
            String s = tt.getSource();
            String t = tt.getTarget();
            if (!state_mapping.containsKey(s)) {
                state_mapping.put(s, i++);
            }
            if (!state_mapping.containsKey(t)) {
                state_mapping.put(t, i++);
            }
        }
        state_mapping_is_built = true;
    }


}
