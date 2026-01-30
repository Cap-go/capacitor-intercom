import { WebPlugin } from '@capacitor/core';

import type { CapgoIntercomPlugin, IntercomUnreadCountResult } from './definitions';

export class CapgoIntercomWeb extends WebPlugin implements CapgoIntercomPlugin {
  async loadWithKeys(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async registerIdentifiedUser(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async registerUnidentifiedUser(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async updateUser(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async logout(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async logEvent(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayMessenger(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayMessageComposer(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayHelpCenter(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async hideMessenger(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayLauncher(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async hideLauncher(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayInAppMessages(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async hideInAppMessages(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayCarousel(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displayArticle(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async displaySurvey(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async setUserHash(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async setUserJwt(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async setBottomPadding(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async sendPushTokenToIntercom(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async receivePush(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getUnreadConversationCount(): Promise<IntercomUnreadCountResult> {
    throw this.unimplemented('Not implemented on web.');
  }
}
