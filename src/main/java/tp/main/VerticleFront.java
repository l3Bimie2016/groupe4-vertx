package tp.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import tp.main.queryBuilders.UserQueryBuilder;
import tp.main.utils.Handlers;
import tp.main.utils.JsonUtils;

/**
 * Created by Nico on 18/02/2016.
 */
public class VerticleFront extends AbstractVerticle {

    @Override
    public void start(){
        Router router = Router.router(vertx);

        router.route("/public/*").produces("application/json");
        // Important pour consommer du JSON !!!
        router.route().handler(BodyHandler.create());
        router.route("/public/*").handler(context -> {
            context.response().headers().add(HttpHeaders.CONTENT_TYPE, "application/json");
            context.response().headers().add("content-type", "text/html;charset=UTF-8");

            context.response()
                    // do not allow proxies to cache the data
                    .putHeader("Cache-Control", "no-store, no-cache")
                    // prevents Internet Explorer from MIME - sniffing a
                    // response away from the declared content-type
                    .putHeader("X-Content-Type-Options", "nosniff")
                    // Strict HTTPS (for about ~6Months)
                    .putHeader("Strict-Transport-Security", "max-age=" + 15768000)
                    // IE8+ do not allow opening of attachments in the context
                    // of this resource
                    .putHeader("X-Download-Options", "noopen")
                    // enable XSS for IE
                    .putHeader("X-XSS-Protection", "1; mode=block")
                    // deny frames
                    .putHeader("X-FRAME-OPTIONS", "DENY")
                    // Accept all
                    .putHeader("Access-Control-Allow-Origin", "*");

            System.out.println("handle -> " + context.request().path());

            context.next();
        });

        router.post("/user").handler(x -> {
            JsonObject bodyParams = x.getBodyAsJson();
            JsonArray params = JsonUtils.objectToArray(bodyParams);

            Connector.request(UserQueryBuilder.getInsert(), params, res -> {
                if (res.succeeded()) {
                    Handlers.connectUser(bodyParams.getString("login"), connection -> {
                        x.response().end(Json.encode(connection));
                    });
                } else {
                    x.response().end(Json.encode(false));
                }
            });
        });
        router.post("/login").handler(x -> {
            JsonObject bodyParams = x.getBodyAsJson();
            Handlers.connectUser(
                bodyParams.getValue("login").toString(),
                bodyParams.getValue("password").toString(),
                connection -> {
                    x.response().end(Json.encode(connection));
                }
            );
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8090);
    }
}
