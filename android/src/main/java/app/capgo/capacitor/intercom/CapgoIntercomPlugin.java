package app.capgo.capacitor.intercom;

import android.app.Application;
import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import io.intercom.android.sdk.Company;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.IntercomSpace;
import io.intercom.android.sdk.UnreadConversationCountListener;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.android.sdk.push.IntercomPushClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "CapgoIntercom")
public class CapgoIntercomPlugin extends Plugin {

    private static final String TAG = "CapgoIntercom";
    private final IntercomPushClient intercomPushClient = new IntercomPushClient();
    private UnreadConversationCountListener unreadListener;

    @Override
    public void load() {
        try {
            String apiKey = getConfig().getString("androidApiKey", "");
            String appId = getConfig().getString("androidAppId", "");

            if (apiKey != null && !apiKey.isEmpty() && appId != null && !appId.isEmpty()) {
                Application app = (Application) getContext().getApplicationContext();
                Intercom.initialize(app, apiKey, appId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Intercom: " + e.getMessage(), e);
        }

        unreadListener = (unreadCount) -> {
            JSObject data = new JSObject();
            data.put("count", unreadCount);
            notifyListeners("unreadCountDidChange", data);
        };
        Intercom.client().addUnreadConversationCountListener(unreadListener);
    }

    @Override
    public void handleOnStart() {
        try {
            String apiKey = getConfig().getString("androidApiKey", "");
            String appId = getConfig().getString("androidAppId", "");

            if (apiKey != null && !apiKey.isEmpty() && appId != null && !appId.isEmpty()) {
                Application app = (Application) getContext().getApplicationContext();
                Intercom.initialize(app, apiKey, appId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to re-initialize Intercom on start: " + e.getMessage(), e);
        }
    }

    @Override
    protected void handleOnDestroy() {
        if (unreadListener != null) {
            Intercom.client().removeUnreadConversationCountListener(unreadListener);
            unreadListener = null;
        }
    }

    @PluginMethod
    public void loadWithKeys(PluginCall call) {
        String apiKey = call.getString("apiKeyAndroid", "");
        String appId = call.getString("appId", "");

        if (apiKey == null || apiKey.isEmpty() || appId == null || appId.isEmpty()) {
            call.reject("appId and apiKeyAndroid are required");
            return;
        }

        try {
            Application app = (Application) getContext().getApplicationContext();
            Intercom.initialize(app, apiKey, appId);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to initialize Intercom: " + e.getMessage(), e);
        }
    }

    @PluginMethod
    public void registerIdentifiedUser(PluginCall call) {
        String userId = call.getString("userId");
        String email = call.getString("email");

        Registration registration = new Registration();
        if (userId != null && !userId.isEmpty()) {
            registration = registration.withUserId(userId);
        }
        if (email != null && !email.isEmpty()) {
            registration = registration.withEmail(email);
        }

        Intercom.client().registerIdentifiedUser(registration);
        call.resolve();
    }

    @PluginMethod
    public void registerUnidentifiedUser(PluginCall call) {
        Intercom.client().registerUnidentifiedUser();
        call.resolve();
    }

    @PluginMethod
    public void updateUser(PluginCall call) {
        UserAttributes.Builder builder = new UserAttributes.Builder();

        String userId = call.getString("userId");
        if (userId != null && !userId.isEmpty()) {
            builder.withUserId(userId);
        }

        String email = call.getString("email");
        if (email != null && !email.isEmpty()) {
            builder.withEmail(email);
        }

        String name = call.getString("name");
        if (name != null && !name.isEmpty()) {
            builder.withName(name);
        }

        String phone = call.getString("phone");
        if (phone != null && !phone.isEmpty()) {
            builder.withPhone(phone);
        }

        String languageOverride = call.getString("languageOverride");
        if (languageOverride != null && !languageOverride.isEmpty()) {
            builder.withLanguageOverride(languageOverride);
        }

        JSObject customAttributes = call.getObject("customAttributes");
        if (customAttributes != null) {
            Map<String, Object> map = mapFromJSON(customAttributes);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder.withCustomAttribute(entry.getKey(), entry.getValue());
            }
        }

        try {
            JSONArray companiesArray = call.getArray("companies");
            if (companiesArray != null) {
                for (int i = 0; i < companiesArray.length(); i++) {
                    JSONObject companyData = companiesArray.getJSONObject(i);
                    Company.Builder companyBuilder = new Company.Builder();

                    if (companyData.has("companyId")) {
                        companyBuilder.withCompanyId(companyData.getString("companyId"));
                    }
                    if (companyData.has("name")) {
                        companyBuilder.withName(companyData.getString("name"));
                    }
                    if (companyData.has("plan")) {
                        companyBuilder.withPlan(companyData.getString("plan"));
                    }
                    if (companyData.has("monthlySpend")) {
                        companyBuilder.withMonthlySpend(companyData.getInt("monthlySpend"));
                    }
                    if (companyData.has("createdAt")) {
                        companyBuilder.withCreatedAt(companyData.getLong("createdAt"));
                    }
                    if (companyData.has("customAttributes")) {
                        JSONObject customAttrs = companyData.getJSONObject("customAttributes");
                        Map<String, Object> attrsMap = mapFromJSON(JSObject.fromJSONObject(customAttrs));
                        for (Map.Entry<String, Object> entry : attrsMap.entrySet()) {
                            companyBuilder.withCustomAttribute(entry.getKey(), entry.getValue());
                        }
                    }

                    builder.withCompany(companyBuilder.build());
                }
            }
        } catch (JSONException e) {
            Log.w(TAG, "Failed to parse companies: " + e.getMessage());
        }

        Intercom.client().updateUser(builder.build());
        call.resolve();
    }

    @PluginMethod
    public void logout(PluginCall call) {
        Intercom.client().logout();
        call.resolve();
    }

    @PluginMethod
    public void logEvent(PluginCall call) {
        String name = call.getString("name");
        if (name == null || name.isEmpty()) {
            call.reject("Event name is required");
            return;
        }

        JSObject data = call.getObject("data");
        if (data != null) {
            Map<String, Object> metaData = mapFromJSON(data);
            Intercom.client().logEvent(name, metaData);
        } else {
            Intercom.client().logEvent(name);
        }
        call.resolve();
    }

    @PluginMethod
    public void displayMessenger(PluginCall call) {
        Intercom.client().present();
        call.resolve();
    }

    @PluginMethod
    public void displayMessageComposer(PluginCall call) {
        String message = call.getString("message");
        if (message == null) {
            call.reject("message is required");
            return;
        }
        Intercom.client().displayMessageComposer(message);
        call.resolve();
    }

    @PluginMethod
    public void displayHelpCenter(PluginCall call) {
        Intercom.client().present(IntercomSpace.HelpCenter);
        call.resolve();
    }

    @PluginMethod
    public void hideMessenger(PluginCall call) {
        Intercom.client().hideIntercom();
        call.resolve();
    }

    @PluginMethod
    public void displayLauncher(PluginCall call) {
        Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE);
        call.resolve();
    }

    @PluginMethod
    public void hideLauncher(PluginCall call) {
        Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
        call.resolve();
    }

    @PluginMethod
    public void displayInAppMessages(PluginCall call) {
        Intercom.client().setInAppMessageVisibility(Intercom.Visibility.VISIBLE);
        call.resolve();
    }

    @PluginMethod
    public void hideInAppMessages(PluginCall call) {
        Intercom.client().setInAppMessageVisibility(Intercom.Visibility.GONE);
        call.resolve();
    }

    @PluginMethod
    public void displayCarousel(PluginCall call) {
        String carouselId = call.getString("carouselId");
        if (carouselId == null || carouselId.isEmpty()) {
            call.reject("carouselId is required");
            return;
        }
        Intercom.client().displayCarousel(carouselId);
        call.resolve();
    }

    @PluginMethod
    public void displayArticle(PluginCall call) {
        String articleId = call.getString("articleId");
        if (articleId == null || articleId.isEmpty()) {
            call.reject("articleId is required");
            return;
        }
        Intercom.client().displayArticle(articleId);
        call.resolve();
    }

    @PluginMethod
    public void displaySurvey(PluginCall call) {
        String surveyId = call.getString("surveyId");
        if (surveyId == null || surveyId.isEmpty()) {
            call.reject("surveyId is required");
            return;
        }
        Intercom.client().displaySurvey(surveyId);
        call.resolve();
    }

    @PluginMethod
    public void setUserHash(PluginCall call) {
        String hmac = call.getString("hmac");
        if (hmac == null || hmac.isEmpty()) {
            call.reject("hmac is required");
            return;
        }
        try {
            Intercom.client().setUserHash(hmac);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to set user hash: " + e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setUserJwt(PluginCall call) {
        String jwt = call.getString("jwt");
        if (jwt == null || jwt.isEmpty()) {
            call.reject("jwt is required");
            return;
        }
        try {
            Intercom.client().setUserHash(jwt);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to set user JWT: " + e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setBottomPadding(PluginCall call) {
        Double value = call.getDouble("value");
        if (value == null) {
            call.reject("value is required");
            return;
        }
        Intercom.client().setBottomPadding(value.intValue());
        call.resolve();
    }

    @PluginMethod
    public void sendPushTokenToIntercom(PluginCall call) {
        String token = call.getString("value");
        if (token == null || token.isEmpty()) {
            call.reject("value is required");
            return;
        }
        intercomPushClient.sendTokenToIntercom(getActivity().getApplication(), token);
        call.resolve();
    }

    @PluginMethod
    public void receivePush(PluginCall call) {
        JSObject notificationData = call.getData();
        if (notificationData != null) {
            Map<String, String> map = new HashMap<>();
            Iterator<String> keys = notificationData.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    map.put(key, notificationData.getString(key));
                } catch (Exception e) {
                    // skip non-string values
                }
            }
            if (intercomPushClient.isIntercomPush(map)) {
                intercomPushClient.handlePush(getActivity().getApplication(), map);
            }
        }
        call.resolve();
    }

    @PluginMethod
    public void getUnreadConversationCount(PluginCall call) {
        int count = Intercom.client().getUnreadConversationCount();
        JSObject result = new JSObject();
        result.put("count", count);
        call.resolve(result);
    }

    private static Map<String, Object> mapFromJSON(JSObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    map.put(key, mapFromJSON(JSObject.fromJSONObject((JSONObject) value)));
                } else if (value instanceof JSONArray) {
                    map.put(key, listFromJSON((JSONArray) value));
                } else {
                    map.put(key, value);
                }
            } catch (JSONException e) {
                // skip
            }
        }
        return map;
    }

    private static List<Object> listFromJSON(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object value = jsonArray.get(i);
                if (value instanceof JSONObject) {
                    list.add(mapFromJSON(JSObject.fromJSONObject((JSONObject) value)));
                } else if (value instanceof JSONArray) {
                    list.add(listFromJSON((JSONArray) value));
                } else {
                    list.add(value);
                }
            } catch (JSONException e) {
                // skip
            }
        }
        return list;
    }
}
