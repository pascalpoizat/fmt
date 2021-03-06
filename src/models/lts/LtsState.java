/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * fmt
 * Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 * emails: pascal.poizat@lip6.fr
 */

package models.lts;

import models.base.AbstractStringModelWriter;
import models.base.IllegalModelException;

public class LtsState {

    private String id;

    public LtsState(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String modelToString(LtsModel ltsModel, AbstractStringModelWriter writer) throws RuntimeException {
        try {
            if (!(writer instanceof AbstractStringLtsWriter)) {
                throw new IllegalModelException(String.format("Wrong kind of writer (%s), should be %s",
                        writer.getClass().toString(),
                        AbstractStringLtsWriter.class));
            }
            AbstractStringLtsWriter ltsWriter = (AbstractStringLtsWriter) writer;
            return ltsWriter.modelToString(ltsModel, this);
        } catch (IllegalModelException e) {
            throw new RuntimeException(e); // BAD TRICK DUE TO Java 1.8 support for exceptions in map()
        }
    }
}
