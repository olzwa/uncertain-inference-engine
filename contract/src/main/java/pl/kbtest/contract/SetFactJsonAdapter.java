package pl.kbtest.contract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Ja on 2016-11-12.
 */
public class SetFactJsonAdapter implements JsonSerializer<SetFact>, JsonDeserializer<SetFact>
{

        @Override
        public JsonElement serialize(SetFact src, Type sf, JsonSerializationContext context) {
        return context.serialize(src);
    }

        @Override
        public SetFact deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
        jsonDeserializationContext) throws JsonParseException {

            Gson gs = new GsonBuilder()
                    .create();

            DefaultSetFact fact = gs.fromJson(jsonElement, DefaultSetFact.class);
            return fact;

    }

}
