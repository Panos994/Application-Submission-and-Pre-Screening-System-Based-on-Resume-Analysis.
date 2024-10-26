# Use the node image from official Docker Hub
FROM node:20-alpine3.19 as build-stage
# Set the working directory
WORKDIR /app
# Copy the package.json and package-lock.json files
COPY package*.json ./
# Install the project dependencies
RUN npm install
# Copy the rest of the project files to the container
COPY . .
# Build the Vue.js application to the production mode
RUN npm run build

# Use a lightweight Node.js image to serve the application
FROM node:20-alpine3.19 as production-stage
WORKDIR /app
# Copy the built application from the previous stage
COPY --from=build-stage /app/dist ./dist
# Install a simple server
RUN npm install -g serve
# Start the server
CMD ["serve", "-s", "dist", "-l", "80"]
# Expose the port (default is 3000 for serve)
EXPOSE 80