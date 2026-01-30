import type { PluginListenerHandle } from '@capacitor/core';

export interface CapgoIntercomPlugin {
  /**
   * Initialize Intercom with API keys at runtime.
   * Use this if you prefer not to configure keys in capacitor.config.
   */
  loadWithKeys(options: IntercomLoadOptions): Promise<void>;

  /**
   * Register a known user with Intercom.
   * At least one of userId or email must be provided.
   */
  registerIdentifiedUser(options: IntercomIdentifiedUserOptions): Promise<void>;

  /**
   * Register an anonymous user with Intercom.
   */
  registerUnidentifiedUser(): Promise<void>;

  /**
   * Update user attributes in Intercom.
   */
  updateUser(options: IntercomUserUpdateOptions): Promise<void>;

  /**
   * Log the user out of Intercom.
   */
  logout(): Promise<void>;

  /**
   * Log a custom event in Intercom.
   */
  logEvent(options: IntercomLogEventOptions): Promise<void>;

  /**
   * Open the Intercom messenger.
   */
  displayMessenger(): Promise<void>;

  /**
   * Open the message composer with a pre-filled message.
   */
  displayMessageComposer(options: IntercomMessageComposerOptions): Promise<void>;

  /**
   * Open the Intercom help center.
   */
  displayHelpCenter(): Promise<void>;

  /**
   * Hide the Intercom messenger.
   */
  hideMessenger(): Promise<void>;

  /**
   * Show the Intercom launcher button.
   */
  displayLauncher(): Promise<void>;

  /**
   * Hide the Intercom launcher button.
   */
  hideLauncher(): Promise<void>;

  /**
   * Enable in-app messages from Intercom.
   */
  displayInAppMessages(): Promise<void>;

  /**
   * Disable in-app messages from Intercom.
   */
  hideInAppMessages(): Promise<void>;

  /**
   * Display a specific Intercom carousel.
   */
  displayCarousel(options: IntercomCarouselOptions): Promise<void>;

  /**
   * Display a specific Intercom article.
   */
  displayArticle(options: IntercomArticleOptions): Promise<void>;

  /**
   * Display a specific Intercom survey.
   */
  displaySurvey(options: IntercomSurveyOptions): Promise<void>;

  /**
   * Set the HMAC for identity verification.
   */
  setUserHash(options: IntercomUserHashOptions): Promise<void>;

  /**
   * Set JWT for secure messenger authentication.
   */
  setUserJwt(options: IntercomUserJwtOptions): Promise<void>;

  /**
   * Set the bottom padding for the Intercom messenger UI.
   */
  setBottomPadding(options: IntercomBottomPaddingOptions): Promise<void>;

  /**
   * Send a push notification token to Intercom.
   */
  sendPushTokenToIntercom(options: IntercomPushTokenOptions): Promise<void>;

  /**
   * Handle a received Intercom push notification.
   */
  receivePush(notification: IntercomPushNotificationData): Promise<void>;

  /**
   * Get the number of unread conversations for the current user.
   */
  getUnreadConversationCount(): Promise<IntercomUnreadCountResult>;

  /**
   * Listen for when the Intercom window is shown.
   */
  addListener(eventName: 'windowDidShow', listenerFunc: () => void): Promise<PluginListenerHandle>;

  /**
   * Listen for when the Intercom window is hidden.
   */
  addListener(eventName: 'windowDidHide', listenerFunc: () => void): Promise<PluginListenerHandle>;

  /**
   * Listen for changes in the unread conversation count.
   */
  addListener(
    eventName: 'unreadCountDidChange',
    listenerFunc: (data: IntercomUnreadCountResult) => void,
  ): Promise<PluginListenerHandle>;

  /**
   * Remove all event listeners.
   */
  removeAllListeners(): Promise<void>;
}

export interface IntercomLoadOptions {
  appId?: string;
  apiKeyIOS?: string;
  apiKeyAndroid?: string;
}

export interface IntercomIdentifiedUserOptions {
  userId?: string;
  email?: string;
}

export interface IntercomUserUpdateOptions {
  userId?: string;
  email?: string;
  name?: string;
  phone?: string;
  languageOverride?: string;
  customAttributes?: { [key: string]: any };
  companies?: IntercomCompany[];
}

export interface IntercomCompany {
  companyId: string;
  name?: string;
  plan?: string;
  monthlySpend?: number;
  createdAt?: number;
  customAttributes?: { [key: string]: any };
}

export interface IntercomLogEventOptions {
  name: string;
  data?: { [key: string]: any };
}

export interface IntercomMessageComposerOptions {
  message: string;
}

export interface IntercomCarouselOptions {
  carouselId: string;
}

export interface IntercomArticleOptions {
  articleId: string;
}

export interface IntercomSurveyOptions {
  surveyId: string;
}

export interface IntercomUserHashOptions {
  hmac: string;
}

export interface IntercomUserJwtOptions {
  jwt: string;
}

export interface IntercomBottomPaddingOptions {
  value: number;
}

export interface IntercomPushTokenOptions {
  value: string;
}

export interface IntercomPushNotificationData {
  [key: string]: any;
}

export interface IntercomUnreadCountResult {
  count: number;
}
