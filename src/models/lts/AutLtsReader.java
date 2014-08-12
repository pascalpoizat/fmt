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
import models.base.AbstractStringModelReader;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutLtsReader extends AbstractStringModelReader {

    private Pattern description_line_pattern;
    private Pattern transition_line_pattern;

    public AutLtsReader() {
        description_line_pattern = Pattern.compile("^\\s*des\\s*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)\\s*$");
        transition_line_pattern = Pattern.compile("^\\s*\\(\\s*([0-9]+)\\s*,\\s*\"([^)]*)\"\\s*,\\s*([0-9]+)\\s*\\)\\s*$");
    }

    @Override
    public String getSuffix() {
        return "aut";
    }

    @Override
    public void modelFromString(AbstractModel model, String stringModel) throws IllegalResourceException, IllegalModelException {
        checkModel(model, LtsModel.class);
        LtsModel ltsModel = (LtsModel) model;
        String lines[] = stringModel.split("\n");
        String line;
        String source;
        String target;
        String label;
        // check that there is a first line (description)
        if (lines.length == 0) {
            throw new IllegalModelException("Incorrect model, empty information");
        }

        // get first line and check it
        line = lines[0];
        Matcher matcher = description_line_pattern.matcher(line);
        if (!matcher.find()) {
            throw new IllegalModelException(String.format("Incorrect model at '%s', could not find 'des (x,y,z)' line", line));
        }
        try {
            Integer.parseInt(matcher.group(2));
            Integer.parseInt(matcher.group(3));
        } catch (NumberFormatException e) {
            throw new IllegalModelException(String.format("Incorrect model at '%s'", line));
        }

        // for all other lines, check them and add transition to model
        for (int i = 1; i < lines.length; i++) {
            matcher = transition_line_pattern.matcher(lines[i]);
            if (!matcher.find()) {
                throw new IllegalModelException(String.format("Incorrect model at '%s', could not find '(source,\"label\",transition)' line", line));
            }
            source = matcher.group(1);
            label = matcher.group(2);
            target = matcher.group(3);
            if (!ltsModel.getStateIds().contains(source)) {
                ltsModel.addState(source);
            }
            if (!ltsModel.getStateIds().contains(target)) {
                ltsModel.addState(target);
            }
            ltsModel.addTransition(source, target, new LtsLabel(label));
        }
    }
}
