FROM nginx:alpine

# Add metadata indicating the maintainer
LABEL maintainer="my docker container"

# Copy the entire folder into the container
COPY . /usr/share/nginx/html/

# Expose port 80 to the outside world
EXPOSE 80

# Start nginx in the foreground when container starts
CMD ["nginx", "-g", "daemon off;"]
