.PHONY: up down build logs exec attach help

# Default environment
env ?= prod
# Default skip_build (empty)
skip_build ?=

# Select Compose file based on environment
ifeq ($(env),prod)
	COMPOSE_FILE := compose.prod.yml
else ifeq ($(env),dev)
	COMPOSE_FILE := compose.dev.yml
else
	$(error Invalid env: $(env). Use 'prod' or 'dev')
endif

# Command to run docker compose
DC := docker compose -f $(COMPOSE_FILE)

# Extract arguments (services) from command line
# Filter out known targets and variable assignments
KNOWN_TARGETS := up down build logs exec attach help
ARGS := $(filter-out $(KNOWN_TARGETS) %=%,$(MAKECMDGOALS))

# Create do-nothing targets for arguments so Make doesn't complain
$(eval $(ARGS):;@:)

# Helper to get all services from the compose file
# Only run shell command if ARGS is empty (for performance)
ALL_SERVICES_CMD = $(DC) config --services 2>/dev/null
TARGET_SERVICES := $(if $(ARGS),$(ARGS),$(shell $(ALL_SERVICES_CMD)))

# Process skip_build list (comma-separated to space-separated)
COMMA := ,
EMPTY :=
SPACE := $(EMPTY) $(EMPTY)
SKIP_BUILD_LIST := $(subst $(COMMA),$(SPACE),$(skip_build))

# Determine services to build (TARGET_SERVICES - SKIP_BUILD_LIST)
BUILD_SERVICES = $(filter-out $(SKIP_BUILD_LIST),$(TARGET_SERVICES))

up:
	@# 1. Validation: Check if skipped services are in the target list
	@for skip in $(SKIP_BUILD_LIST); do \
		found=0; \
		for target in $(TARGET_SERVICES); do \
			if [ "$$skip" = "$$target" ]; then found=1; break; fi; \
		done; \
		if [ $$found -eq 0 ]; then \
			echo "Error: Service '$$skip' in skip_build is not in the target services list: $(TARGET_SERVICES)"; \
			exit 1; \
		fi; \
	done
	@echo "Environment: $(env)"
	@echo "Target services: $(TARGET_SERVICES)"
	@if [ -n "$(SKIP_BUILD_LIST)" ]; then echo "Skipping build for: $(SKIP_BUILD_LIST)"; fi
	@# 2. Build services (unless skipped)
	@if [ -n "$(strip $(BUILD_SERVICES))" ]; then \
		echo "Building: $(BUILD_SERVICES)"; \
		$(DC) build $(BUILD_SERVICES); \
	fi
	@# 3. Run up
	@echo "Starting services..."
	$(DC) up -d $(TARGET_SERVICES)

down:
	@if [ -z "$(ARGS)" ]; then \
		echo "Stopping and removing all services in $(env)..."; \
		$(DC) down; \
	else \
		echo "Stopping and removing services: $(ARGS)..."; \
		$(DC) rm -s -f -v $(ARGS); \
	fi

build:
	@echo "Building services for $(env)..."
	$(DC) build $(TARGET_SERVICES)

logs:
	@# Default to follow=true unless follow=false is passed
	$(eval FOLLOW_FLAG := $(if $(filter false,$(follow)),,-f))
	@if [ -z "$(ARGS)" ]; then \
		echo "Showing logs for all services in $(env)..."; \
		$(DC) logs $(FOLLOW_FLAG); \
	else \
		echo "Showing logs for $(ARGS)..."; \
		$(DC) logs $(FOLLOW_FLAG) $(ARGS); \
	fi

attach:
	@if [ -z "$(ARGS)" ]; then \
		echo "Error: Please specify a service to attach to."; \
		exit 1; \
	fi
	@# We need to find the container ID
	$(eval CONTAINER_ID := $(shell $(DC) ps -q $(firstword $(ARGS)) | head -n 1))
	@if [ -z "$(CONTAINER_ID)" ]; then \
		echo "Error: Container for service '$(firstword $(ARGS))' is not running."; \
		exit 1; \
	fi
	docker attach $(CONTAINER_ID)

exec:
	@if [ -z "$(ARGS)" ]; then \
		echo "Error: Please specify a service. Usage: make exec <service> [cmd=<command>]"; \
		exit 1; \
	fi
	$(DC) exec $(ARGS) $(if $(cmd),$(cmd),/bin/sh)

help:
	@echo "Usage:"
	@echo "  make up [service...] [env=prod|dev] [skip_build=s1,s2]  - Build and start services"
	@echo "  make down [service...] [env=prod|dev]                  - Stop and remove services"
	@echo "  make build [service...] [env=prod|dev]                 - Build services"
	@echo "  make logs [service...] [env=prod|dev] [follow=false]   - Show/Follow logs"
	@echo "  make attach <service> [env=prod|dev]                   - Attach to service container"
	@echo "  make exec <service> [cmd=...] [env=prod|dev]           - Execute command in container"
