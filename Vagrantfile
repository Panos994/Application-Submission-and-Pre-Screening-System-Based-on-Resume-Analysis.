Vagrant.configure("2") do |config|
  # Define the box image (Ubuntu in this case)
  config.vm.box = "ubuntu/bionic64"

  # Define the VM hostname
  config.vm.hostname = "docker-test"

  # Define the VM network
  config.vm.network "private_network", type: "dhcp"

  # Provision the VM using Ansible
  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "deploy.yml"
    ansible.verbose = "v"
  end

  # Forward the ports for frontend and backend
  config.vm.network "forwarded_port", guest: 8080, host: 8083
  config.vm.network "forwarded_port", guest: 9090, host: 9093

  # Define VM resources
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"  # or more, if you have sufficient RAM
    vb.cpus = 2         # Adjust as needed
    vb.customize ["modifyvm", :id, "--audio", "none"]  # Disable audio
  end

end
