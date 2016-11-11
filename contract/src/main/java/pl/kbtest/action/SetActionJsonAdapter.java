package pl.kbtest.action;

import com.google.gson.*;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
 * Created by tomasz on 30.10.16.
 */
public class SetActionJsonAdapter implements JsonSerializer<SetAction>, JsonDeserializer<SetAction> {

    @Override
    public JsonElement serialize(SetAction src, Type sa, JsonSerializationContext context) {
        return context.serialize(src);
    }

    @Override
    public SetAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String fact = null;
        try {
            fact = jsonElement.getAsJsonObject().get("fact").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String output = null;
        try {
            output = jsonElement.getAsJsonObject().get("output").getAsString();
        } catch (UnsupportedOperationException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean conj = new Boolean(jsonElement.getAsJsonObject().get("conjunction").getAsString());

        return new DefaultSetAction(fact, output, conj);

    }
}
