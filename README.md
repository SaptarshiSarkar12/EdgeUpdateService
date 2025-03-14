# EdgeUpdateService (EUS)

## Overview

EdgeUpdateService (EUS) is a service that provides a way to update the Edge browser automatically to the latest version, in linux.
Currently, it works only for beta versions of Edge (installed via debian package). It is tested to work in Ubuntu amd64.

> [!WARNING]
> This service does not ask you to accept the terms and conditions of Microsoft Edge. It is your responsibility to accept the terms and conditions.
> It only upgrades the Edge browser to the latest version and requires you to have Edge installed on your machine.

## Installation

1. Download the `eus` executable from the [release page](https://github.com/SaptarshiSarkar12/EdgeUpdateService/releases/latest) and put the binary in `/usr/bin` directory.
2. Run this command with `sudo` privileges: `eus -gs` to install and enable the EUS service file.
3. EUS service will be enabled and started automatically at startup time when the network connection starts up.

## Usage

Available commands:
- `eus [ --help | -h ]`: Display help.
- `eus [ --version | -v ]`: Display version.
- `eus [ --uninstall-eus | -u ]`: Uninstall everything related to EUS excluding the binary which you need to remove manually. This command requires admin permissions (`sudo`).
- `eus [ --generate-service | -gs ]`: Installs and enables the EUS service. This command requires admin permissions (`sudo`).
- `eus` (with no arguments): Check for Edge updates and install if available. This command also require admin permissions (`sudo`).

For EUS service, below are some examples how
- To manually start the service, run `sudo systemctl start edge-update.service`.
- To stop the service, run `sudo systemctl stop edge-update.service`.
- To disable the service, run `sudo systemctl disable edge-update.service`.
- To enable the service, run `sudo systemctl enable edge-update.service`.

## Troubleshooting

- If the service is not working, check the logs by running `sudo journalctl -u edge-update.service`.
- If the service is not starting, check the status by running `sudo systemctl status edge-update.service`.
- If the service is not updating the Edge browser, check the logs by running `sudo journalctl -xeu edge-update.service`.

## Uninstallation

1. Run this command with `sudo` privileges: `eus -u` to disable and remove the EUS service file.
2. Remove the binary from `/usr/bin` directory.

## Tech Stack

- [JDK 23](https://www.oracle.com/java/technologies/downloads/#java23)
- [Oracle GraalVM for JDK 23](https://www.graalvm.org/)

## Building from source

> [!NOTE]
> You need to have JDK 23 and GraalVM installed on your machine to build the project.
> You must set `GRAALVM_HOME` environment variable to the path of GraalVM.

1. Clone the repository.
2. Run `mvn clean install` to build the project.
3. Run `mvn -P build-binary package` to build the binary. A binary named `eus` will be created in the `target` directory.
4. Follow the [installation](#installation) steps to install the binary.

> [!TIP]
> If you have changed the source code and want to build a new binary, it is recommended to run `mvn -P generate-graalvm-metadata exec:exec@java-agent` before building the binary.
> This command will generate the GraalVM metadata required for the binary.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) before submitting a pull request.
