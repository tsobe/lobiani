FROM nginx:stable-alpine
COPY dist /usr/share/nginx/html
ENV NG_CONFIG=/etc/nginx/conf.d/default.conf

# Configure rewrite rule to enable HTML5 history mode
# https://router.vuejs.org/guide/essentials/history-mode.html#example-server-configurations
RUN sed -i '/index  index.html index.htm/a\\ttry_files $uri $uri\/ \/index.html;' $NG_CONFIG
RUN if ! grep -q try_files $NG_CONFIG; then echo "Configuration patching failed. `cat $NG_CONFIG`" && exit 1; fi

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
