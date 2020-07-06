variable "do_token" {}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_vpc" "lobiani-fra1" {
  name     = "lobiani-fra1"
  region   = "fra1"
}

resource "digitalocean_kubernetes_cluster" "lobiani" {
  name    = "lobiani-test"
  region  = "fra1"
  version = "1.18.3-do.0"
  vpc_uuid = digitalocean_vpc.lobiani-fra1.id

  node_pool {
    name       = "worker-pool"
    size       = "s-1vcpu-2gb"
    node_count = 2
  }
}

provider "kubernetes" {
  host = digitalocean_kubernetes_cluster.lobiani.kube_config.0.host
  token = digitalocean_kubernetes_cluster.lobiani.kube_config.0.token
  cluster_ca_certificate = base64decode(digitalocean_kubernetes_cluster.lobiani.kube_config.0.cluster_ca_certificate)
  load_config_file = false
}

module "nginx-ingress-controller" {
  source  = "byuoitav/nginx-ingress-controller/kubernetes"
  version = "0.1.13"
}

data "digitalocean_domain" "default" {
  name = "baybay.dev"
}

resource "digitalocean_record" "lobiani-domain" {
  domain = data.digitalocean_domain.default.name
  type   = "A"
  name   = "test-lobiani"
  value  = module.nginx-ingress-controller.lb_address
}

resource "digitalocean_record" "axonserver-gui-domain" {
  domain = data.digitalocean_domain.default.name
  type   = "A"
  name   = "test-axonserver-gui.lobiani"
  value  = module.nginx-ingress-controller.lb_address
}

output "cluster_endpoint" {
  value = digitalocean_kubernetes_cluster.lobiani.endpoint
}

output "lb_ip" {
  value = module.nginx-ingress-controller.lb_address
}

