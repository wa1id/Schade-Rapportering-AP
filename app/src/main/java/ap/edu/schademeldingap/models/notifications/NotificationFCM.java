package ap.edu.schademeldingap.models.notifications;

public class NotificationFCM {
    private String body;
    private String title;

    public NotificationFCM(String body, String title) {
        this.body = body;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }
}
