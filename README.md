# @capgo/capacitor-intercom
 <a href="https://capgo.app/"><img src='https://raw.githubusercontent.com/Cap-go/capgo/main/assets/capgo_banner.png' alt='Capgo - Instant updates for capacitor'/></a>

<div align="center">
  <h2><a href="https://capgo.app/?ref=plugin_intercom"> ➡️ Get Instant updates for your App with Capgo</a></h2>
  <h2><a href="https://capgo.app/consulting/?ref=plugin_intercom"> Missing a feature? We'll build the plugin for you 💪</a></h2>
</div>

Intercom Capacitor plugin

## Why Capacitor Intercom?

A fully re-implemented Intercom plugin for **Capacitor 8**, built to Capgo quality standards:

- **Intercom iOS SDK 19+** - Latest Swift APIs with proper async handling
- **Intercom Android SDK 17+** - Modern Intercom client integration
- **Bug-free** - Fixed known bugs from community implementations (e.g. `hideInAppMessages` on Android)
- **Cross-platform** - Consistent API across iOS and Android
- **Full feature set** - Messenger, Help Center, Articles, Carousels, Push, Identity Verification, and more

Essential for any app integrating Intercom for customer support, onboarding, or in-app messaging.

## Documentation

The most complete doc is available here: https://capgo.app/docs/plugins/intercom/

## Install

```bash
npm install @capgo/capacitor-intercom
npx cap sync
```

## Configuration

Add your Intercom keys to your Capacitor config:

```json
{
  "plugins": {
    "CapgoIntercom": {
      "iosApiKey": "ios_sdk-xxx",
      "iosAppId": "yyy",
      "androidApiKey": "android_sdk-xxx",
      "androidAppId": "yyy"
    }
  }
}
```

Alternatively, you can initialize at runtime using `loadWithKeys()`.

### iOS

The Intercom iOS SDK (`~> 19.0`) is included automatically via CocoaPods or Swift Package Manager.

### Android

The Intercom Android SDK (`17.4.2`) is included automatically via Gradle.

## API

<docgen-index>

* [`loadWithKeys(...)`](#loadwithkeys)
* [`registerIdentifiedUser(...)`](#registeridentifieduser)
* [`registerUnidentifiedUser()`](#registerunidentifieduser)
* [`updateUser(...)`](#updateuser)
* [`logout()`](#logout)
* [`logEvent(...)`](#logevent)
* [`displayMessenger()`](#displaymessenger)
* [`displayMessageComposer(...)`](#displaymessagecomposer)
* [`displayHelpCenter()`](#displayhelpcenter)
* [`hideMessenger()`](#hidemessenger)
* [`displayLauncher()`](#displaylauncher)
* [`hideLauncher()`](#hidelauncher)
* [`displayInAppMessages()`](#displayinappmessages)
* [`hideInAppMessages()`](#hideinappmessages)
* [`displayCarousel(...)`](#displaycarousel)
* [`displayArticle(...)`](#displayarticle)
* [`setUserHash(...)`](#setuserhash)
* [`setUserJwt(...)`](#setuserjwt)
* [`setBottomPadding(...)`](#setbottompadding)
* [`sendPushTokenToIntercom(...)`](#sendpushtokentointercom)
* [`receivePush(...)`](#receivepush)
* [`addListener('windowDidShow', ...)`](#addlistenerwindowdidshow-)
* [`addListener('windowDidHide', ...)`](#addlistenerwindowdidhide-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### loadWithKeys(...)

```typescript
loadWithKeys(options: IntercomLoadOptions) => Promise<void>
```

Initialize Intercom with API keys at runtime.
Use this if you prefer not to configure keys in capacitor.config.

| Param         | Type                                                                |
| ------------- | ------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomloadoptions">IntercomLoadOptions</a></code> |

--------------------


### registerIdentifiedUser(...)

```typescript
registerIdentifiedUser(options: IntercomIdentifiedUserOptions) => Promise<void>
```

Register a known user with Intercom.
At least one of userId or email must be provided.

| Param         | Type                                                                                    |
| ------------- | --------------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomidentifieduseroptions">IntercomIdentifiedUserOptions</a></code> |

--------------------


### registerUnidentifiedUser()

```typescript
registerUnidentifiedUser() => Promise<void>
```

Register an anonymous user with Intercom.

--------------------


### updateUser(...)

```typescript
updateUser(options: IntercomUserUpdateOptions) => Promise<void>
```

Update user attributes in Intercom.

| Param         | Type                                                                            |
| ------------- | ------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomuserupdateoptions">IntercomUserUpdateOptions</a></code> |

--------------------


### logout()

```typescript
logout() => Promise<void>
```

Log the user out of Intercom.

--------------------


### logEvent(...)

```typescript
logEvent(options: IntercomLogEventOptions) => Promise<void>
```

Log a custom event in Intercom.

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomlogeventoptions">IntercomLogEventOptions</a></code> |

--------------------


### displayMessenger()

```typescript
displayMessenger() => Promise<void>
```

Open the Intercom messenger.

--------------------


### displayMessageComposer(...)

```typescript
displayMessageComposer(options: IntercomMessageComposerOptions) => Promise<void>
```

Open the message composer with a pre-filled message.

| Param         | Type                                                                                      |
| ------------- | ----------------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercommessagecomposeroptions">IntercomMessageComposerOptions</a></code> |

--------------------


### displayHelpCenter()

```typescript
displayHelpCenter() => Promise<void>
```

Open the Intercom help center.

--------------------


### hideMessenger()

```typescript
hideMessenger() => Promise<void>
```

Hide the Intercom messenger.

--------------------


### displayLauncher()

```typescript
displayLauncher() => Promise<void>
```

Show the Intercom launcher button.

--------------------


### hideLauncher()

```typescript
hideLauncher() => Promise<void>
```

Hide the Intercom launcher button.

--------------------


### displayInAppMessages()

```typescript
displayInAppMessages() => Promise<void>
```

Enable in-app messages from Intercom.

--------------------


### hideInAppMessages()

```typescript
hideInAppMessages() => Promise<void>
```

Disable in-app messages from Intercom.

--------------------


### displayCarousel(...)

```typescript
displayCarousel(options: IntercomCarouselOptions) => Promise<void>
```

Display a specific Intercom carousel.

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomcarouseloptions">IntercomCarouselOptions</a></code> |

--------------------


### displayArticle(...)

```typescript
displayArticle(options: IntercomArticleOptions) => Promise<void>
```

Display a specific Intercom article.

| Param         | Type                                                                      |
| ------------- | ------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomarticleoptions">IntercomArticleOptions</a></code> |

--------------------


### setUserHash(...)

```typescript
setUserHash(options: IntercomUserHashOptions) => Promise<void>
```

Set the HMAC for identity verification.

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomuserhashoptions">IntercomUserHashOptions</a></code> |

--------------------


### setUserJwt(...)

```typescript
setUserJwt(options: IntercomUserJwtOptions) => Promise<void>
```

Set JWT for secure messenger authentication.

| Param         | Type                                                                      |
| ------------- | ------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercomuserjwtoptions">IntercomUserJwtOptions</a></code> |

--------------------


### setBottomPadding(...)

```typescript
setBottomPadding(options: IntercomBottomPaddingOptions) => Promise<void>
```

Set the bottom padding for the Intercom messenger UI.

| Param         | Type                                                                                  |
| ------------- | ------------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercombottompaddingoptions">IntercomBottomPaddingOptions</a></code> |

--------------------


### sendPushTokenToIntercom(...)

```typescript
sendPushTokenToIntercom(options: IntercomPushTokenOptions) => Promise<void>
```

Send a push notification token to Intercom.

| Param         | Type                                                                          |
| ------------- | ----------------------------------------------------------------------------- |
| **`options`** | <code><a href="#intercompushtokenoptions">IntercomPushTokenOptions</a></code> |

--------------------


### receivePush(...)

```typescript
receivePush(notification: IntercomPushNotificationData) => Promise<void>
```

Handle a received Intercom push notification.

| Param              | Type                                                                                  |
| ------------------ | ------------------------------------------------------------------------------------- |
| **`notification`** | <code><a href="#intercompushnotificationdata">IntercomPushNotificationData</a></code> |

--------------------


### addListener('windowDidShow', ...)

```typescript
addListener(eventName: 'windowDidShow', listenerFunc: () => void) => Promise<PluginListenerHandle>
```

Listen for when the Intercom window is shown.

| Param              | Type                         |
| ------------------ | ---------------------------- |
| **`eventName`**    | <code>'windowDidShow'</code> |
| **`listenerFunc`** | <code>() =&gt; void</code>   |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('windowDidHide', ...)

```typescript
addListener(eventName: 'windowDidHide', listenerFunc: () => void) => Promise<PluginListenerHandle>
```

Listen for when the Intercom window is hidden.

| Param              | Type                         |
| ------------------ | ---------------------------- |
| **`eventName`**    | <code>'windowDidHide'</code> |
| **`listenerFunc`** | <code>() =&gt; void</code>   |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

Remove all event listeners.

--------------------


### Interfaces


#### IntercomLoadOptions

| Prop                | Type                |
| ------------------- | ------------------- |
| **`appId`**         | <code>string</code> |
| **`apiKeyIOS`**     | <code>string</code> |
| **`apiKeyAndroid`** | <code>string</code> |


#### IntercomIdentifiedUserOptions

| Prop         | Type                |
| ------------ | ------------------- |
| **`userId`** | <code>string</code> |
| **`email`**  | <code>string</code> |


#### IntercomUserUpdateOptions

| Prop                   | Type                                 |
| ---------------------- | ------------------------------------ |
| **`userId`**           | <code>string</code>                  |
| **`email`**            | <code>string</code>                  |
| **`name`**             | <code>string</code>                  |
| **`phone`**            | <code>string</code>                  |
| **`languageOverride`** | <code>string</code>                  |
| **`customAttributes`** | <code>{ [key: string]: any; }</code> |


#### IntercomLogEventOptions

| Prop       | Type                                 |
| ---------- | ------------------------------------ |
| **`name`** | <code>string</code>                  |
| **`data`** | <code>{ [key: string]: any; }</code> |


#### IntercomMessageComposerOptions

| Prop          | Type                |
| ------------- | ------------------- |
| **`message`** | <code>string</code> |


#### IntercomCarouselOptions

| Prop             | Type                |
| ---------------- | ------------------- |
| **`carouselId`** | <code>string</code> |


#### IntercomArticleOptions

| Prop            | Type                |
| --------------- | ------------------- |
| **`articleId`** | <code>string</code> |


#### IntercomUserHashOptions

| Prop       | Type                |
| ---------- | ------------------- |
| **`hmac`** | <code>string</code> |


#### IntercomUserJwtOptions

| Prop      | Type                |
| --------- | ------------------- |
| **`jwt`** | <code>string</code> |


#### IntercomBottomPaddingOptions

| Prop        | Type                |
| ----------- | ------------------- |
| **`value`** | <code>number</code> |


#### IntercomPushTokenOptions

| Prop        | Type                |
| ----------- | ------------------- |
| **`value`** | <code>string</code> |


#### IntercomPushNotificationData


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

</docgen-api>
