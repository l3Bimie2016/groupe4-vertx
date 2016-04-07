package tp.main.utils;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * Created by Polygone Asynchrone on 06/04/2016.
 */
public abstract class ReqError {

    static public String hurl(String message) {
        JsonObject res = new JsonObject();
        res.put("message", message);
        res.put("error", true);
        return Json.encode(res);
    }

}
