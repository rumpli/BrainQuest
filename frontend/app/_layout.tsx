// Layout for the app
import { Stack } from 'expo-router';
import { useEffect, useState } from 'react';
import { Platform } from 'react-native';

/**
 * RootLayout component that sets up the main layout for the app.
 * It uses a Stack navigator to manage different screens.
 *
 * @returns {JSX.Element} The rendered RootLayout component.
 */
export default function RootLayout() {
    const [platform, setPlatform] = useState<string | null>(null);

    useEffect(() => {
        setPlatform(Platform.OS);
    }, []);

    useEffect(() => {
        if (platform === 'web') {
            document.title = 'Brain Quest';
        }
    }, []);

    return (
        <Stack screenOptions={{ headerShown: false }}>
            <Stack.Screen name="(game)" />
            <Stack.Screen name="(highscore)" />
            <Stack.Screen name="(login)" />
            <Stack.Screen name="(main)" />
            <Stack.Screen name="(admin)" />
            <Stack.Screen name="+not-found" />
        </Stack>
    );
}