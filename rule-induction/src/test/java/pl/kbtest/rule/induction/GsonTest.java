package pl.kbtest.rule.induction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Assert;
import org.junit.Test;
import pl.kbtest.action.SetActionJsonAdapter;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.DefaultSetFact;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetFactJsonAdapter;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tomasz on 27.10.16.
 */
public class GsonTest {

    @Test
    public void convertSetRuleBothWays() {
        // given
        SetPremise first = new SetPremise("wydzial_rodzimy", new HashSet<>(Arrays.asList("informatyka", "elektryk")), false, true);
        SetAction action = new DefaultSetAction("kierunek=informatyka", null, true);
        List<SetPremise> premises = new ArrayList();
        premises.add(first);
        List conclusions = Arrays.asList(action);
        SetRule rule = new SetRule(premises, conclusions, new GrfIrf(BigDecimal.valueOf(0.55), BigDecimal.valueOf(0.75)));

        System.out.println(rule);

        Gson gs = new GsonBuilder().serializeNulls().disableHtmlEscaping()/*.setPrettyPrinting().*/
                .registerTypeAdapter(SetAction.class, new SetActionJsonAdapter())
                .create();
        // when
        String json = gs.toJson(rule);

        System.out.println(json);

        SetRule fromJSON = gs.fromJson(json, SetRule.class);
        // then
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compare(rule, fromJSON);
        System.out.println(diff);


        Assert.assertEquals(rule, fromJSON);
        //Assert.assertEquals(0, diff.getChanges().size());


    }

    @Test
    public void convertSetFactBothWays() {
        // given

        SetFact fact = SetFactFactory.getInstance("rok = 1 AND 2",new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.valueOf(0.0)));

        System.out.println(fact);
        // when
        Gson gs = new GsonBuilder().serializeNulls().disableHtmlEscaping()/*.setPrettyPrinting().*/
                .registerTypeAdapter(SetFact.class, new SetFactJsonAdapter())
                .create();

        String json = gs.toJson(fact);
        System.out.println(json);

        SetFact fromJSON = gs.fromJson(json, SetFact.class);
        //DefaultSetFact fromJSON = gs.fromJson(json, DefaultSetFact.class);

        // then
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compare(fact, fromJSON);
        System.out.println(diff);


        Assert.assertEquals(fact, fromJSON);
        //Assert.assertEquals(0, diff.getChanges().size());


    }


    @Test
    public void adapterFactoryTest() {
        RuntimeTypeAdapterFactory<SetFact> setFactRuntimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory
                        .of(SetFact.class)
                        .registerSubtype(DefaultSetFact.class);

        Gson gs = new GsonBuilder().serializeNulls().disableHtmlEscaping()/*.setPrettyPrinting().*/
                .registerTypeAdapterFactory(setFactRuntimeTypeAdapterFactory)
                .create();

        SetFact fact = SetFactFactory.getInstance("rok = 1 AND 2",new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.valueOf(0.0)));

        System.out.println(fact);

       
        String json = gs.toJson(fact,SetFact.class);

        System.out.println(json);



        //SetFact fromJSON = gs.fromJson(json, DefaultSetFact.class);
        SetFact fromJSON = gs.fromJson(json, SetFact.class);


        // then
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compare(fact, fromJSON);
        System.out.println(diff);


        Assert.assertEquals(fact, fromJSON);


    }

}
