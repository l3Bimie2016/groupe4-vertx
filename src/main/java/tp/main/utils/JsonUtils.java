package tp.main.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Polygone Asynchrone on 05/04/2016.
 */
public abstract class JsonUtils {

    public static JsonArray objectToArray(JsonObject jsonObject) {
        JsonArray res = new JsonArray();

        Stream<Map.Entry<String, Object>> stream = jsonObject.stream();
        stream.forEach(e -> res.add(e.getValue()));

        return res;
    }

}
