# ============================================================================
# infra/modules/compute/outputs.tf
# ============================================================================

output "instance_name" {
  value = google_compute_instance.vm.name
}

output "instance_id" {
  value = google_compute_instance.vm.id
}

output "external_ip" {
  value = google_compute_address.static.address
}

output "internal_ip" {
  value = google_compute_instance.vm.network_interface[0].network_ip
}