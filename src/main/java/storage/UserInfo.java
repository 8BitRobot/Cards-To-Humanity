package storage;

public class UserInfo {
    private String username;
    private String display_name;
    private String email;
    private long creation_time;

    public UserInfo(String username, String display_name, String email, long creation_time) {
        this.username = username;
        this.display_name = display_name;
        this.email = email;
        this.creation_time = creation_time;
    }
}
