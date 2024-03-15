FROM nginx:alpine

# Add metadata indicating the maintainer
LABEL maintainer="my docker container"

# Set the working directory inside the container
WORKDIR /usr/share/nginx/html

# Copy specific files and directories from your application into the container
COPY webapp/ ./

# Expose port 80 to the outside world
EXPOSE 80

# Start nginx in the foreground when container starts
CMD ["nginx", "-g", "daemon off;"]