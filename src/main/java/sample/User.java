package sample;

public class User {

    private int userId;
    private String login;
    private String password;
    private String email;
    private String user_ip;

    public User(){}

    @Override
    public String toString() {
        return "User{" +
                "userLogin=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", user_ip='" + user_ip + '\'' +
                '}';
    }

    public User(String login, String password, String email) {
        //this.userLogin = userLogin;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserIp() {
        return user_ip;
    }

    public void setUserIp(String user_ip) {
        this.user_ip = user_ip;
    }
}
