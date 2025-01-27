import { validatePlayerName } from './validatePlayerName';

describe('validatePlayerName', () => {
    it('should pass for a simple valid name', () => {
        const result = validatePlayerName('John');
        expect(result.isValid).toBe(true);
        expect(result.error).toBeNull();
        expect(result.name).toBe('John');
    });

    it('should allow a single space between words', () => {
        const result = validatePlayerName('John Doe');
        expect(result.isValid).toBe(true);
        expect(result.error).toBeNull();
        expect(result.name).toBe('John Doe');
    });

    it('should trim whitespace from the start and end', () => {
        const result = validatePlayerName('   John Doe   ');
        expect(result.isValid).toBe(true);
        expect(result.error).toBeNull();
        expect(result.name).toBe('John Doe');
    });

    it('should fail for names with two consecutive spaces', () => {
        const result = validatePlayerName('John  Doe');
        expect(result.isValid).toBe(false);
        expect(result.error).toBe('Only letters, numbers, a single whitespace between words, and "-", "_", "." are allowed.');
    });

    it('should fail if the name is less than 2 characters', () => {
        const result = validatePlayerName('J');
        expect(result.isValid).toBe(false);
        expect(result.error).toBe('Name must be at least 2 characters long.');
    });

    it('should fail if the name contains disallowed characters', () => {
        const result = validatePlayerName('John@Doe');
        expect(result.isValid).toBe(false);
        expect(result.error).toBe('Only letters, numbers, a single whitespace between words, and "-", "_", "." are allowed.');
    });

    it('should pass for names with allowed special characters', () => {
        const result = validatePlayerName('John_Doe');
        expect(result.isValid).toBe(true);
        expect(result.error).toBeNull();
        expect(result.name).toBe('John_Doe');
    });

    it('should pass for a name with numbers', () => {
        const result = validatePlayerName('John123');
        expect(result.isValid).toBe(true);
        expect(result.error).toBeNull();
        expect(result.name).toBe('John123');
    });

    it('should pass for a name with special characters', () => {
        const result = validatePlayerName('John.Doe');
        expect(result.isValid).toBe(true);
        expect(result.error).toBeNull();
        expect(result.name).toBe('John.Doe');
    });

    it('should fail if the name contains multiple spaces between words', () => {
        const result = validatePlayerName('John   Doe');
        expect(result.isValid).toBe(false);
        expect(result.error).toBe('Only letters, numbers, a single whitespace between words, and "-", "_", "." are allowed.');
    });

    it('should fail if the name only contains whitespace', () => {
        const result = validatePlayerName('      ');
        expect(result.isValid).toBe(false);
        expect(result.error).toBe('Name must be at least 2 characters long.');
    });
});
