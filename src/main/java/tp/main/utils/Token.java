package tp.main.utils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;

/**
 * Created by Polygone Asynchrone on 06/04/2016.
 */
public abstract class Token {

    static public String generate(String login) {
        Vertx vertx = Vertx.currentContext().owner();
        JsonObject config = new JsonObject().put("keyStore", new JsonObject()
                .put("path", "keystore.jceks")
                .put("type", "jceks")
                .put("password", "secret"));

        JWTAuth provider = JWTAuth.create(vertx, config);
        return provider.generateToken(new JsonObject().put("sub", login), new JWTOptions());
    }

}
