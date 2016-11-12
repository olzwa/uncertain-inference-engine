package pl.kbtest.rule.induction.input;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactJsonAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ja on 2016-11-12.
 */
public class GsonSetFactReader {

    File file = null;

    public GsonSetFactReader(File file) {
        this.file = file;
    }

    public List<SetFact> readFacts(){
        Gson gs = new GsonBuilder().serializeNulls().disableHtmlEscaping()/*.setPrettyPrinting().*/
                .registerTypeAdapter(SetFact.class, new SetFactJsonAdapter())
                .create();

        List<SetFact> factsFromFile = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String currentLine = bf.readLine();

            while (currentLine != null) {
                SetFact factFromFile = gs.fromJson(currentLine,SetFact.class);
                factsFromFile.add(factFromFile);
                currentLine = bf.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return factsFromFile;
    }
}
