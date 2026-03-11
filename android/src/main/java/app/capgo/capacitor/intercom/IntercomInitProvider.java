package app.capgo.capacitor.intercom;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import io.intercom.android.sdk.Intercom;
import java.io.InputStream;
import org.json.JSONObject;

/**
 * Auto-initializes Intercom at app startup by reading keys from capacitor.config.json.
 *
 * ContentProviders run before Application.onCreate(), which means Intercom is ready
 * before any Activity launches — including IntercomRootActivity when the user taps
 * a push notification on a cold start.
 *
 * Users just need to add their keys to capacitor.config.json:
 *
 *   "plugins": {
 *     "CapgoIntercom": {
 *       "androidApiKey": "android_sdk-...",
 *       "androidAppId": "your-app-id"
 *     }
 *   }
 */
public class IntercomInitProvider extends ContentProvider {

    private static final String TAG = "IntercomInitProvider";

    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context == null) return false;

        try {
            InputStream is = context.getAssets().open("capacitor.config.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            JSONObject config = new JSONObject(new String(buffer));
            JSONObject plugins = config.optJSONObject("plugins");
            if (plugins == null) return false;

            JSONObject intercomConfig = plugins.optJSONObject("CapgoIntercom");
            if (intercomConfig == null) return false;

            String apiKey = intercomConfig.optString("androidApiKey", "");
            String appId = intercomConfig.optString("androidAppId", "");

            if (!apiKey.isEmpty() && !appId.isEmpty()) {
                Application app = (Application) context.getApplicationContext();
                Intercom.initialize(app, apiKey, appId);
                Log.d(TAG, "Intercom initialized from capacitor.config.json");
            }
        } catch (Exception e) {
            Log.w(TAG, "Could not auto-initialize Intercom: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) { return null; }
    @Override
    public String getType(Uri uri) { return null; }
    @Override
    public Uri insert(Uri uri, ContentValues values) { return null; }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
}
