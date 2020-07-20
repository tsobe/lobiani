provider "digitalocean" {
  version = "~> 1.20"
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
    size = "s-2vcpu-2gb"
    node_count = 1

    labels = {
      node-type = "worker-pool"
    }
  }
}

provider "kubernetes" {
  version = "~> 1.11"
  host = digitalocean_kubernetes_cluster.cluster.kube_config.0.host
  token = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
  cluster_ca_certificate = base64decode(digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate)
  load_config_file = false
}

provider "helm" {
  version = "~> 1.2"
  kubernetes {
    host = digitalocean_kubernetes_cluster.cluster.kube_config.0.host
    token = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
    cluster_ca_certificate = base64decode(digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate)
    load_config_file = false
  }
}

resource "kubernetes_namespace" "ingress-nginx-ns" {
  metadata {
    name = "ingress-nginx"
  }
}

resource "helm_release" "ingerss-nginx" {
  depends_on = [kubernetes_namespace.ingress-nginx-ns]

  name = "ingress-nginx"
  repository = "https://helm.nginx.com/stable"
  chart = "nginx-ingress"
  version = "0.5.2"
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

resource "kubernetes_namespace" "ingress-nginx-ns-v2" {
  metadata {
    name = "ingress-nginx-v2"
  }
}

resource "helm_release" "ingerss-nginx-v2" {
  depends_on = [kubernetes_namespace.ingress-nginx-ns-v2]

  name = "ingress-nginx-v2"
  repository = "https://kubernetes.github.io/ingress-nginx"
  chart = "ingress-nginx"
  version = "2.9.0"
  namespace = "ingress-nginx-v2"

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
    value = format("%s-lb-v2", digitalocean_kubernetes_cluster.cluster.name)
  }
}
