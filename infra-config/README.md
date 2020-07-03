# Setting up Argo CD in a cluster
## Instructions
1. Install argo-cd to a Kubernetes, quoting [official instructions](https://argoproj.github.io/argo-cd/getting_started/#1-install-argo-cd)
    ```
    kubectl create namespace argocd
    kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
    ```
    and the CLI
    
    ```
    brew tap argoproj/tap
    brew install argoproj/tap/argocd
    ```
    
2. Add a private repository
    ```
    argocd repo add git@bitbucket.org:sevteen/lobiani --ssh-private-key-path ~/.ssh/argocd_rsa
    ```
3. Register test cluster 
    
   see instructions [here](https://argoproj.github.io/argo-cd/getting_started/#5-register-a-cluster-to-deploy-apps-to-optional) 
4. Deploy moon (make sure to use production context)

    ```
    kubectl apply -f moon/argocd-app.yaml
    ```
  
5. Bootstrap the uber app (see [App Of Apps pattern](https://argoproj.github.io/argo-cd/operator-manual/cluster-bootstrapping/))
   
   for `test` environment: 
   ```
   argocd app create test-apps --repo git@bitbucket.org:sevteen/lobiani \
        --path infra-config/apps --dest-namespace argocd \
        --dest-server https://kubernetes.default.svc \
        --sync-policy automated --auto-prune -l environment=test \
        --revision master \
        --values test-values.yaml
   ```
   
   for `production` environment: 
   ```
   argocd app create production-apps --repo git@bitbucket.org:sevteen/lobiani \
        --path infra-config/apps --dest-namespace argocd \
        --dest-server https://kubernetes.default.svc \
        --sync-policy automated -l environment=production \
        --revision production \
        --values production-values.yaml
   ```

## Known issues
 
1. Nginx ingress controller from [here](https://kubernetes.github.io/ingress-nginx/deploy) 
    doesn't play well with argo-cd, pre-sync hook fails with "namespace not found" error.
    If the namespace is created manually upfront, then `ingress-nginx-admission-create` job
    fails to complete 
2. Because of the above, [this](https://docs.nginx.com/nginx-ingress-controller/installation/installation-with-helm/)
    version of Nginx ingress controller is used instead.

    But this version comes with another issue which prevents `cert-manager` to solve `Challenges` and results in following
    error: `http-01 challenge propagation: wrong status code '404', expected '200'`
    
    As a workaround, `acme.cert-manager.io/http01-edit-in-place: "true"` annotation must be added to each `Ingress`
    resource. Read more [here](https://github.com/jetstack/cert-manager/issues/2517)
