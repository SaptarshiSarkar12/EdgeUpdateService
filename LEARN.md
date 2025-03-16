# Rise of _EdgeUpdateService_

## The Story

I am Saptarshi, an Ubuntu (AMD64) and Microsoft Edge (Beta) user. I use Edge for browsing, watching videos, and reading articles. I have been using Edge for a long time now.
Microsoft does not provide any support for auto-updating linux deb packages. As I am a student, updating edge manually takes too much time. I have to download the deb package from the official website, install it, and then remove the old version. This is a very time-consuming process. I wanted to automate this process. So, I decided to create a systemd-service that will automatically check and update Edge Beta on my Ubuntu system.

## The Plan

I will create a Java program that will check for any available updates for Edge Beta. If an update is available, it will download the deb package and install it using `apt`.
A systemd-service will be generated, installed, and enabled on the system, on the choice of the user (by running the program with the `--generate-service` flag). The service will run the Java program once every time the user logs in.

## The Implementation

I will use Java to create the program. The program will use the Java network class to check for updates and `ProcessBuilder` for running shell commands.
The current Edge (currently only for `Beta` channel) version can be retrieved by executing the command `microsoft-edge-beta --version`.
The program will use the `wget` command to download the deb package and `apt` (with `dpkg`) to install the package.
The systemd-service will be generated in `/etc/systemd/system/` (so that it can run with `sudo` privileges) and will be enabled using `systemctl`.

## The Outcome

When the user logs in, the service will run the Java program. The program will check for updates and install them if available. The user will not have to worry about updating Edge Beta manually.
Edge gets updated automatically even if the browser is open. The user can continue browsing without any interruptions. It has been tested successfully in Ubuntu 24.10.
For one time update, the user need not run with `--generate-service` flag. The program will update Edge Beta and exit.

## 🔮 The Future

In the future, I plan to add support for other Edge channels (Stable, Dev, and Canary). I will also add a feature to check for updates only once every day irrespective of the number of logins. This will reduce the number of times the program runs and checks for updates.
Contributions are welcome. Feel free to fork the repository and create a pull request. Raise an issue if you find any bugs or have any suggestions.
Remember to adhere to the [Code of Conduct](CODE_OF_CONDUCT.md) and the [Contributing Guidelines](CONTRIBUTING.md).

## 🙌 Final Words

I hope you find this project useful. I will continue to maintain and improve it. Thank you for reading. Have a great day!