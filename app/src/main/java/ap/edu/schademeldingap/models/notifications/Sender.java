package ap.edu.schademeldingap.models.notifications;

public class Sender {
    private NotificationFCM notification;
    private String to;

    public Sender(){

    }

    public Sender(String to, NotificationFCM notification) {
        this.notification = notification;
        this.to = to;
    }

    public NotificationFCM getNotification() {
        return notification;
    }

    public void setNotification(NotificationFCM notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
