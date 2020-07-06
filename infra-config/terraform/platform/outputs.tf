output "cluster_endpoint" {
  value = digitalocean_kubernetes_cluster.cluster.endpoint
}

output "lb_ip" {
  value = module.nginx-ingress-controller.lb_address
}
