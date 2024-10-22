# Use the Node.js base image
FROM node:16

# Set the working directory
WORKDIR /app

# Copy all files from the host to the container
COPY . .

# Install dependencies
RUN npm install

# Build the project
RUN npm run build

# Expose the frontend port (8080)
EXPOSE 8080

# Start the frontend server
CMD ["npm", "run", "serve"]
