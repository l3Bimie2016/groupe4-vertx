package tp.main.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import tp.main.Connector;
import tp.main.model.User;
import tp.main.queryBuilders.UserQueryBuilder;

import java.util.List;

/**
 * Created by Polygone Asynchrone on 06/04/2016.
 */
public abstract class Handlers {

    private static JsonObject buildResponse(String login, String jsonUser) {
        String token = Auth.generateToken(login);
        JsonObject jObject = new JsonObject();
        jObject.put("token", token);
        jObject.put("user", jsonUser);
        return jObject;
    }

    public static void connectUser(String login, String pwd, Handler<AsyncResult<JsonObject>> handler) {
        JsonArray params = new JsonArray().add(login);

        Connector.request(User.class, UserQueryBuilder.getRetrieve(), params, res -> {
            //res.result().getRows().stream().map(x -> Json.decodeValue(x.encode(), User))
            String result = Json.encode(res.result());

            if (res.succeeded()) {
                //if(result.get(0)) {
                if(true) {
                    JsonObject jObject = buildResponse(login, result);
                    handler.handle(Future.succeededFuture(jObject));
                } else {
                    handler.handle(Future.failedFuture(ReqError.hurl("Mauvais password !")));
                }
            } else {
                handler.handle(Future.failedFuture(ReqError.hurl(res.cause().getMessage())));
            }
        });
    }

    public static void connectUser(String login, Handler<AsyncResult<JsonObject>> handler) {
        JsonArray params = new JsonArray().add(login);

        Connector.request(User.class, UserQueryBuilder.getRetrieve(), params, res -> {
            if (res.succeeded()) {
                //JsonObject jObject = buildResponse(login, res.result().toJson());
                //handler.handle(Future.succeededFuture(jObject));
                JsonObject jObject = buildResponse(login, Json.encode(res.result()));
                handler.handle(Future.succeededFuture(jObject));
            } else {
                handler.handle(Future.failedFuture(ReqError.hurl(res.cause().getMessage())));
            }
        });
    }

}
