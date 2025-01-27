# Use a single stage for simplicity
FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install
RUN npm install -g expo-cli
RUN npm install -g serve

COPY . .

# Copy the inject-config.js script
COPY inject-config.js /app/inject-config.js

# Expose the port
EXPOSE 5000

# Command to run the inject-config.js script, build the app, and then serve it
CMD ["sh", "-c", "node /app/inject-config.js && npx expo export --platform web --output-dir web-build && serve -s /app/web-build -l 5000"]