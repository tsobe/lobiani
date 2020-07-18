output "cluster_endpoint" {
  value = digitalocean_kubernetes_cluster.cluster.endpoint
}

output "cluster_token" {
  value = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
}

output "cluster_ca_certificate" {
  value = digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate
}

output "lb_ip" {
  value = module.nginx-ingress-controller.lb_address
}

output "cluster_id" {
  value = digitalocean_kubernetes_cluster.cluster.id
}
