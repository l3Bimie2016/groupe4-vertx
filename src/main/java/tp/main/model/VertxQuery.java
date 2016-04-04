package tp.main.model;

/**
 * Created by Nico on 03/03/2016.
 */
public class VertxQuery<T> {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    private String message;
    private T entity;
}
