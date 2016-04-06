package tp.main;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;

/**
* Created by Nico on 18/02/2016.
        */
public class MainVerticle extends AbstractVerticle{

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(VerticleFront.class.getCanonicalName());
        vertx.deployVerticle(VerticleService.class.getCanonicalName());
    }

}
