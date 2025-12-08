# ============================================================================
# infra/envs/dev/main.tf
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

  network_name = "heapdog-dev-vpc"
  region       = var.region
  subnet_cidr  = "10.0.1.0/24"
}

module "heapdog_dev_instance" {
  source = "../../modules/compute"

  instance_name     = "heapdog-dev"
  machine_type      = "e2-standard-4"
  zone              = var.zone
  region            = var.region
  disk_size_gb      = 20
  network_self_link = module.network.network_self_link
  subnet_self_link  = module.network.subnet_self_link
}
