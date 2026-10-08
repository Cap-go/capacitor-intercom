import { Capacitor } from '@capacitor/core';
import { CapacitorUpdater } from '@capgo/capacitor-updater';
import { CapgoIntercom } from '@capgo/capacitor-intercom';
import type { PluginListenerHandle } from '@capacitor/core';

import './style.css';

const logEl = document.getElementById('result-log') as HTMLPreElement;
const platformChip = document.getElementById('platform-chip') as HTMLSpanElement;
const loadChip = document.getElementById('load-chip') as HTMLSpanElement;
const unreadChip = document.getElementById('unread-chip') as HTMLSpanElement;
const listenerChip = document.getElementById('listener-chip') as HTMLSpanElement;

const inputAppId = document.getElementById('input-app-id') as HTMLInputElement;
const inputIosKey = document.getElementById('input-ios-key') as HTMLInputElement;
const inputAndroidKey = document.getElementById('input-android-key') as HTMLInputElement;
const inputUserId = document.getElementById('input-user-id') as HTMLInputElement;
const inputEmail = document.getElementById('input-email') as HTMLInputElement;
const inputName = document.getElementById('input-name') as HTMLInputElement;
const inputPhone = document.getElementById('input-phone') as HTMLInputElement;
const inputComposer = document.getElementById('input-composer') as HTMLInputElement;
const inputArticleId = document.getElementById('input-article-id') as HTMLInputElement;
const inputCarouselId = document.getElementById('input-carousel-id') as HTMLInputElement;
const inputSurveyId = document.getElementById('input-survey-id') as HTMLInputElement;
const inputEventName = document.getElementById('input-event-name') as HTMLInputElement;
const inputEventData = document.getElementById('input-event-data') as HTMLInputElement;
const inputHmac = document.getElementById('input-hmac') as HTMLInputElement;
const inputJwt = document.getElementById('input-jwt') as HTMLInputElement;
const inputPadding = document.getElementById('input-padding') as HTMLInputElement;
const inputPushToken = document.getElementById('input-push-token') as HTMLInputElement;
const inputPushPayload = document.getElementById('input-push-payload') as HTMLInputElement;

let listenersActive = false;
const listenerHandles: PluginListenerHandle[] = [];

const stamp = (): string => new Date().toISOString().slice(11, 19);

const appendLog = (label: string, payload: unknown): void => {
  const body = typeof payload === 'string' ? payload : JSON.stringify(payload, null, 2);
  const next = `[${stamp()}] ${label}\n${body}\n\n`;
  logEl.textContent = next + logEl.textContent;
};

const setChip = (el: HTMLSpanElement, text: string, ok = false): void => {
  el.textContent = text;
  el.classList.toggle('chip-ok', ok);
};

const runAction = async (label: string, action: () => Promise<unknown>): Promise<void> => {
  try {
    const result = await action();
    appendLog(`${label} OK`, result ?? '(no return value)');
  } catch (error) {
    const message = error instanceof Error ? error.message : String(error);
    appendLog(`${label} ERROR`, message);
  }
};

const parseJsonObject = (raw: string): Record<string, unknown> => {
  const trimmed = raw.trim();
  if (!trimmed) {
    return {};
  }
  const parsed = JSON.parse(trimmed) as unknown;
  if (typeof parsed !== 'object' || parsed === null || Array.isArray(parsed)) {
    throw new Error('Expected a JSON object');
  }
  return parsed as Record<string, unknown>;
};

const refreshUnreadChip = async (): Promise<void> => {
  try {
    const { count } = await CapgoIntercom.getUnreadConversationCount();
    setChip(unreadChip, `Unread: ${count}`, true);
  } catch {
    setChip(unreadChip, 'Unread: n/a', false);
  }
};

const startListeners = async (): Promise<void> => {
  if (listenersActive) {
    appendLog('Listeners', 'Already active');
    return;
  }

  listenerHandles.push(
    await CapgoIntercom.addListener('windowDidShow', () => {
      appendLog('Event windowDidShow', {});
    }),
  );
  listenerHandles.push(
    await CapgoIntercom.addListener('windowDidHide', () => {
      appendLog('Event windowDidHide', {});
    }),
  );
  listenerHandles.push(
    await CapgoIntercom.addListener('unreadCountDidChange', (data) => {
      setChip(unreadChip, `Unread: ${data.count}`, true);
      appendLog('Event unreadCountDidChange', data);
    }),
  );

  listenersActive = true;
  setChip(listenerChip, 'Listeners on', true);
  appendLog('Listeners', 'Started window and unread listeners');
};

const stopListeners = async (): Promise<void> => {
  await CapgoIntercom.removeAllListeners();
  listenerHandles.length = 0;
  listenersActive = false;
  setChip(listenerChip, 'Listeners off', false);
  appendLog('Listeners', 'Removed all listeners');
};

platformChip.textContent = Capacitor.getPlatform();
setChip(loadChip, 'Not loaded', false);
void refreshUnreadChip();

document.getElementById('btn-clear-log')?.addEventListener('click', () => {
  logEl.textContent = 'Log cleared.\n';
});

document.getElementById('btn-load')?.addEventListener('click', () =>
  runAction('loadWithKeys', async () => {
    const appId = inputAppId.value.trim();
    const apiKeyIOS = inputIosKey.value.trim();
    const apiKeyAndroid = inputAndroidKey.value.trim();
    await CapgoIntercom.loadWithKeys({
      appId: appId || undefined,
      apiKeyIOS: apiKeyIOS || undefined,
      apiKeyAndroid: apiKeyAndroid || undefined,
    });
    setChip(loadChip, appId ? `Loaded ${appId}` : 'Loaded (config keys)', true);
  }),
);

document.getElementById('btn-register-identified')?.addEventListener('click', () =>
  runAction('registerIdentifiedUser', () =>
    CapgoIntercom.registerIdentifiedUser({
      userId: inputUserId.value.trim() || undefined,
      email: inputEmail.value.trim() || undefined,
    }),
  ),
);

document.getElementById('btn-register-unidentified')?.addEventListener('click', () =>
  runAction('registerUnidentifiedUser', () => CapgoIntercom.registerUnidentifiedUser()),
);

document.getElementById('btn-update-user')?.addEventListener('click', () =>
  runAction('updateUser', () =>
    CapgoIntercom.updateUser({
      userId: inputUserId.value.trim() || undefined,
      email: inputEmail.value.trim() || undefined,
      name: inputName.value.trim() || undefined,
      phone: inputPhone.value.trim() || undefined,
    }),
  ),
);

document.getElementById('btn-logout')?.addEventListener('click', () =>
  runAction('logout', async () => {
    await CapgoIntercom.logout();
    setChip(loadChip, 'Logged out', false);
    await refreshUnreadChip();
  }),
);

document.getElementById('btn-messenger')?.addEventListener('click', () =>
  runAction('displayMessenger', () => CapgoIntercom.displayMessenger()),
);

document.getElementById('btn-help-center')?.addEventListener('click', () =>
  runAction('displayHelpCenter', () => CapgoIntercom.displayHelpCenter()),
);

document.getElementById('btn-article')?.addEventListener('click', () =>
  runAction('displayArticle', () =>
    CapgoIntercom.displayArticle({ articleId: inputArticleId.value.trim() }),
  ),
);

document.getElementById('btn-composer')?.addEventListener('click', () =>
  runAction('displayMessageComposer', () =>
    CapgoIntercom.displayMessageComposer({ message: inputComposer.value }),
  ),
);

document.getElementById('btn-carousel')?.addEventListener('click', () =>
  runAction('displayCarousel', () =>
    CapgoIntercom.displayCarousel({ carouselId: inputCarouselId.value.trim() }),
  ),
);

document.getElementById('btn-survey')?.addEventListener('click', () =>
  runAction('displaySurvey', () =>
    CapgoIntercom.displaySurvey({ surveyId: inputSurveyId.value.trim() }),
  ),
);

document.getElementById('btn-hide-messenger')?.addEventListener('click', () =>
  runAction('hideMessenger', () => CapgoIntercom.hideMessenger()),
);

document.getElementById('btn-show-launcher')?.addEventListener('click', () =>
  runAction('displayLauncher', () => CapgoIntercom.displayLauncher()),
);

document.getElementById('btn-hide-launcher')?.addEventListener('click', () =>
  runAction('hideLauncher', () => CapgoIntercom.hideLauncher()),
);

document.getElementById('btn-show-inapp')?.addEventListener('click', () =>
  runAction('displayInAppMessages', () => CapgoIntercom.displayInAppMessages()),
);

document.getElementById('btn-hide-inapp')?.addEventListener('click', () =>
  runAction('hideInAppMessages', () => CapgoIntercom.hideInAppMessages()),
);

document.getElementById('btn-log-event')?.addEventListener('click', () =>
  runAction('logEvent', () =>
    CapgoIntercom.logEvent({
      name: inputEventName.value.trim(),
      data: parseJsonObject(inputEventData.value),
    }),
  ),
);

document.getElementById('btn-unread-count')?.addEventListener('click', () =>
  runAction('getUnreadConversationCount', async () => {
    const result = await CapgoIntercom.getUnreadConversationCount();
    setChip(unreadChip, `Unread: ${result.count}`, true);
    return result;
  }),
);

document.getElementById('btn-start-listeners')?.addEventListener('click', () => runAction('startListeners', startListeners));

document.getElementById('btn-stop-listeners')?.addEventListener('click', () => runAction('stopListeners', stopListeners));

document.getElementById('btn-set-hash')?.addEventListener('click', () =>
  runAction('setUserHash', () => CapgoIntercom.setUserHash({ hmac: inputHmac.value.trim() })),
);

document.getElementById('btn-set-jwt')?.addEventListener('click', () =>
  runAction('setUserJwt', () => CapgoIntercom.setUserJwt({ jwt: inputJwt.value.trim() })),
);

document.getElementById('btn-set-padding')?.addEventListener('click', () =>
  runAction('setBottomPadding', () =>
    CapgoIntercom.setBottomPadding({ value: Number(inputPadding.value) || 0 }),
  ),
);

document.getElementById('btn-send-push-token')?.addEventListener('click', () =>
  runAction('sendPushTokenToIntercom', () =>
    CapgoIntercom.sendPushTokenToIntercom({ value: inputPushToken.value.trim() }),
  ),
);

document.getElementById('btn-receive-push')?.addEventListener('click', () =>
  runAction('receivePush', () => CapgoIntercom.receivePush(parseJsonObject(inputPushPayload.value))),
);

if (Capacitor.isNativePlatform()) {
  CapacitorUpdater.notifyAppReady().catch((error) => {
    console.error('Capgo notifyAppReady failed', error);
  });
}
