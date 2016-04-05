package tp.main;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLConnection;

/**
* Created by Nico on 18/02/2016.
        */
public class MainVerticle extends AbstractVerticle{

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        /*JsonObject mySQLClientConfig = new JsonObject()
            .put("host", "127.0.0.1")
            .put("port", 3306)
            .put("username", "root")
            .put("password", "toor")
            .put("database", "g4_vertx");

        AsyncSQLClient mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);

        mySQLClient.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection connection = res.result();

                vertx.deployVerticle(VerticleFront.class.getCanonicalName());
                vertx.deployVerticle(VerticleService.class.getCanonicalName());

            } else {
                // Failed to get connection - deal with it
            }
        });*/
        vertx.deployVerticle(VerticleFront.class.getCanonicalName());
        vertx.deployVerticle(VerticleService.class.getCanonicalName());
    }

}
