package com.diguage.truman.vertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import org.junit.jupiter.api.Test;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-05-31 21:52
 */
public class VertTest {
    @Test
    public void test() {
        Vertx vertx = Vertx.vertx();
//        request.response().putHeader("Content-Type", "text/plain").write("some text").end();
        vertx.setPeriodic(1000, id -> {
            System.out.println(id);
        });
        vertx.executeBlocking(promise -> {

        }, asyncResult -> {

        });

        HttpServer server = vertx.createHttpServer();
        Handler<HttpServerRequest> requestHandler = server.requestHandler();
    }
}
