# Example App for `@capgo/capacitor-intercom`

This Vite + TypeScript project links to the local plugin so you can exercise Intercom APIs on device or simulator.

## Playground actions

- **Load with keys** – Initialize Intercom with your app ID and platform API keys.
- **Register users** – Sign in identified or unidentified users, update attributes, then log out.
- **Messenger** – Open the messenger, help center, articles, surveys, carousels, and the message composer.
- **Unread events** – Read the unread count and subscribe to `unreadCountDidChange` plus window show/hide events.

## Getting started

```bash
bun install
bun run start
```

Add native shells with `bunx cap add ios` or `bunx cap add android` from this folder to test on a device or simulator.

Do not commit real Intercom keys. Use your own app ID and keys in the load form when testing.
