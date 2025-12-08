# ============================================================================
# infra/envs/prod/variables.tf
# ============================================================================

variable "project_id" {
  description = "basic-formula-477522-r1"
  type        = string
}

variable "region" {
  description = "GCP region"
  type        = string
  default     = "northamerica-northeast1"
}

variable "zone" {
  description = "GCP zone"
  type        = string
  default     = "northamerica-northeast1-a"
}