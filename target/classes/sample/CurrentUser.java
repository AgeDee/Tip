package sample;

public class CurrentUser {
    static String userId = "userId";

    public CurrentUser() {
    }

    public CurrentUser(String userId){
        this.userId = userId;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        CurrentUser.userId = userId;
    }

}
