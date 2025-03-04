FROM node:18-alpine

WORKDIR /app

# Copy package.json and package-lock.json (if exists)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy all other files
COPY . ./

# Build the app
RUN npm run build

# Install serve globally to serve the production build
RUN npm install -g serve

# Expose the port that the app will run on
EXPOSE 3000

# Use the serve command to start the app on port 3000
CMD ["serve", "-s", "dist", "-l", "3000"]
