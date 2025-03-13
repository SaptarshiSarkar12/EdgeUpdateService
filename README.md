# EdgeUpdateService (EUS)

## Overview

EdgeUpdateService (EUS) is a service that provides a way to update the Edge browser automatically to the latest version, in linux.
Currently, it works only for beta versions of Edge (installed via debian package). It is tested to work in Ubuntu amd64.

> [!WARNING]
> This service does not ask you to accept the terms and conditions of Microsoft Edge. It is your responsibility to accept the terms and conditions.
> It only upgrades the Edge browser to the latest version and requires you to have Edge installed on your machine.

## Installation

1. Download the latest release from the release page and put the binary in `/usr/bin` directory.
2. Run this command with `sudo` privileges: `eus -gs` to generate and enable the EUS service file.
3. EUS service will be enabled and started automatically at startup time when the network connection starts up.

## Usage

- To manually start the service, run `sudo systemctl start edge-update.service`.
- To stop the service, run `sudo systemctl stop edge-update.service`.
- To disable the service, run `sudo systemctl disable edge-update.service`.
- To enable the service, run `sudo systemctl enable edge-update.service`.

## Troubleshooting

- If the service is not working, check the logs by running `sudo journalctl -u edge-update.service`.
- If the service is not starting, check the status by running `sudo systemctl status edge-update.service`.
- If the service is not updating the Edge browser, check the logs by running `sudo journalctl -xeu edge-update.service`.

## Uninstallation

1. Run this command with `sudo` privileges: `eus -us` to disable and remove the EUS service file.
2. Remove the binary from `/usr/bin` directory.
