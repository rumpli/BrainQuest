const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Exclude .test.js, .test.ts, .test.tsx files from bundling
config.transformer = {
  ...config.transformer,
  getTransformOptions: async () => ({
    transform: {
      experimentalImportSupport: false,
      inlineRequires: true,
    },
  }),
};

config.resolver = {
  ...config.resolver,
  blacklistRE: /.*\.test\.(js|ts|tsx)$/, // Ignore all test files
};

module.exports = config;

