package sample;

public class ConnectionLog {
    int id;
    int user1Id;
    int user2Id;
    String date;
    String description;

    public ConnectionLog(int user1Id, int user2Id, String date, String description) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.date = date;
        this.description = description;
    }

    public ConnectionLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ConnectionLog{" +
                "id=" + id +
                ", user1Id=" + user1Id +
                ", user2Id=" + user2Id +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
