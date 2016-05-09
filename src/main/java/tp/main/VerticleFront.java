package tp.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import tp.main.model.*;
import tp.main.queryBuilders.UserQueryBuilder;
import tp.main.utils.Auth;
import tp.main.utils.Handlers;
import tp.main.utils.JsonUtils;
import tp.main.utils.ReqError;

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
        /*router.route("/public/*").handler(context -> {
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
        });*/

        router.route("/api/*").handler(context -> {
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

        router.post("/api/user").handler(x -> {
            JsonObject bodyParams = x.getBodyAsJson();
            JsonArray params = JsonUtils.objectToArray(bodyParams);

            Connector.request(User.class, UserQueryBuilder.getRetrieve(), new JsonArray().add(bodyParams.getValue("login")), resExists -> {
                //if(resExists.succeeded()) {
                    //if(resExists.result().getNumRows() <= 0) {
                    if(resExists.result().size() <= 0) {
                        Connector.request(User.class, UserQueryBuilder.getInsert(), params, res -> {
                            if (res.succeeded()) {
                                Handlers.connectUser(bodyParams.getString("login"), connection -> {
                                    //x.response().end(Json.encode(connection));
                                    x.response().end(connection.result().toString());
                                });
                            } else {
                                x.response().end(ReqError.hurl(res.cause().getMessage()));
                            }
                        });
                    } else {
                        x.response().end(ReqError.hurl("L'utilisateur existe déjà"));
                    }
                //} else {
                //    x.response().end(ReqError.hurl(resExists.cause().getMessage()));
                //}
            });
        });
        router.post("/api/login").handler(x -> {
            JsonObject bodyParams = x.getBodyAsJson();
            Handlers.connectUser(
                bodyParams.getValue("login").toString(),
                bodyParams.getValue("password").toString(),
                connection -> x.response().end(Json.encode(connection.result()))
            );
        });

        router.get("/api/marques").handler(x -> {
            /*Auth.authenticate(x.getBodyAsJson().getValue("token").toString(), z -> {
                //...
            });*/

            String req = "SELECT vehicleBrand FROM VehicleBrand";

            Connector.request(VehicleBrand.class, req, res -> {
                if(res.succeeded()) {
                    //x.response().end(Json.encode(res.result().getRows()));
                    x.response().end(Json.encode(res.result()));
                } else {
                    x.response().end(ReqError.hurl(res.cause().getMessage()));
                }
            });
        });
        router.get("/api/modeles").handler(x -> {
            /*Auth.authenticate(x.getBodyAsJson().getValue("token").toString(), z -> {
                //...
            });*/

            JsonArray params = new JsonArray().add(x.request().getParam("marque"));
            String req = "SELECT vm.vehicleModelName FROM VehicleModel vm" +
                    " JOIN  VehicleBrand vb ON vb.vehicleBrandID = vm.vehicleBrandID" +
                    " WHERE vb.vehicleBrand LIKE ?";

            Connector.request(VehicleModel.class, req, params, res -> {
                if(res.succeeded()) {
                    //x.response().end(Json.encode(res.result().getRows()));
                    x.response().end(Json.encode(res.result()));
                } else {
                    x.response().end(ReqError.hurl(res.cause().getMessage()));
                }
            });
        });
        router.get("/api/fuel").handler(x -> {
            /*Auth.authenticate(x.getBodyAsJson().getValue("token").toString(), z -> {
                //...
            });*/

            JsonArray params = new JsonArray().add(x.request().getParam("modele"));
            String req = "SELECT ft.vehicleFuelName FROM FuelType ft" +
                    " JOIN  ModelFuel mf ON mf.vehicleFuelID = ft.vehicleFuelID" +
                    " JOIN VehicleModel vm ON vm.vehicleModelID = mf.vehicleModelID" +
                    " WHERE vm.vehicleModelName LIKE ?";

            Connector.request(VehicleFuel.class, req, params, res -> {
                if(res.succeeded()) {
                    //x.response().end(Json.encode(res.result().getRows()));
                    x.response().end(Json.encode(res.result()));
                } else {
                    x.response().end(ReqError.hurl(res.cause().getMessage()));
                }
            });
        });
        router.get("/api/hp").handler(x -> {
            /*Auth.authenticate(x.getBodyAsJson().getValue("token").toString(), z -> {
                //...
            });*/

            JsonArray params = new JsonArray().add(x.request().getParam("modeleId"));
            String req = "SELECT vhp.vehicleHPNb FROM VehicleHP AS vhp\n" +
                    "JOIN ModelHP AS mhp\n" +
                    "WHERE mhp.vehicleHPID = ?\n" +
                    "AND vhp.vehicleHPID = mhp.vehicleHPID";

            Connector.request(VehicleHP.class, req, params, res -> {
                if(res.succeeded()) {
                    //x.response().end(Json.encode(res.result().getRows()));
                    x.response().end(Json.encode(res.result()));
                } else {
                    x.response().end(ReqError.hurl(res.cause().getMessage()));
                }
            });
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8090);
    }
}
