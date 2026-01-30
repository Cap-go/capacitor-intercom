import { registerPlugin } from '@capacitor/core';

import type { CapgoIntercomPlugin } from './definitions';

const CapgoIntercom = registerPlugin<CapgoIntercomPlugin>('CapgoIntercom', {
  web: () => import('./web').then((m) => new m.CapgoIntercomWeb()),
});

export * from './definitions';
export { CapgoIntercom };
