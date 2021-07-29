package Files;

public class user {

    private int id;
    private String username, userType, location;

    public user(int id, String username, String userType, String location) {
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.location = location;
    }

    public user(int id, String username, String userType) {
        this.id = id;
        this.username = username;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
