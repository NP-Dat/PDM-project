package action;

public class SessionData {
    private static SessionData instance;
    private int Id;
    private SessionData() {}

    public static SessionData getInstance() {
        if (instance == null) {
            instance = new SessionData();
        }
        return instance;
    }
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}
