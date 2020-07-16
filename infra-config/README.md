# Infrastructure setup
## Bring up the production cluster

Run `terraform apply` in `infra-config/terraform/prod`. This will bring 
up the cluster dedicated for production environment and other utility tools
(Argo CD and Moon)

## Set up Argo CD

[Argo CD](https://argoproj.github.io/argo-cd/) is deployed in the production environment. These instructions assume that kubeconfig is already
configured and current context is set to production cluster

1. Install Argo CD
    ```
    kubectl apply -n argocd -k argocd
    ```
   
    and the CLI
    
    ```
    brew tap argoproj/tap
    brew install argoproj/tap/argocd
    ```
2. Expose Argo CD on localhost via port-forwarding. This is needed initially until 
   the ingress is configured and endpoint is exposed to the public (after bootstrapping production env below)
    ```
    kubectl port-forward svc/argocd-server -n argocd 8480:80
    ```
3. Log in
    ```
    argocd login localhost:8480
    ```
   Initial password is the the name of argocd pod. It can be obtained as
   `kubectl get pod -l app.kubernetes.io/name=argocd-server -n argocd`
4. Add a private repository
    ```
    argocd repo add git@bitbucket.org:sevteen/lobiani --ssh-private-key-path ~/.ssh/argocd_rsa
    ``` 
5. Configure API access
   
   Add following entry under the `data` element of `argocd-cm` ConfigMap
   ```
   accounts.lobiani: apiKey, login
   ```
   
   And for `argocd-rbac-cm`:
   ```
   policy.csv: |
     p, lobiani, applications, *, */*, allow
   ```
   
   And then generate a token
   ```
   argocd account generate-token --account lobiani
   ```
    
   
## Set up Moon

Similar to Argo CD, [Moon](https://aerokube.com/moon/) is deployed in production environment 
(but as an  Argo CD app)
   
 
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
    
    Nginx ingress controller now is set up by [Terraform module](https://registry.terraform.io/modules/byuoitav/nginx-ingress-controller/kubernetes/0.1.5)
