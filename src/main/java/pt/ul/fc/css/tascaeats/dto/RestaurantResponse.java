package pt.ul.fc.css.tascaeats.dto;

public class RestaurantResponse {
    Long id;
    String designation;
    String status;
    String city;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
