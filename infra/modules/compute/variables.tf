# ============================================================================
# infra/modules/compute/variables.tf
# ============================================================================

variable "instance_name" {
  description = "Name of the compute instance"
  type        = string
}

variable "machine_type" {
  description = "GCP machine type"
  type        = string
}

variable "zone" {
  description = "GCP zone"
  type        = string
}

variable "region" {
  description = "GCP region"
  type        = string
}

variable "disk_size_gb" {
  description = "Boot disk size in GB"
  type        = number
}

variable "network_self_link" {
  description = "Self link of the VPC network"
  type        = string
}

variable "subnet_self_link" {
  description = "Self link of the subnet"
  type        = string
}