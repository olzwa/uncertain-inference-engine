package pl.kbtest.rule.induction.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Ja on 2016-11-23.
 */
public class GsonWriter {

    Class c;
    File f;
    boolean append;

    public GsonWriter(Class c, File f, boolean append) {
        this.c = c;
        this.f = f;
        this.append = append;
    }

    public void write(List objects){

        Gson gs = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        try{
        if (!f.exists()){
            f.createNewFile();
        }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f,append)));

            for(int i=0; i < objects.size(); i++){
                String json = gs.toJson(objects.get(i));
                pw.write(json+"\n");
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
