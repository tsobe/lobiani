variable "do_token" {}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_vpc" "test-fra2" {
  name     = "test-fra2"
  region   = "fra1"
  ip_range = "10.115.0.0/20"
}
