import React, { Component, ErrorInfo, ReactNode } from 'react';
import { View, Text, StyleSheet } from 'react-native';

interface Props {
    children: ReactNode;
    fallback: ReactNode;
}

interface State {
    hasError: boolean;
    error: Error | null;
}

/**
 * ErrorBoundary component that catches JavaScript errors anywhere in the child component tree,
 * logs those errors, and displays a fallback UI instead of the component tree that crashed.
 *
 * @class
 * @extends {Component<Props, State>}
 */
class ErrorBoundary extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = { hasError: false, error: null };
    }

    /**
     * Update state so the next render will show the fallback UI.
     *
     * @param {Error} error - The error that was thrown.
     * @returns {State} The updated state.
     */
    static getDerivedStateFromError(error: Error): State {
        return { hasError: true, error };
    }

    /**
     * Log the error to an error reporting service.
     *
     * @param {Error} error - The error that was thrown.
     * @param {ErrorInfo} info - An object with a componentStack key containing information about which component threw the error.
     */
    componentDidCatch(error: Error, info: ErrorInfo) {
        console.error("Uncaught error:", error, info);
    }

    /**
     * Render the component.
     *
     * @returns {ReactNode} The rendered component.
     */
    render() {
        if (this.state.hasError) {
            return (
                <View style={styles.container}>
                    <Text style={styles.title}>Something went wrong.</Text>
                    <Text style={styles.error}>{this.state.error?.message}</Text>
                    {this.props.fallback}
                </View>
            );
        }

        return this.props.children;
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
    },
    error: {
        fontSize: 16,
        color: 'red',
    },
});

export default ErrorBoundary;