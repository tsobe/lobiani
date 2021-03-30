module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
  },
  parserOptions: {
    parser: 'babel-eslint',
  },
  extends: [
    'plugin:vue/essential',
    'plugin:nuxt/recommended'
  ],
  plugins: [],
  rules: {
    'vue/script-indent': [
      'error',
      2,
      {
        'baseIndent': 1,
        'switchCase': 0,
        'ignores': []
      }
    ],
    'space-before-function-paren': [
      'error',
      {
        'anonymous': 'never',
        'named': 'never',
        'asyncArrow': 'always'
      }
    ],
    'object-curly-spacing': [
      'error',
      'never'
    ]
  },
  overrides: [
    {
      'files': [
        '**/__tests__/*.{j,t}s?(x)',
        '**/test/**/*.spec.{j,t}s?(x)'
      ],
      'env': {
        'jest': true
      }
    },
    {
      'files': [
        '*.vue'
      ],
      'rules': {
        'indent': 'off'
      }
    }
  ]
}
