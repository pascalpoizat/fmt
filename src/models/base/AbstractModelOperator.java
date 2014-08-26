/**
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *   {description}
 *   Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 *   emails: pascal.poizat@lip6.fr
 */

package models.base;

public abstract class AbstractModelOperator {
    // returns the suffix of the files the writer works with
    public abstract String getSuffix();

    // checks if all is ok with a model before working with it
    public boolean checkModel(AbstractModel model, Class expectedClass) throws IllegalResourceException, IllegalModelException {
        if (model == null) {
            throw new IllegalResourceException("Model is null");
        }
        if (model.getResource() == null) {
            throw new IllegalResourceException("Resource is not set");
        }
        if (!model.getResource().getName().endsWith("." + getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be " + getSuffix() + ")");
        }
        if (!expectedClass.isAssignableFrom(model.getClass())) {
            throw new IllegalModelException(String.format("Wrong kind of model (%s), should be %s or a subclass of it",
                    model.getClass().toString(),
                    expectedClass.toString()));
        }
        return true;
    }

}
