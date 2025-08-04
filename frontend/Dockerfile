FROM nginx:alpine

# Copia la configurazione personalizzata di Nginx
COPY default.conf /etc/nginx/conf.d/default.conf

# Copia i file statici
COPY . /usr/share/nginx/html

# Imposta i permessi
RUN chmod -R 755 /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
