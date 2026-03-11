package app.capgo.capacitor.intercom;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import io.intercom.android.sdk.push.IntercomPushClient;
import java.util.Map;

/**
 * Static helper for routing Firebase Cloud Messaging events to Intercom.
 *
 * <p>Since Android allows only one {@code FirebaseMessagingService}, your app must
 * create its own service and call these methods to forward Intercom pushes.
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * public class MyFirebaseMessagingService extends FirebaseMessagingService {
 *
 *     @Override
 *     public void onMessageReceived(RemoteMessage remoteMessage) {
 *         if (IntercomFcmHelper.isIntercomPush(remoteMessage)) {
 *             IntercomFcmHelper.onMessageReceived(this, remoteMessage);
 *         } else {
 *             // handle your own / other SDK pushes
 *         }
 *     }
 *
 *     @Override
 *     public void onNewToken(String token) {
 *         IntercomFcmHelper.onNewToken(this, token);
 *         // also forward token to other SDKs if needed
 *     }
 * }
 * }</pre>
 */
public final class IntercomFcmHelper {

    private static final String TAG = "IntercomFcmHelper";
    private static final IntercomPushClient pushClient = new IntercomPushClient();

    private IntercomFcmHelper() {}

    /**
     * Returns {@code true} if this message was sent by Intercom.
     */
    public static boolean isIntercomPush(@NonNull RemoteMessage remoteMessage) {
        return pushClient.isIntercomPush(remoteMessage.getData());
    }

    /**
     * Passes an Intercom push message to the SDK for display.
     * Call this only when {@link #isIntercomPush} returns {@code true}.
     */
    public static void onMessageReceived(@NonNull android.content.Context context, @NonNull RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (pushClient.isIntercomPush(data)) {
            Log.d(TAG, "Handling Intercom push");
            Application app = (Application) context.getApplicationContext();
            pushClient.handlePush(app, data);
        }
    }

    /**
     * Forwards a new FCM token to Intercom so the device can receive pushes.
     */
    public static void onNewToken(@NonNull android.content.Context context, @NonNull String token) {
        Log.d(TAG, "Forwarding FCM token to Intercom");
        Application app = (Application) context.getApplicationContext();
        pushClient.sendTokenToIntercom(app, token);
    }
}
