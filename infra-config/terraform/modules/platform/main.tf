terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "~> 2.3.0"
    }
    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "~> 1.13.3"
    }
    helm = {
      source = "hashicorp/helm"
      version = "~> 2.0.1"
    }
  }
}

resource "digitalocean_vpc" "vcp" {
  name = format("%s-%s-%s", var.name, var.region, var.env)
  region = var.region
}

resource "digitalocean_kubernetes_cluster" "cluster" {
  name = format("%s-%s", var.name, var.env)
  region = var.region
  version = var.k8s_version
  vpc_uuid = digitalocean_vpc.vcp.id

  node_pool {
    name = "worker-pool"
    size = "s-1vcpu-2gb"
    auto_scale = true
    min_nodes = 1
    max_nodes = 2

    labels = {
      node-type = "worker-pool"
    }
  }
}

provider "kubernetes" {
  host = digitalocean_kubernetes_cluster.cluster.kube_config.0.host
  token = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
  cluster_ca_certificate = base64decode(digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate)
  load_config_file = false
}

provider "helm" {
  kubernetes {
    host = digitalocean_kubernetes_cluster.cluster.kube_config.0.host
    token = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
    cluster_ca_certificate = base64decode(digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate)
  }
}

resource "kubernetes_namespace" "ingress-nginx-ns" {
  depends_on = [digitalocean_kubernetes_cluster.cluster]
  metadata {
    name = "ingress-nginx"
  }
}

resource "helm_release" "ingerss-nginx" {
  depends_on = [kubernetes_namespace.ingress-nginx-ns]

  name = "ingress-nginx"
  repository = "https://kubernetes.github.io/ingress-nginx"
  chart = "ingress-nginx"
  version = "2.9.0"
  namespace = "ingress-nginx"

  set {
    name = "controller.nodeSelector.node-type"
    value = "worker-pool"
  }

  set {
    name = "controller.admissionWebhooks.patch.nodeSelector.node-type"
    value = "worker-pool"
  }

  set {
    name = "defaultBackend.nodeSelector.node-type"
    value = "worker-pool"
  }

  set {
    name = "controller.service.annotations.service\\.beta\\.kubernetes\\.io/do-loadbalancer-name"
    value = format("%s-lb", digitalocean_kubernetes_cluster.cluster.name)
  }
}
