# Infrastructure setup
## Set up Argo CD

[Argo CD](https://argoproj.github.io/argo-cd/) is deployed in the production environment. These instructions assumes that kubeconfig is already
configured and current context is set to production cluster

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
   
## Set up Moon

Similar to Argo CD, [Moon](https://aerokube.com/moon/) is deployed in production environment
   
 
```
kubectl apply -f moon/argocd-app.yaml
```
 
## Bootstrap the uber app

[App Of Apps pattern](https://argoproj.github.io/argo-cd/operator-manual/cluster-bootstrapping/) is followed
here.

## Test env
   
1. Run `terraform apply` in `infra-config/terraform/test` directory.
It should produce similar output
    ```
    Apply complete! Resources: 3 added, 0 changed, 0 destroyed.
    
    Outputs:
    
    cluster_endpoint = https://985e2b0e-b70f-4c94-b68a-a96256fc371b.k8s.ondigitalocean.com
    ```   

2. Capture the `cluster_endpoint` in `TEST_CLUSTER_ENDPOINT` environment variable and run
    ```
    argocd app create test-apps --repo git@bitbucket.org:sevteen/lobiani \
        --path infra-config/apps --dest-namespace argocd \
        --dest-server https://kubernetes.default.svc \
        --sync-policy automated --auto-prune -l environment=test \
        --revision master \
        --helm-set spec.destination.server=$TEST_CLUSTER_ENDPOINT \
        --values test-values.yaml
    ```

## Production env
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
 
1. <strike>Nginx ingress controller from [here](https://kubernetes.github.io/ingress-nginx/deploy) 
    doesn't play well with argo-cd, pre-sync hook fails with "namespace not found" error.
    If the namespace is created manually upfront, then `ingress-nginx-admission-create` job
    fails to complete</strike>
2. <strike>Because of the above, [this](https://docs.nginx.com/nginx-ingress-controller/installation/installation-with-helm/)
    version of Nginx ingress controller is used instead.

    But this version comes with another issue which prevents `cert-manager` to solve `Challenges` and results in following
    error: `http-01 challenge propagation: wrong status code '404', expected '200'`
    
    As a workaround, `acme.cert-manager.io/http01-edit-in-place: "true"` annotation must be added to each `Ingress`
    resource. Read more [here](https://github.com/jetstack/cert-manager/issues/2517)</strike>
    
    Nginx ingress controller now is set up by [Terraform module](https://registry.terraform.io/modules/byuoitav/nginx-ingress-controller/kubernetes/0.1.5)
