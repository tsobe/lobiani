variable "do_token" {}

module "platform" {
  source = "../platform"
  do_token = var.do_token
  env = "test"
}

module "dns" {
  source = "../dns"
  address = module.platform.lb_ip
  subdomain-prefix = "test-"
}

output "cluster_endpoint" {
  value = module.platform.cluster_endpoint
}

module "argocd_regcluster" {
  source = "../argocd_regcluster"
  argocd_k8s_cluster_name = "lobiani-production"
  new_cluster_name = "test-lobiani"
  new_cluster_token = module.platform.cluster_token
  new_cluster_endpoint = module.platform.cluster_endpoint
  new_cluster_ca_certificate = module.platform.cluster_ca_certificate
}
