package pl.kbtest.rule.induction.input;

import java.util.List;

/**
 * Created by Ja on 2016-01-16.
 */
public interface Parser {

    List<String> getColumns();
    List<List<String>> getRows();

}
