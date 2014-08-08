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

import models.base.IllegalResourceException;
import models.base.ModelWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class LtsTransition {

    private String source_state_id;
    private String target_state_id;
    private LtsLabel label;

    public LtsTransition(String source, String target, LtsLabel label) {
        this.source_state_id = source;
        this.target_state_id = target;
        this.label = label;
    }

    public String getSource() {
        return source_state_id;
    }

    public String getTarget() {
        return target_state_id;
    }

    public LtsLabel getLabel() { return label; }

    @Override
    public String toString() {
        // defaults to DOT format
        try {
            return this.modelToString(new DotLtsWriter());
        } catch (RuntimeException e) {
            return null;
        } // impossible
    }

    public String modelToString(ModelWriter writer) throws RuntimeException {
        try {
            if (!(writer instanceof LtsWriter)) {
                throw new IllegalResourceException(String.format("Wrong kind of writer (%s), should be %s",
                        writer.getClass().toString(),
                        LtsWriter.class));
            }
            LtsWriter ltsWriter = (LtsWriter) writer;
            return ltsWriter.modelToString(this);
        } catch (IllegalResourceException e) {
            throw new RuntimeException(e); // BAD TRICK DUE TO Java 1.8 support for exceptions in map()
        }
    }

}
