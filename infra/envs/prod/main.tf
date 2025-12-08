# ============================================================================
# infra/envs/prod/main.tf
# ============================================================================

terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
}

module "network" {
  source = "../../modules/network"

  network_name = "heapdog-prod-vpc"
  region       = var.region
  subnet_cidr  = "10.0.2.0/24"
}

module "heapdog_prod_instance" {
  source = "../../modules/compute"

  instance_name     = "heapdog-prod"
  machine_type      = "e2-standard-4"
  zone              = var.zone
  region            = var.region
  disk_size_gb      = 30
  network_self_link = module.network.network_self_link
  subnet_self_link  = module.network.subnet_self_link
}