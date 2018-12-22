package ap.edu.schademeldingap.interfaces;

import ap.edu.schademeldingap.models.notifications.NotificationResponse;
import ap.edu.schademeldingap.models.notifications.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAABUkrAYc:APA91bHanJ07sy0M1v6ARfEuW1icC6vae-WMxaNbiyeiJ67_ZJ4PWA-GRcLO3A6_GTo9j3wQkUVh5HAmLFBzCHGaiNTq4xBdexKbFWndYIMblAh_-DJZNDWQYa38446buXH2QZWZEALk"
    })

    @POST("fcm/send")
    Call<NotificationResponse> sendNotification(@Body Sender body);
}
