type ValidationResult = {
    isValid: boolean;
    error: string | null;
    name?: string;
};

export function validatePlayerName(input: string): ValidationResult {
    const allowedSpecialCharacters = ['-', '_', '.'];
    const trimmedName = input.trim(); // Remove whitespace at the start and end
    const regex = new RegExp(`^[a-zA-Z0-9${allowedSpecialCharacters.join('')}]+( [a-zA-Z0-9${allowedSpecialCharacters.join('')}]+)*$`);
    // Explanation of the regex:
    // 1. ^[a-zA-Z0-9._-]+ — Starts with letters, numbers, or special characters
    // 2. ( [a-zA-Z0-9._-]+)* — Allows a single space followed by letters, numbers, or special characters, repeated 0 or more times
    // 3. $ — End of the string

    if (trimmedName.length < 2) {
        return { isValid: false, error: 'Name must be at least 2 characters long.' };
    }

    if (!regex.test(trimmedName)) {
        return {
            isValid: false,
            error: `Only letters, numbers, a single whitespace between words, and "${allowedSpecialCharacters.join('", "')}" are allowed.`
        };
    }

    return { isValid: true, error: null, name: trimmedName };
}
