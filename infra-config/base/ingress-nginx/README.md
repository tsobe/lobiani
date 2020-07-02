Based on `ingress-nginx.yaml` obtained as:
```
helm template ingress-nginx nginx-stable/nginx-ingress -n ingress-nginx --version 0.5.2 > ingress-nginx.yaml
```

Make sure to add following repo:
````
helm repo add nginx-stable https://helm.nginx.com/stable
````
