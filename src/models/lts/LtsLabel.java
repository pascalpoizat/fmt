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

import models.base.AbstractStringModelWriter;
import models.base.IllegalResourceException;

/**
 * Created by pascalpoizat on 05/08/2014.
 */
public class LtsLabel {

    private String label;

    public LtsLabel() {
        this("");
    }

    public LtsLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String modelToString(LtsModel ltsModel, AbstractStringModelWriter writer) throws RuntimeException {
        try {
            if (!(writer instanceof AbstractLtsWriter)) {
                throw new IllegalResourceException(String.format("Wrong kind of writer (%s), should be %s",
                        writer.getClass().toString(),
                        AbstractLtsWriter.class));
            }
            AbstractLtsWriter ltsWriter = (AbstractLtsWriter) writer;
            return ltsWriter.modelToString(ltsModel, this);
        } catch (IllegalResourceException e) {
            throw new RuntimeException(e);  // BAD TRICK DUE TO Java 1.8 support for exceptions in map()
        }
    }
}
