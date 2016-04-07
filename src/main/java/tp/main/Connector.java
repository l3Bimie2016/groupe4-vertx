package tp.main;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Polygone Asynchrone on 05/04/2016.
 */
//public abstract class Connector<T> {
public class Connector {

    private static AsyncSQLClient getSQLClient(Vertx vertx) {
        JsonObject mySQLClientConfig = new JsonObject()
                .put("host", "91.121.192.13")
                .put("port", 3306)
                .put("username", "groupe4")
                .put("password", "GHT1VTT9")
                .put("database", "groupe4vertx");

        AsyncSQLClient mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);
        return mySQLClient;
    }

    //public static void request(String req, Handler<AsyncResult<ResultSet>> handler) {
    public static <T> void request(Class<T> clazz, String req,Handler<AsyncResult<List<T>>> handler) {
        Vertx.currentContext().owner().runOnContext(x -> {
            AsyncSQLClient mySQLClient = Connector.getSQLClient(Vertx.currentContext().owner());

            mySQLClient.getConnection(resConnect -> {
                try {
                    if(resConnect.succeeded()) {
                        SQLConnection connection = resConnect.result();

                        connection.query(req, resQuery -> {
                            if (resQuery.succeeded()) {
                                List<T> response = resQuery.result().getRows().stream().map(r -> Json.decodeValue(r.encode(), clazz)).collect(Collectors.toList());
                                handler.handle(Future.succeededFuture(response));
                            } else {
                                handler.handle(Future.failedFuture(resQuery.cause()));
                            }
                        });
                    } else {
                        handler.handle(Future.failedFuture(resConnect.cause()));
                    }
                } finally {
                    mySQLClient.close();
                }
            });
        });
    }

    public static <T> void request(Class<T> clazz, String req, JsonArray params, Handler<AsyncResult<List<T>>> handler) {
        Vertx.currentContext().owner().runOnContext(x -> {
            AsyncSQLClient mySQLClient = Connector.getSQLClient(Vertx.currentContext().owner());

            mySQLClient.getConnection(resConnect -> {
                try {

                    if(resConnect.succeeded()) {
                        SQLConnection connection = resConnect.result();

                        connection.queryWithParams(req, params, resQuery -> {
                            if (resQuery.succeeded()) {
                                List<T> response = resQuery.result().getRows().stream().map(r -> Json.decodeValue(r.encode(), clazz)).collect(Collectors.toList());
                                handler.handle(Future.succeededFuture(response));
                            } else {
                                handler.handle(Future.failedFuture(resQuery.cause()));
                            }
                        });
                    } else {
                        handler.handle(Future.failedFuture(resConnect.cause()));
                    }
                } finally {
                    mySQLClient.close();
                }
            });
        });
    }

}