package tp.main.model;

/**
 * Created by Polygone Asynchrone on 07/04/2016.
 */
public class VehicleModel {

    private Integer vehicleModelID;
    private String vehicleModelName;

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public Integer getVehicleModelID() {
        return vehicleModelID;
    }

    public void setVehicleModelID(Integer vehicleModelID) {
        this.vehicleModelID = vehicleModelID;
    }
}
