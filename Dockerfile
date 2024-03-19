# Specify the Base Image
FROM centos:7

# Set the working directory in the container
WORKDIR /var/www/html

# Add metadata indicating the maintainer
LABEL maintainer="my docker container"

# Update packages and install Apache
RUN yum -y update
RUN yum -y install httpd 
RUN yum clean all    

# Copy the contents of the webapp directory into the container
COPY webapp/ .

# Expose port 80 to the outside world
EXPOSE 80

# Set the entry point
ENTRYPOINT ["/usr/sbin/httpd"]

# # Start Apache in the foreground when container starts
CMD ["-D", "FOREGROUND"]

# # Specify the Base Image
# FROM nginx:alpine

# # Set the working directory in the container
# WORKDIR /usr/share/nginx/html

# # Copy the entire webapp directory into the container
# COPY webapp/ .

# # Expose the port your app runs on
# EXPOSE 80

# # Define the command to start the web server
# CMD ["nginx", "-g", "daemon off;"]

