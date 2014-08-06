package models.lts;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;

import java.util.regex.Pattern;

/**
 * Created by pascalpoizat on 06/08/2014.
 */
public class AutLtsReader extends LtsReader {

    private Pattern description_line_pattern;
    private Pattern transition_line_pattern;

    public AutLtsReader() {
        description_line_pattern = Pattern.compile("^des\\(([0-9]+),([0-9]+),([0-9]+)\\)$");
        transition_line_pattern = Pattern.compile("^\\(([0-9]+),\"([^)]*)\",([0-9]+)\\)$");
    }

    @Override
    public String getSuffix() {
        return "aut";
    }

    @Override
    public void modelFromString(Model model, String string) throws IllegalResourceException {
        if (!(model instanceof LtsModel)) {
            throw new IllegalResourceException(String.format("Wrong kind of model (%s), should be %s",
                    model.getClass().toString(),
                    LtsModel.class.toString()));
        }
        LtsModel ltsModel = (LtsModel) model;
        String lines[] = string.split("\n");
        String description_line;
        int i = 0;
        // check that there is a first line (description)
        if(lines.length==0) {
            throw new IllegalResourceException("Incorrect model, could not find 'des(x,y,z)' line");
        }

        // get first line and check it

        // for all other lines, check them and add transition to model
    }
}
