# VM configuration
STAGE_RAM_MB = "2048"
STAGE_CPU_CNT = "2"

PROD_RAM_MB = "2048"
PROD_CPU_CNT = "2"

VM_PROVIDER = "vmware_desktop" # Options: "vmware_desktop", "virtualbox"
# Project configuration
DOCKER_FILES = "/home/vagrant"
POSTGRES_ENTRYPOINT_DIR = "#{DOCKER_FILES}/dump"
TEST_CONTAINER_NAME = "schedule-app-test"
SCHEDULE_APP_PORT="8888"
SCHEDULE_APP_IMAGE="arkma/schedule-tomcat-prod:1.0"
SCHEDULE_TEST_APP_IMAGE="arkma/schedule-test:1.0"


unless Vagrant.has_plugin?("vagrant-docker-compose")
  system("vagrant plugin install vagrant-docker-compose")
  puts "Dependencies installed, please try the command again."
  exit
end

# Script determine if tests passed
$app_test_results = <<-'SCRIPT'
test_exit_code=$(docker wait $TEST_CONTAINER_NAME)
if [ $test_exit_code -eq 0 ]; then
  echo "All tests have been passed successfully!"
else
  echo "Some tests failed. Container logs:"
  docker logs $TEST_CONTAINER_NAME
fi
SCRIPT

$app_running_msg = <<-SCRIPT
echo "Application is avaliable at http://$(hostname -I | cut -d' ' -f1):#{SCHEDULE_APP_PORT}/"
SCRIPT


Vagrant.configure("2") do |config|

  config.vm.define "stage-compose", autostart: false do |stage|
    stage.vm.box = "generic/ubuntu2204"
    stage.vm.box_download_options = {"ssl-revoke-best-effort" => true}

    stage.vm.provider VM_PROVIDER do |vmware|
      vmware.memory = STAGE_RAM_MB
      vmware.cpus = STAGE_CPU_CNT
    end
    # File provision
    stage.vm.provision "UL_STAGE_COMPOSE", type: "file", source: "docker-compose-stage.yml", destination: "#{DOCKER_FILES}/docker-compose.yml"
    stage.vm.provision "UL_STAGE_ENV", type: "file", source: ".env.stage", destination: "#{DOCKER_FILES}/.env"
    stage.vm.provision "UL_INIT_DUMP", type: "file", source: "./backup/initial_data.dump", destination: "#{POSTGRES_ENTRYPOINT_DIR}/initial_data.dump"
    stage.vm.provision "UL_INIT_SCRIPT", type: "file", source: "./scripts/init_db.sh", destination: "#{POSTGRES_ENTRYPOINT_DIR}/init_db.sh"

    # Docker provision
    stage.vm.provision :docker
    stage.vm.provision :docker_compose, yml: "#{DOCKER_FILES}/docker-compose.yml", run: "always", env: {
      "SCHEDULE_APP_IMAGE" => SCHEDULE_APP_IMAGE,
      "SCHEDULE_TEST_APP_IMAGE" => SCHEDULE_TEST_APP_IMAGE,
      "POSTGRES_ENTRYPOINT_DIR" => POSTGRES_ENTRYPOINT_DIR,
      "SCHEDULE_APP_PORT" => SCHEDULE_APP_PORT
    }

    # Shell provision
    stage.vm.provision "CHECK_APP_TESTS", type: "shell", inline: $app_test_results, env: {
      "TEST_CONTAINER_NAME" => TEST_CONTAINER_NAME
    }
    stage.vm.provision "shell", inline: $app_running_msg
  end

  config.vm.define "stage-docker-run", autostart: false do |stage|
    stage.vm.box = "generic/ubuntu2204"
    stage.vm.box_download_options = {"ssl-revoke-best-effort" => true}

    stage.vm.provider VM_PROVIDER do |vmware|
      vmware.memory = STAGE_RAM_MB
      vmware.cpus = STAGE_CPU_CNT
    end
    # File provision
    stage.vm.provision "UL_STAGE_ENV", type: "file", source: ".env.stage", destination: "#{DOCKER_FILES}/.env"
    stage.vm.provision "UL_INIT_DUMP", type: "file", source: "./backup/initial_data.dump", destination: "#{POSTGRES_ENTRYPOINT_DIR}/initial_data.dump"
    stage.vm.provision "UL_INIT_SCRIPT", type: "file", source: "./scripts/init_db.sh", destination: "#{POSTGRES_ENTRYPOINT_DIR}/init_db.sh"

    # Docker provision
    stage.vm.provision "INSTALL_DOCKER", type: :docker    
    stage.vm.provision "DEPLOY_APP", type: "shell", after: "INSTALL_DOCKER", path: "./scripts/deploy_app.sh", env: {
      "POSTGRES_ENTRYPOINT_DIR" => POSTGRES_ENTRYPOINT_DIR,
      "ENV_FILE" => "#{DOCKER_FILES}/.env",
      "APP_PORT" => SCHEDULE_APP_PORT,
      "SCHEDULE_APP_IMAGE" => SCHEDULE_APP_IMAGE,
      "SCHEDULE_TEST_APP_IMAGE" => SCHEDULE_TEST_APP_IMAGE
      }, args: ["run"]
    stage.vm.provision "shell", inline: $app_running_msg
  end


  config.vm.define "prod", autostart: false do |prod|
    prod.vm.box = "generic/ubuntu2204"
    prod.vm.box_download_options = {"ssl-revoke-best-effort" => true}

    prod.vm.provider VM_PROVIDER do |vmware|
      vmware.memory = PROD_RAM_MB
      vmware.cpus = PROD_CPU_CNT
    end
    # File provision
    prod.vm.provision "UL_PROD_COMPOSE", type: "file", source: "docker-compose-prod.yml", destination: "#{DOCKER_FILES}/docker-compose.yml"
    prod.vm.provision "UL_PROD_ENV", type: "file", source: ".env.prod", destination: "#{DOCKER_FILES}/.env"

    # Docker provision
    prod.vm.provision :docker
    prod.vm.provision :docker_compose, yml: "#{DOCKER_FILES}/docker-compose.yml", run: "always", env:{
      "SCHEDULE_APP_IMAGE" => SCHEDULE_APP_IMAGE,
      "SCHEDULE_APP_PORT" => SCHEDULE_APP_PORT
    }

    # Shell provision
    prod.vm.provision "shell", inline: $app_running_msg
  end

end