package sample;

public class Contact {

    private int id;
    private int user_id;
    private int contact_id;

    public Contact(int user_id, int contact_id) {
        this.user_id = user_id;
        this.contact_id = contact_id;
    }

    public Contact() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", contact_id=" + contact_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
