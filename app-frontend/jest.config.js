module.exports = {
  preset: '@vue/cli-plugin-unit-jest',
  setupFilesAfterEnv: [
    '<rootDir>/tests/setup.js'
  ],
  collectCoverage: process.env.COLLECT_COVERAGE === 'true'
}
