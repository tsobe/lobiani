output "cluster_endpoint" {
  value = digitalocean_kubernetes_cluster.cluster.endpoint
}

output "cluster_token" {
  value = digitalocean_kubernetes_cluster.cluster.kube_config.0.token
}

output "cluster_ca_certificate" {
  value = digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate
}

data digitalocean_loadbalancer "lb" {
  name = format("%s-lb-v2", digitalocean_kubernetes_cluster.cluster.name)
  depends_on = [helm_release.ingerss-nginx-v2]
}

output "lb_ip" {
  value = data.digitalocean_loadbalancer.lb.ip
}

output "cluster_id" {
  value = digitalocean_kubernetes_cluster.cluster.id
}
