package tp.main.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.sql.ResultSet;

/**
 * Created by Polygone Asynchrone on 06/04/2016.
 */
public abstract class Auth {

    static public String generateToken(String login) {
        Vertx vertx = Vertx.currentContext().owner();
        JsonObject config = new JsonObject().put("keyStore", new JsonObject()
                .put("path", "keystore.jceks")
                .put("type", "jceks")
                .put("password", "secret"));

        JWTAuth provider = JWTAuth.create(vertx, config);
        return provider.generateToken(new JsonObject().put("sub", login), new JWTOptions());
    }

    static public void authenticate(String token, Handler<AsyncResult<Boolean>> handler) {
        Vertx vertx = Vertx.currentContext().owner();
        JsonObject config = new JsonObject().put("keyStore", new JsonObject()
                .put("path", "keystore.jceks")
                .put("type", "jceks")
                .put("password", "secret"));

        JWTAuth authProvider = JWTAuth.create(vertx, config);

        JsonObject authInfo = new JsonObject()
                .put("jwt", token);

        authProvider.authenticate(authInfo, res -> {
            if (res.failed()) {
                handler.handle(Future.failedFuture(res.cause()));
            } else {
                handler.handle(Future.succeededFuture());
            }
        });
    }

}
