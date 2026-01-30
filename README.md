<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Capacitor Intercom</h3>
<p align="center"><strong><code>@capgo/capacitor-intercom</code></strong></p>
<p align="center">
  Capacitor plugin for Intercom. Supports iOS and Android.
</p>

<p align="center">
  <a href="https://capgo.app/docs/plugins/intercom/"><strong>Documentation</strong></a>
</p>

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

The Intercom iOS SDK (`~> 18.0`) is included automatically via CocoaPods or Swift Package Manager.

### Android

The Intercom Android SDK (`17.2.0`) is included automatically via Gradle.

## API

