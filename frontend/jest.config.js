const { pathsToModuleNameMapper } = require('ts-jest');
const { compilerOptions } = require('./tsconfig.json');

module.exports = {
    preset: 'jest-expo',
    testEnvironment: 'jsdom',
    moduleFileExtensions: ['js', 'jsx', 'ts', 'tsx'],
    moduleNameMapper: pathsToModuleNameMapper(compilerOptions.paths, { prefix: '<rootDir>/' }),
    transformIgnorePatterns: ['node_modules/(?!(jest-)?react-native|@react-native|expo(nent)?|@expo(nent)?/.*|@expo-google-fonts/.*|@unimodules/.*|unimodules|sentry-expo|native-base)'],
    setupFilesAfterEnv: ['@testing-library/jest-native/extend-expect']
};