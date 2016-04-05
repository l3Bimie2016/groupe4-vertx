package tp.main;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

/**
 * Created by Polygone Asynchrone on 05/04/2016.
 */
public abstract class Connector {

    private static AsyncSQLClient getSQLClient(Vertx vertx) {
        JsonObject mySQLClientConfig = new JsonObject()
                .put("host", "127.0.0.1")
                .put("port", 3306)
                .put("username", "root")
                .put("password", "toor")
                .put("database", "g4_vertx");

        AsyncSQLClient mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);
        return mySQLClient;
    }

    public static void request(String req, Handler<AsyncResult<ResultSet>> handler) {
        Vertx.currentContext().owner().runOnContext(x -> {
            AsyncSQLClient mySQLClient = Connector.getSQLClient(Vertx.currentContext().owner());

            mySQLClient.getConnection(resConnect -> {
                if(resConnect.succeeded()) {
                    SQLConnection connection = resConnect.result();

                    connection.query(req, resQuery -> {
                        if (resQuery.succeeded()) {
                            handler.handle(Future.succeededFuture(resQuery.result()));
                        } else {
                            handler.handle(Future.failedFuture(resQuery.cause()));
                        }
                    });
                } else {
                    handler.handle(Future.failedFuture(resConnect.cause()));
                }
            });
        });
    }

    public static void request(String req, JsonArray params, Handler<AsyncResult<ResultSet>> handler) {
        Vertx.currentContext().owner().runOnContext(x -> {
            AsyncSQLClient mySQLClient = Connector.getSQLClient(Vertx.currentContext().owner());

            mySQLClient.getConnection(resConnect -> {
                if(resConnect.succeeded()) {
                    SQLConnection connection = resConnect.result();

                    connection.queryWithParams(req, params, resQuery -> {
                        if (resQuery.succeeded()) {
                            handler.handle(Future.succeededFuture(resQuery.result()));
                        } else {
                            handler.handle(Future.failedFuture(resQuery.cause()));
                        }
                    });
                } else {
                    handler.handle(Future.failedFuture(resConnect.cause()));
                }
            });
        });
    }

}