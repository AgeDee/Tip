package sample;

public class BlockedUser {

    private int id;
    private int userId;
    private int blockedId;
    private String date;

    public BlockedUser(){}

    public BlockedUser(int userId, int blockedId, String date) {
        this.userId = userId;
        this.blockedId = blockedId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(int blockedId) {
        this.blockedId = blockedId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BlockedUser{" +
                "id=" + id +
                ", userLogin=" + userId +
                ", blockedId=" + blockedId +
                ", date='" + date + '\'' +
                '}';
    }
}
