package ducnm.com.footballyardmanage;

import java.util.HashMap;
import java.io.Serializable;

/**
 * Created by Administrator on 7/17/2017.
 */

public class Yard implements Serializable{

    private String address;
    private String yardOwner;
    private String phoneNumber;
    private String description;
    private String area;
    private String idOwner;
    private String id;
    private HashMap<String, String> listImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public void setListImage(HashMap<String, String> listImage) {
        this.listImage = listImage;
    }

    public Yard() {
        listImage = new HashMap<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYardOwner() {
        return yardOwner;
    }

    public void setYardOwner(String yardOwner) {
        this.yardOwner = yardOwner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public HashMap<String, String> getListImage() {
        return listImage;
    }

    public void setListImage(String name, String url) {
        listImage.put(name,url);
    }
}
