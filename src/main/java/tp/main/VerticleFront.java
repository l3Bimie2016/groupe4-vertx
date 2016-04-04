package tp.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import tp.main.model.Board;
import tp.main.model.ResultMessage;
import tp.main.model.VertxQuery;
import tp.main.model.VertxResponse;

import java.lang.reflect.Type;

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

        router.get("/public/hello").handler(x -> {
           x.response().end(Json.encode("Hello world"));
        });

        router.get("/public/message").handler(x -> {
           vertx.eventBus().send("MY_EVENT","hello from VerticleFront", r -> {
               ResultMessage result = new ResultMessage();

               result.setMessage(r.result().body().toString());
               x.response().end(Json.encode(result));
           });
        });

        router.post("/public/query").consumes("application/json").handler(x ->{
            JsonObject queryJson = x.getBodyAsJson();
            Gson gson = new Gson();
            Type boardType = new TypeToken<VertxQuery<Board>>(){}.getType();
            VertxQuery<Board> query = gson.fromJson(queryJson.encodePrettily(),boardType);
            VertxResponse result = new VertxResponse();
            result.setMessage("from vertx front");
            result.setEntity("result board -> " + query.getEntity().getA() * query.getEntity().getB());
            x.response().end(Json.encode(result));

        });


        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8090);


    }
}
