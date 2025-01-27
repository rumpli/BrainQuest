// app/index.tsx
import React, { useEffect } from 'react';
import { useRouter } from 'expo-router';

export default function Index() {
    const router = useRouter();

    useEffect(() => {
        // Make sure the navigation is happening after the root layout is ready
        const timer = setTimeout(() => {
            router.push('/(main)/main'); // Navigate to your main screen
        }, 100); // A small delay to ensure the layout is ready (this could vary)

        // Cleanup function to clear the timeout if needed
        return () => clearTimeout(timer);
    }, [router]);

    return null; // No content is needed in this screen
}
