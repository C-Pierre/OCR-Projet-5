import { defineConfig } from 'cypress'

export default defineConfig({
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,

  e2e: {
    baseUrl: 'http://localhost:4200',
    reporterOptions: {
      reportDir: "cypress/reports",
      overwrite: false,
      html: false,
      json: true,
    },
    setupNodeEvents(on, config) {
      const codeCoverageTask = require('@cypress/code-coverage/task')
      codeCoverageTask(on, config)
      return config
    },
  },
})
