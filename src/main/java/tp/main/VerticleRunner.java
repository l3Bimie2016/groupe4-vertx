package tp.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * Created by Nico on 18/02/2016.
 */
public class VerticleRunner extends AbstractVerticle{


    @Override
    public void start(){

        vertx.deployVerticle(VerticleFront.class.getCanonicalName());
        vertx.deployVerticle(VerticleService.class.getCanonicalName());
    }

}
