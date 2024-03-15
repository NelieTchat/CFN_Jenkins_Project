# Specify the Base Image
FROM centos:7

# Add metadata indicating the maintainer
LABEL maintainer="my docker container"

# Update packages and install Apache
RUN yum -y update
RUN yum -y install httpd 
RUN yum clean all    

# Copy the entire folder into the container
COPY . /var/www/html/

# Expose port 80 to the outside world
EXPOSE 80

# Set the entry point
ENTRYPOINT ["/usr/sbin/httpd"]

# Start Apache in the foreground when container starts
CMD ["-D", "FOREGROUND"]

# # Use the official ARM-compatible nginx base image
# FROM nginx:stable-alpine

# # Set the working directory in the container
# WORKDIR /usr/share/nginx/html

# # Copy all application files and directories to the container
# COPY . .

# # Expose port 80 for web traffic
# EXPOSE 80

# # Start the Nginx web server
# CMD ["nginx", "-g", "daemon off;"]
