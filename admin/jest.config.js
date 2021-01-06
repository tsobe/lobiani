module.exports = {
  preset: '@vue/cli-plugin-unit-jest',
  setupFilesAfterEnv: [
    '<rootDir>/tests/setup.js',
    'jest-extended'
  ],
  collectCoverage: process.env.COLLECT_COVERAGE === 'true',
  collectCoverageFrom: [
    'src/**/*.{js,jsx,vue}'
  ],
  reporters: process.env.ADD_JUNIT_REPORTER === 'true' ? ['default', 'jest-junit'] : ['default']
}
