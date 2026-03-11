package app.capgo.capacitor.intercom;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.intercom.android.sdk.push.IntercomPushClient;
import java.util.Map;

/**
 * Handles FCM messages for Intercom with android:priority="100" so it wins over
 * Capacitor's push-notifications service (priority 0) and Intercom's own
 * built-in service (priority -400).
 *
 * <p>Once this plugin is installed, this service becomes the sole FCM handler
 * for the app. If your app also needs to handle non-Intercom push notifications,
 * extend this class and override onMessageReceived/onNewToken to add your own
 * logic, or implement a delegation chain to other services.
 */
public class IntercomMessagingService extends FirebaseMessagingService {

    private static final String TAG = "IntercomMessagingService";
    private final IntercomPushClient intercomPushClient = new IntercomPushClient();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> message = remoteMessage.getData();

        if (intercomPushClient.isIntercomPush(message)) {
            Log.d(TAG, "Intercom push received, handling natively");
            intercomPushClient.handlePush(getApplication(), message);
        } else {
            super.onMessageReceived(remoteMessage);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        intercomPushClient.sendTokenToIntercom(getApplication(), token);
    }
}
