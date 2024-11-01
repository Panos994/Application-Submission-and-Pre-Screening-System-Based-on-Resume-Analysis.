#nginx try frontend dockerfile (CORS issue)

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

# Production Stage
FROM nginx:alpine

#Copy build to Nginx path
COPY --from=build-stage /app/dist /usr/share/nginx/html


# Expose the port Nginx will listen on (this is used internally in Docker)
EXPOSE 80