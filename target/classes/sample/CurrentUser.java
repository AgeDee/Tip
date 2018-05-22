package sample;

public class CurrentUser {
    static String userLogin = "userLogin";

    public CurrentUser() {
    }

    public CurrentUser(String userLogin){
        this.userLogin = userLogin;
    }

    public static String getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(String userLogin) {
        CurrentUser.userLogin = userLogin;
    }

}
