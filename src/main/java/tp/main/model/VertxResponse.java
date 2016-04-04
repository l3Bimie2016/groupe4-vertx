package tp.main.model;

/**
 * Created by Nico on 03/03/2016.
 */
public class VertxResponse {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    private String message;
    private String entity;
}
