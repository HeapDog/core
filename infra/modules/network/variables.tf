# ============================================================================
# infra/modules/network/variables.tf
# ============================================================================

variable "network_name" {
  description = "Name of the VPC network"
  type        = string
}

variable "region" {
  description = "GCP region"
  type        = string
}

variable "subnet_cidr" {
  description = "CIDR block for subnet"
  type        = string
  default     = "10.0.1.0/24"
}