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
    size = "s-1vcpu-2gb"
    node_count = 2
  }
}

provider "kubernetes" {
  version = "~> 1.11"
  host = digitalocean_kubernetes_cluster.cluster.kube_config.0.host
  token = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
  cluster_ca_certificate = base64decode(digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate)
  load_config_file = false
}

module "nginx-ingress-controller" {
  source = "byuoitav/nginx-ingress-controller/kubernetes"
  version = "0.1.13"
}

