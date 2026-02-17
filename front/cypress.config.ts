import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    setupNodeEvents(on, config) {
      const codeCoverageTask = require('@cypress/code-coverage/task');
      codeCoverageTask(on, config);
      return config;
    },
  },
});
