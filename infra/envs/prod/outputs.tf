# ============================================================================
# infra/envs/prod/outputs.tf
# ============================================================================

output "prod_instance_external_ip" {
  value       = module.heapdog_prod_instance.external_ip
  description = "External IP of heapdog-prod instance"
}

output "prod_instance_internal_ip" {
  value       = module.heapdog_prod_instance.internal_ip
  description = "Internal IP of heapdog-prod instance"
}

output "network_name" {
  value = module.network.network_name
}

output "storage_bucket_id" {
  value = module.storage.bucket_id
}

output "storage_bucket_arn" {
  value = module.storage.bucket_arn
}
