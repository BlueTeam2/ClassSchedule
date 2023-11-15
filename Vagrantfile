STAGE_RAM_MB = "2048"
STAGE_CPU_CNT = "2"

PROD_RAM_MB = "2048"
PROD_CPU_CNT = "2"

VM_PROVIDER = "vmware_desktop" 

DOCKER_FILES = "/home/vagrant"
POSTGRES_ENTRYPOINT_DIR = "#{DOCKER_FILES}/postgres_entrypoint"

unless Vagrant.has_plugin?("vagrant-docker-compose")
  system("vagrant plugin install vagrant-docker-compose")
  puts "Dependencies installed, please try the command again."
  exit
end

$app_test_results = <<-'SCRIPT'
test_exit_code=$(docker wait schedule-app-test)
if [ $test_exit_code -eq 0 ]; then
  echo "All tests have been passed successfully!"
else
  echo "Some tests failed. Container logs:"
  docker logs schedule-app-test
fi
SCRIPT

$app_running_msg = <<-SCRIPT
source .env
echo "Application is avaliable at http://$(hostname -I | cut -d' ' -f1):$SCHEDULE_APP_PORT/"
SCRIPT

$docker_compose_up = <<-SCRIPT
docker compose -f docker-compose-deps.yml -f docker-compose.yml up -d
SCRIPT

$append_postgres_entrypoint = <<-SCRIPT
echo "POSTGRES_ENTRYPOINT_DIR=\"#{POSTGRES_ENTRYPOINT_DIR}\"" >> .env
SCRIPT

$apply_dos2unix = <<-'SCRIPT'
sudo apt-get install dos2unix
find . -type f -exec dos2unix '{}' \;
SCRIPT


Vagrant.configure("2") do |config|

  config.vm.define "stage-compose", autostart: false do |stage|
    stage.vm.box = "generic/ubuntu2204"
    stage.vm.box_download_options = {"ssl-revoke-best-effort" => true}

    stage.vm.provider VM_PROVIDER do |vm_provider|
      vm_provider.memory = STAGE_RAM_MB
      vm_provider.cpus = STAGE_CPU_CNT
    end

    stage.vm.provision "UL_STAGE_COMPOSE_DEPS", type: "file", source: "docker-compose-deps.yml", destination: "#{DOCKER_FILES}/docker-compose-deps.yml"
    stage.vm.provision "UL_STAGE_COMPOSE", type: "file", source: "docker-compose-stage.yml", destination: "#{DOCKER_FILES}/docker-compose.yml"
    stage.vm.provision "UL_STAGE_ENV", type: "file", source: ".env.stage", destination: "#{DOCKER_FILES}/.env"
    stage.vm.provision "UL_INIT_DUMP", type: "file", source: "./backup/initial_data.dump", destination: "#{POSTGRES_ENTRYPOINT_DIR}/initial_data.dump"
    stage.vm.provision "UL_INIT_SCRIPT", type: "file", source: "./scripts/init_db.sh", destination: "#{POSTGRES_ENTRYPOINT_DIR}/init_db.sh"

    stage.vm.provision :docker

    stage.vm.provision "shell", inline: $apply_dos2unix
    stage.vm.provision "shell", inline: $append_postgres_entrypoint
    stage.vm.provision "shell", inline: $docker_compose_up, run: "always"
    stage.vm.provision "CHECK_APP_TESTS", type: "shell", inline: $app_test_results
    stage.vm.provision "shell", inline: $app_running_msg
  end

  config.vm.define "stage-docker-run", autostart: false do |stage|
    stage.vm.box = "generic/ubuntu2204"
    stage.vm.box_download_options = {"ssl-revoke-best-effort" => true}

    stage.vm.provider VM_PROVIDER do |vm_provider|
      vm_provider.memory = STAGE_RAM_MB
      vm_provider.cpus = STAGE_CPU_CNT
    end

    stage.vm.provision "UL_STAGE_ENV", type: "file", source: ".env.stage", destination: "#{DOCKER_FILES}/.env"
    stage.vm.provision "UL_INIT_DUMP", type: "file", source: "./backup/initial_data.dump", destination: "#{POSTGRES_ENTRYPOINT_DIR}/initial_data.dump"
    stage.vm.provision "UL_INIT_SCRIPT", type: "file", source: "./scripts/init_db.sh", destination: "#{POSTGRES_ENTRYPOINT_DIR}/init_db.sh"

    stage.vm.provision "shell", inline: $apply_dos2unix
    stage.vm.provision "INSTALL_DOCKER", type: :docker
    stage.vm.provision "shell", inline: $append_postgres_entrypoint
    stage.vm.provision "DEPLOY_APP", type: "shell", after: "INSTALL_DOCKER", path: "./scripts/deploy_app.sh", args: ["#{DOCKER_FILES}/.env","run-stage"]
    stage.vm.provision "shell", inline: $app_running_msg
  end

  config.vm.define "prod", autostart: false do |prod|
    prod.vm.box = "generic/ubuntu2204"
    prod.vm.box_download_options = {"ssl-revoke-best-effort" => true}

    prod.vm.provider VM_PROVIDER do |vm_provider|
      vm_provider.memory = PROD_RAM_MB
      vm_provider.cpus = PROD_CPU_CNT
    end
    prod.vm.provision "UL_STAGE_COMPOSE_DEPS", type: "file", source: "docker-compose-deps.yml", destination: "#{DOCKER_FILES}/docker-compose-deps.yml"
    prod.vm.provision "UL_PROD_COMPOSE", type: "file", source: "docker-compose-prod.yml", destination: "#{DOCKER_FILES}/docker-compose.yml"
    prod.vm.provision "UL_PROD_ENV", type: "file", source: ".env.prod", destination: "#{DOCKER_FILES}/.env"
    prod.vm.provision "UL_PROD_INIT_SCRIPT", type: "file", source: "./scripts/init_db.sh", destination: "#{POSTGRES_ENTRYPOINT_DIR}/init_db.sh"

    prod.vm.provision :docker
    prod.vm.provision "shell", inline: $apply_dos2unix
    prod.vm.provision "shell", inline: $append_postgres_entrypoint
    prod.vm.provision "shell", inline: $docker_compose_up, run: "always"
    prod.vm.provision "shell", inline: $app_running_msg
  end

end