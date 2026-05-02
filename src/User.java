public class User {
    private Integer userId;
    private String username;
    private String phone;

    public User() {
    }

    public User(Integer userId, String username, String phone) {
        this.userId = userId;
        this.username = username;
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}