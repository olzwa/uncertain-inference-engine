package pl.kbtest.rule.induction.input;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.kbtest.action.SetAction;
import pl.kbtest.action.SetActionJsonAdapter;
import pl.kbtest.contract.SetRule;
import pl.kbtest.rule.induction.SetFactReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz on 11.11.16.
 */
public class GsonSetRuleReader {

    File file = null;

    public GsonSetRuleReader(File file) {
        this.file = file;
    }

    public List<SetRule> readRules(){
        Gson gs = new GsonBuilder().serializeNulls().disableHtmlEscaping()/*.setPrettyPrinting().*/
                .registerTypeAdapter(SetAction.class, new SetActionJsonAdapter())
                .create();

        List<SetRule> rulesFromFile = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String currentLine = br.readLine();

            while (currentLine != null) {
                SetRule ruleFromFile = gs.fromJson(currentLine,SetRule.class);
                rulesFromFile.add(ruleFromFile);
                currentLine = br.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return rulesFromFile;
    }
}
