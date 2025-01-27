import { Platform } from 'react-native';
import log from './logger';

let SecureStore: any = null;
let platform: string | null = null;

const initializeStorage = async () => {
    platform = Platform.OS;

    if (platform !== 'web') {
        try {
            const module = await import('expo-secure-store');
            SecureStore = module;
        } catch (error) {
            log.warn('Failed to load expo-secure-store:', error);
        }
    }
};

initializeStorage();

const isLocalStorageAvailable = (() => {
    try {
        localStorage.setItem('test', 'test');
        localStorage.removeItem('test');
        return true;
    } catch (error) {
        log.warn('localStorage is not available. Falling back to in-memory storage.');
        return false;
    }
})();

const inMemoryStore: Record<string, string> = {};

export const StorageService = {
    async setItem(key: string, value: string): Promise<void> {
        try {
            if (platform === 'web') {
                log.debug('Platform is web.');
                if (isLocalStorageAvailable) {
                    // Ensure the localStorage item is set if localStorage is available
                    log.debug('Setting item in localStorage');
                    localStorage.setItem(key, value);
                } else {
                    log.debug('Using in-memory store for web (localStorage not available)');
                    inMemoryStore[key] = value;
                }
            } else if (SecureStore && typeof SecureStore.setItemAsync === 'function') {
                log.debug('Platform is mobile. Setting item in SecureStore.');
                await SecureStore.setItemAsync(key, value);
            }
        } catch (error) {
            log.error('Error setting item in storage:', error);
            throw new Error('Failed to set item');
        }
    },

    async getItem(key: string): Promise<string | null> {
        try {
            if (platform === 'web') {
                if (isLocalStorageAvailable) {
                    const value = localStorage.getItem(key);
                    return value ? value : null;
                } else {
                    return inMemoryStore[key] || null;
                }
            } else if (SecureStore && typeof SecureStore.getItemAsync === 'function') {
                const value = await SecureStore.getItemAsync(key);
                return value ? value : null;
            }
        } catch (error) {
            log.error('Error getting item from storage:', error);
            throw new Error('Failed to get item');
        }
    },

    async removeItem(key: string): Promise<void> {
        try {
            if (platform === 'web') {
                if (isLocalStorageAvailable) {
                    localStorage.removeItem(key);
                } else {
                    delete inMemoryStore[key];
                }
            } else if (SecureStore && typeof SecureStore.deleteItemAsync === 'function') {
                await SecureStore.deleteItemAsync(key);
            }
        } catch (error) {
            log.error('Error removing item from storage:', error);
            throw error;
        }
    }
};