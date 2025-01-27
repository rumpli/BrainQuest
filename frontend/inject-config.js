const fs = require('fs');
const path = require('path');

// Define the path to the config.js in the built web files
const configPath = path.join(__dirname, 'config.js');

// Read the config.js file
fs.readFile(configPath, 'utf8', (err, data) => {
    if (err) {
        console.error('Error reading config.js:', err);
        process.exit(1);
    }

    // Replace the placeholders in config.js with environment variables
    const updatedConfig = data
        .replace('default-api-url', process.env.API_URL || 'default-api-url')

    // Write the updated config back to the file
    fs.writeFile(configPath, updatedConfig, 'utf8', (err) => {
        if (err) {
            console.error('Error writing config.js:', err);
            process.exit(1);
        }
        console.log('config.js successfully updated with environment variables.');
    });
});