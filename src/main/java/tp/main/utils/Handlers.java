package tp.main.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import tp.main.Connector;
import tp.main.queryBuilders.UserQueryBuilder;

import java.util.List;

/**
 * Created by Polygone Asynchrone on 06/04/2016.
 */
public abstract class Handlers {

    private static JsonObject buildResponse(String login, JsonObject user) {
        String token = Token.generate(login);
        JsonObject jObject = new JsonObject();
        jObject.put("token", token);
        jObject.put("user", user);
        return jObject;
    }

    public static void connectUser(String login, String pwd, Handler<AsyncResult<JsonObject>> handler) {
        JsonArray params = new JsonArray().add(login);

        Connector.request(UserQueryBuilder.getRetrieve(), params, res -> {
            List<JsonArray> result = res.result().getResults();

            if (res.succeeded() || pwd.equals(result.get(0).getString(1))) {
                /*String token = Token.generate(login);
                JsonObject jObject = new JsonObject();
                jObject.put("token", token);
                jObject.put("user", res.result().toJson());*/

                JsonObject jObject = buildResponse(login, res.result().toJson());
                handler.handle(Future.succeededFuture(jObject));
            } else {
                handler.handle(Future.failedFuture(Json.encode(false)));
            }
        });
    }

    public static void connectUser(String login, Handler<AsyncResult<JsonObject>> handler) {
        JsonArray params = new JsonArray().add(login);

        Connector.request(UserQueryBuilder.getRetrieve(), params, res -> {
            if (res.succeeded()) {
                /*String token = Token.generate(login);
                JsonObject jObject = new JsonObject();
                jObject.put("token", token);
                jObject.put("user", res.result().toJson());*/

                JsonObject jObject = buildResponse(login, res.result().toJson());
                handler.handle(Future.succeededFuture(jObject));
            } else {
                handler.handle(Future.failedFuture(Json.encode(false)));
            }
        });
    }

}
