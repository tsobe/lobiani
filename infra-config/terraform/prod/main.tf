terraform {
  backend "remote" {
    organization = "lobiani"

    workspaces {
      name = "lobiani-prod-fra1"
    }
  }
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "~> 2.3.0"
    }
  }
}

module "platform" {
  source = "../modules/platform"
  env = "prod"
}

module "dns" {
  source = "../modules/dns"
  address = module.platform.lb_ip
}

output "cluster_endpoint" {
  value = module.platform.cluster_endpoint
}

data "digitalocean_domain" "default" {
  name = "baybay.dev"
}

resource "digitalocean_record" "argocd-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = "argocd"
  value = module.platform.lb_ip
}

resource "digitalocean_record" "argocd-grpc-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = "grpc.argocd"
  value = module.platform.lb_ip
}

resource "digitalocean_record" "moon-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = "moon"
  value = module.platform.lb_ip
}

resource "digitalocean_kubernetes_node_pool" "cicd-tools-pool" {
  cluster_id = module.platform.cluster_id

  name = "cicd-tools-pool"
  size = "s-1vcpu-2gb"
  auto_scale = true
  min_nodes = 1
  max_nodes = 2

  labels = {
    node-type = "cicd-tools-pool"
  }
}
