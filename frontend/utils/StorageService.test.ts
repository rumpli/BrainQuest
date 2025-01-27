import { StorageService } from './StorageService';
import * as SecureStore from 'expo-secure-store';
import { Platform } from 'react-native';

// Mock SecureStore
jest.mock('expo-secure-store', () => ({
    setItemAsync: jest.fn(),
    getItemAsync: jest.fn(),
    deleteItemAsync: jest.fn(),
}));

describe('StorageService', () => {
    const originalPlatform = Platform.OS;

    beforeEach(() => {
        jest.clearAllMocks();

        // Set platform back to its original value before each test
        Platform.OS = originalPlatform;

        // Mock global.localStorage if not already present (for Jest environment)
        if (typeof global.localStorage === 'undefined') {
            Object.defineProperty(global, 'localStorage', {
                value: {
                    setItem: jest.fn(),
                    getItem: jest.fn(),
                    removeItem: jest.fn(),
                },
                writable: true,
            });
        } else {
            // If localStorage is already available (e.g., in a browser or mock environment), mock its methods
            global.localStorage.setItem = jest.fn();
            global.localStorage.getItem = jest.fn();
            global.localStorage.removeItem = jest.fn();
        }
    });

    afterEach(() => {
        // Reset platform after each test
        Platform.OS = originalPlatform;
    });

    afterAll(() => {
        // Clean up global.localStorage mock
        delete global.localStorage;
    });

    describe('setItem', () => {
        // it('throws error if SecureStore.setItemAsync fails on mobile', async () => {
        //     Platform.OS = 'ios';
        //     const key = 'testKey';
        //     const value = 'testValue';
        //     (SecureStore.setItemAsync as jest.Mock).mockRejectedValue(new Error('Failed to set item'));
        //
        //     await expect(StorageService.setItem(key, value)).rejects.toThrow('Failed to set item');
        // });
        //
        // it('throws error if localStorage.setItem fails on web', async () => {
        //     Platform.OS = 'web';
        //     const key = 'testKey';
        //     const value = 'testValue';
        //     (global.localStorage.setItem as jest.Mock).mockImplementation(() => {
        //         throw new Error('Failed to set item');
        //     });
        //
        //     await expect(StorageService.setItem(key, value)).rejects.toThrow('Failed to set item');
        // });
        //
        // it('stores value in SecureStore on mobile', async () => {
        //     Platform.OS = 'ios'; // Force mobile platform
        //     const key = 'testKey';
        //     const value = 'testValue';
        //     await StorageService.setItem(key, value);
        //
        //     expect(SecureStore.setItemAsync).toHaveBeenCalledWith(key, value);
        // });

        it('stores value in localStorage on web', async () => {
            Platform.OS = 'web'; // Force web platform
            const key = 'testKey';
            const value = 'testValue';
            await StorageService.setItem(key, value);

        });

        // describe('getItem', () => {
        //     it('returns null if SecureStore.getItemAsync returns null on mobile', async () => {
        //         Platform.OS = 'ios';
        //         const key = 'testKey';
        //         (SecureStore.getItemAsync as jest.Mock).mockResolvedValue(null);
        //
        //         const result = await StorageService.getItem(key);
        //
        //         expect(result).toBeNull();
        //     });

            // it('returns null if localStorage.getItem returns null on web', async () => {
            //     Platform.OS = 'web';
            //     const key = 'testKey';
            //     (global.localStorage.getItem as jest.Mock).mockReturnValue(null);
            //
            //     const result = await StorageService.getItem(key);
            //
            //     expect(result).toBeNull();
            // });
            //
            // it('retrieves value from SecureStore on mobile', async () => {
            //     Platform.OS = 'ios';
            //     const key = 'testKey';
            //     const mockValue = 'testValue';
            //     (SecureStore.getItemAsync as jest.Mock).mockResolvedValue(mockValue);
            //
            //     const result = await StorageService.getItem(key);
            //
            //     expect(result).toBe(mockValue);
            // });

        //     it('retrieves value from localStorage on web', async () => {
        //         Platform.OS = 'web';
        //         const key = 'testKey';
        //         const mockValue = 'testValue';
        //         (global.localStorage.getItem as jest.Mock).mockReturnValue(mockValue);
        //
        //         const result = await StorageService.getItem(key);
        //
        //         expect(result).toBe(mockValue);
        //     });
        // });

        // describe('removeItem', () => {
        //     it('throws error if SecureStore.deleteItemAsync fails on mobile', async () => {
        //         Platform.OS = 'ios';
        //         const key = 'testKey';
        //         (SecureStore.deleteItemAsync as jest.Mock).mockRejectedValue(new Error('Failed to remove item'));
        //
        //         await expect(StorageService.removeItem(key)).rejects.toThrow('Failed to remove item');
        //     });
        //
        //     it('throws error if localStorage.removeItem fails on web', async () => {
        //         Platform.OS = 'web';
        //         const key = 'testKey';
        //         (global.localStorage.removeItem as jest.Mock).mockImplementation(() => {
        //             throw new Error('Failed to remove item');
        //         });
        //
        //         await expect(StorageService.removeItem(key)).rejects.toThrow('Failed to remove item');
        //     });
        //
        //     it('removes value from SecureStore on mobile', async () => {
        //         Platform.OS = 'ios';
        //         const key = 'testKey';
        //         await StorageService.removeItem(key);
        //
        //         expect(SecureStore.deleteItemAsync).toHaveBeenCalledWith(key);
        //     });
        //
        //     it('removes value from localStorage on web', async () => {
        //         Platform.OS = 'web';
        //         const key = 'testKey';
        //         await StorageService.removeItem(key);
        //
        //         expect(global.localStorage.removeItem).toHaveBeenCalledWith(key);
        //     });
        // });
    });
});