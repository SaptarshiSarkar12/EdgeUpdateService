# EdgeUpdateService (EUS)

<p align="center">
    <a href="https://github.com/SaptarshiSarkar12/EdgeUpdateService/releases/latest/"><img src="https://img.shields.io/github/v/release/SaptarshiSarkar12/EdgeUpdateService?color=%23FFFF0g&amp;label=EdgeUpdateService" alt="Release Version"></a>
    <a href="https://github.com/SaptarshiSarkar12/EdgeUpdateService/blob/master/LICENSE"><img src="https://img.shields.io/github/license/SaptarshiSarkar12/EdgeUpdateService" alt="License"></a>
    <a href="https://github.com/SaptarshiSarkar12/EdgeUpdateService/releases/latest/"><img src="https://img.shields.io/github/downloads/SaptarshiSarkar12/EdgeUpdateService/total" alt="Total No. Of Downloads of EdgeUpdateService"></a>
</p>
<p align="center">
    <a href="https://github.com/SaptarshiSarkar12/EdgeUpdateService/stargazers"><img src="https://img.shields.io/github/stars/SaptarshiSarkar12/EdgeUpdateService?label=Leave%20a%20star&amp;style=social" alt="GitHub Stargazers for EdgeUpdateService"></a> 
    <a href="https://twitter.com/SSarkar2007"><img src="https://img.shields.io/twitter/follow/SSarkar2007?style=social" alt="Follow us on Twitter"></a> 
    <a href="https://discord.gg/DeT4jXPfkG"><img src="https://img.shields.io/discord/1034035416300519454?label=Discord&amp;logo=discord" alt="Discord Server"></a>
</p>

---

## Overview

**EdgeUpdateService (EUS)** is a robust, Java-based program designed to **automate the update process for Microsoft Edge on Linux**. Instead of manually checking and installing updates, EUS ensures your Edge browser is always up to date by seamlessly integrating with your system’s startup routine. Tested on Ubuntu amd64, it leverages systemd to start automatically with your network connection, so updates occur without any manual intervention.

> [!NOTE]
> Currently, EUS supports updates for **_only the Beta channel_** of Microsoft Edge. Contributions are welcome to **extend support to other channels**.

### Why Use EUS?

- **Automated Efficiency:** Say goodbye to manual updates. EUS checks for the latest releases and automatically upgrades your Edge browser once per login, ensuring you always have the most current version without any extra effort.
- **Seamless Integration:** Running as a native systemd service, EUS fits effortlessly into your environment, ensuring reliable operation right from system boot.
- **Transparent Operation:** With clear status outputs and detailed logs, you can monitor the update process using standard system tools like `systemctl` and `journalctl`.
- **Minimal Configuration:** Designed with simplicity in mind, EUS requires only a few steps to install and enable, freeing you to focus on your work without worrying about browser updates.

> [!NOTE]
> This service does not manage the acceptance of the terms and conditions of Microsoft Edge. It solely automates the update process; users remain responsible for the initial acceptance of Edge’s terms.

## Demo

![EUS Demo](https://github.com/user-attachments/assets/615906fc-f0c3-42e1-8948-dc4257b20b82)

## Installation

1. Download the `eus` executable from the [release page](https://github.com/SaptarshiSarkar12/EdgeUpdateService/releases/latest) and put the binary in `/usr/bin` directory.
2. Run this command with elevated privileges: `sudo eus -gs` to install and enable the EUS service file.
   ![Generate Service](https://github.com/user-attachments/assets/2b3d32c0-55c1-461c-bf39-1e56ce1f8f5c)
3. EUS service will be enabled and started automatically at startup time when the network connection starts up.

## Usage

Available commands:
- `eus [ --help | -h ]`: Display help.
  ![Help Command](https://github.com/user-attachments/assets/9319ecb1-ef33-4114-be54-00b4ba6f70ae)
- `eus [ --version | -v ]`: Display version.
- `eus [ --uninstall-eus | -u ]`: Uninstall everything related to EUS excluding the binary which you need to remove manually. This command requires admin permissions (`sudo`).
- `eus [ --generate-service | -gs ]`: Installs and enables the EUS service. This command requires admin permissions (`sudo`).
- `eus` (with no arguments): Check for Edge updates and install if available. This command also requires admin permissions (`sudo`).

For EUS service, below are some examples of how
- To manually start the service, run `sudo systemctl start edge-update.service`.
- To stop the service, run `sudo systemctl stop edge-update.service`.
- To disable the service, run `sudo systemctl disable edge-update.service`.
- To enable the service, run `sudo systemctl enable edge-update.service`.

## Troubleshooting

- If the service is not working, check the logs by running `sudo journalctl -u edge-update.service`.
  ![Journalctl Logs](https://github.com/user-attachments/assets/5951d7a9-4737-41da-bb65-edea36f55633)
- If the service is not starting, check the status by running `sudo systemctl status edge-update.service`.
  ![Systemctl Status](https://github.com/user-attachments/assets/ef64f1d7-a369-4926-bbd5-d9bd69b96150)
- If the service is not updating the Edge browser, check the logs by running `sudo journalctl -xeu edge-update.service`.
  ![Journalctl Error Logs](https://github.com/user-attachments/assets/96c49b77-d0e9-41f7-aaa8-0e80e287aa8c)

## Uninstallation

1. Run this command with `sudo` privileges: `eus -u` to disable and remove the EUS service file.
   ![Uninstall EUS](https://github.com/user-attachments/assets/7ea9df3c-940e-4657-802c-e6c7b6bec41b)
2. Remove the binary from `/usr/bin` directory.

## Tech Stack

The project is built using the following technologies:
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
