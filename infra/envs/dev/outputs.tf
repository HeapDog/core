# ============================================================================
# infra/envs/dev/outputs.tf
# ============================================================================

output "dev_instance_external_ip" {
  value       = module.heapdog_dev_instance.external_ip
  description = "External IP of heapdog-dev instance"
}

output "dev_instance_internal_ip" {
  value       = module.heapdog_dev_instance.internal_ip
  description = "Internal IP of heapdog-dev instance"
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
