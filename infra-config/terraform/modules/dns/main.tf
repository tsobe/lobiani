terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "~> 2.3.0"
    }
  }
}

data "digitalocean_domain" "default" {
  name = var.domain
}

resource "digitalocean_record" "lobiani-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = format("%s%s", var.subdomain-prefix, "lobiani")
  value = var.address
}

resource "digitalocean_record" "admin-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = format("%s%s", var.subdomain-prefix, "admin.lobiani")
  value = var.address
}

resource "digitalocean_record" "axonserver-gui-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = format("%s%s", var.subdomain-prefix, "axonserver-gui.lobiani")
  value = var.address
}

resource "digitalocean_record" "dashboard-domain" {
  domain = data.digitalocean_domain.default.name
  type = "A"
  name = format("%s%s", var.subdomain-prefix, "dashboard.lobiani")
  value = var.address
}
