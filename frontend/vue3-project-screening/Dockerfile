FROM ubuntu:latest
LABEL authors="it2021154"

ENTRYPOINT ["top", "-b"]

FROM node:16
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
CMD ["npm", "run", "serve"]
