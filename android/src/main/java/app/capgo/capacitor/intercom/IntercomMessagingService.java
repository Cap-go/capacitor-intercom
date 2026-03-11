package app.capgo.capacitor.intercom;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.intercom.android.sdk.push.IntercomPushClient;
import java.lang.reflect.Method;
import java.util.Map;

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

        // Always forward to Capacitor's PushNotificationsPlugin (if installed)
        // so the JS layer still gets pushNotificationReceived events for all messages.
        forwardToCapacitorPushPlugin(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        intercomPushClient.sendTokenToIntercom(getApplication(), token);
        forwardTokenToCapacitorPushPlugin(token);
    }

    private void forwardToCapacitorPushPlugin(RemoteMessage remoteMessage) {
        try {
            Class<?> cls = Class.forName("com.capacitorjs.plugins.pushnotifications.PushNotificationsPlugin");
            Method m = cls.getMethod("sendRemoteMessage", RemoteMessage.class);
            m.invoke(null, remoteMessage);
        } catch (ClassNotFoundException e) {
            // @capacitor/push-notifications not installed — fine
        } catch (Exception e) {
            Log.w(TAG, "Failed to forward message to Capacitor: " + e.getMessage());
        }
    }

    private void forwardTokenToCapacitorPushPlugin(String token) {
        try {
            Class<?> cls = Class.forName("com.capacitorjs.plugins.pushnotifications.PushNotificationsPlugin");
            Method m = cls.getMethod("onNewToken", String.class);
            m.invoke(null, token);
        } catch (ClassNotFoundException e) {
            // @capacitor/push-notifications not installed — fine
        } catch (Exception e) {
            Log.w(TAG, "Failed to forward token to Capacitor: " + e.getMessage());
        }
    }
}
