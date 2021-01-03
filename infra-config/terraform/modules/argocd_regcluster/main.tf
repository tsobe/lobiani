terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "~> 2.3.0"
    }
  }
}

data "digitalocean_kubernetes_cluster" "argo-cd" {
  name = var.argocd_k8s_cluster_name
}

provider "kubernetes" {
  load_config_file = false
  host = data.digitalocean_kubernetes_cluster.argo-cd.endpoint
  token = data.digitalocean_kubernetes_cluster.argo-cd.kube_config.0.token
  cluster_ca_certificate = base64decode(data.digitalocean_kubernetes_cluster.argo-cd.kube_config.0.cluster_ca_certificate)
}

resource "kubernetes_secret" "cluster-secret" {
  metadata {
    name = format("%s-secret", var.new_cluster_name)
    namespace = "argocd"
    labels = {
      "argocd.argoproj.io/secret-type" = "cluster"
    }
  }
  data = {
    name = var.new_cluster_name
    server = var.new_cluster_endpoint
    config = jsonencode({
      bearerToken = var.new_cluster_token
      tlsClientConfig = {
        insecure = false
        caData = var.new_cluster_ca_certificate
      }
    })
  }
  type = "Opaque"
}
