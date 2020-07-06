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
