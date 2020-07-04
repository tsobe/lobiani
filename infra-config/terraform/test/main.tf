variable "do_token" {}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_vpc" "test-fra1" {
  name     = "test-fra1"
  region   = "fra1"
  ip_range = "10.115.0.0/20"
}
