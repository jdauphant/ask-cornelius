# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "ubuntu/trusty64"
  config.vm.network :forwarded_port, host: 8080, guest: 80
  config.vm.network :forwarded_port, host: 9000, guest: 9000

  config.vm.provision :ansible do |ansible|
        ansible.playbook = "provisioning/installation.yml"
        ansible.sudo = true
  end
 
  config.vm.provider "virtualbox" do |v|
      v.memory = 1280
      v.cpus = 2
  end
end
