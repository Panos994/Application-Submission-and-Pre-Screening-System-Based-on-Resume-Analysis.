Vagrant.configure("2") do |config|
  # Use Ubuntu as the base box
  config.vm.box = "ubuntu/bionic64"

  # Set up hostname
  config.vm.hostname = "docker-test"
  config.vm.boot_timeout = 600


  # Network configuration for VM
  config.vm.network "private_network", type: "dhcp"

  # Ansible provisioning configuration
  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "deploy.yaml"
    ansible.verbose = "v"
  end

  # Forward ports for access to Dockerized services
  config.vm.network "forwarded_port", guest: 8080, host: 8080  # Frontend (Vue)
  config.vm.network "forwarded_port", guest: 8081, host: 8081  # Nginx
  config.vm.network "forwarded_port", guest: 8082, host: 8082  # Custom Frontend Port if needed
  config.vm.network "forwarded_port", guest: 9090, host: 9090  # Spring Boot
  config.vm.network "forwarded_port", guest: 5432, host: 5432  # PostgreSQL
  config.vm.network "forwarded_port", guest: 9000, host: 9000  # MinIO
  config.vm.network "forwarded_port", guest: 9001, host: 9001  # MinIO Console

  # Define VM resources
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "4096"  # Adjust if more RAM is needed
    vb.cpus = 2         # Adjust CPU as needed
    vb.customize ["modifyvm", :id, "--audio", "none"]
  end
end
