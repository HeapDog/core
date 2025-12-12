# ============================================================================
# infra/modules/compute/main.tf
# ============================================================================

data "google_compute_image" "ubuntu" {
  family  = "ubuntu-2404-lts-amd64"
  project = "ubuntu-os-cloud"
}

resource "google_compute_address" "static" {
  name   = "${var.instance_name}-ip"
  region = var.region
}

resource "google_compute_instance" "vm" {
  name         = var.instance_name
  machine_type = var.machine_type
  zone         = var.zone

  scheduling {
    preemptible                 = true
    automatic_restart           = false
    provisioning_model          = "SPOT"
    instance_termination_action = "STOP"
  }

  boot_disk {
    initialize_params {
      image = data.google_compute_image.ubuntu.self_link
      size  = var.disk_size_gb
      type  = "pd-standard"
    }
  }

  network_interface {
    network    = var.network_self_link
    subnetwork = var.subnet_self_link

    access_config {
      nat_ip = google_compute_address.static.address
    }
  }

  tags = ["http-server", "https-server", "ssh-server"]

  metadata = {
    enable-oslogin = "TRUE"
  }

  lifecycle {
    ignore_changes = [boot_disk[0].initialize_params[0].image]
  }
}