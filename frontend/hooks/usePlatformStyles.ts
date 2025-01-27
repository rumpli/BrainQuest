import { useState, useEffect } from 'react';
import { Platform } from 'react-native';

/**
 * Custom hook to get platform-specific information.
 * Handles server-side rendering (SSR) correctly to avoid hydration mismatch.
 */
export function usePlatformStyles() {
    const [platform, setPlatform] = useState<string | null>(null);

    useEffect(() => {
        setPlatform(Platform.OS);
    }, []);

    const isWeb = platform === 'web';
    const isMobile = platform === 'ios' || platform === 'android';

    return { platform, isWeb, isMobile };
}
