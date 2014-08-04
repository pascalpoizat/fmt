package models.lts;

/**
 * Created by pascalpoizat on 04/08/2014.
 */
public abstract class LtsWriter {
    public abstract String write(LtsModel ltsModel);
    public abstract String write(LtsState ltsState);
    public abstract String write(LtsTransition ltsTransition);
}
