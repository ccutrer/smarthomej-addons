# Smarthome/J Add-ons

[![EPL-2.0](https://img.shields.io/badge/license-EPL%202-green.svg)](https://opensource.org/licenses/EPL-2.0)
[![Build Status](https://github.com/smarthomej/addons/actions/workflows/ci-build.yml/badge.svg?branch=3.2.x)](https://github.com/smarthomej/addons/actions/workflows/ci-build.yml)

This repository contains the add-ons that are implemented on top of [openHAB Core APIs](https://github.com/openhab/openhab-core).
Some parts of this repository are forked from [openHAB Addons](https://github.com/openhab/openhab-addons).

## Installation / Usage

The easiest way to use the bindings in this repository with openHAB 3.0.x/3.1.x is to install the SmartHome/J repository manager.
The documentation can be found [here](https://github.com/smarthomej/addons/tree/main/bundles/org.smarthomej.io.repomanager).

For openHAB 3.2.0 and later it is recommended to add `https://download.smarthomej.org/addons.json` as JSON 3rd Party Addon Service in the settings.
Afterwards all addons can be installed from the UI.

If you installed SmartHome/J bindings from Community Marketplace (NOT the "JSON 3rd Party Addon Service" mentioned above!), you need to manually remove the installed addons.
Go to your `userdata` folder (e.g. `/var/lib/openhab` on Debian systems) and remove the marketplace directory.
*Attention:* This will also uninstall all other bindings installed from the marketplace, make sure you install everything you need afterwards.

### Compatibility

Due to a breaking changes in openHAB, older and newer versions of bundles are not compatible in every combination.
We'll continue to support 3.0 compatible addons until the release of openHAB 3.2.0 (which is expected end of 2021) and provide the same set of addons for newer versions.
Please check the table to see which versions of SmartHome/J are compatible with which openHAB version:

| | openHAB 3.0.x (releases) | openHAB 3.1.x (releases) | openHAB 3.2.0 (release) | openHAB 3.3.0 (snapshots) |
|---|:---:|:---:|:---:|:---:|
| SmartHome/J 3.1.x (snapshots, releases) | yes | no | no | no |
| SmartHome/J 3.2.x (snapshots, releases) | no | yes | yes| yes |
| compatible RepoManager | [latest 3.1.x](https://download.smarthomej.org/repomanager-latest) | [latest 3.2.x](https://download.smarthomej.org/repomanager-latest-3.2.x) | JSON AddonService | JSON AddonService | 

### Upgrading your openHAB system from 3.0.x/3.1.x to 3.2.x

While openHAB is running (this is important, otherwise caching might be an issue!):

- uninstall all SmartHome/J Bindings
- delete the RepoManager jar from your addons folder (if present)
- delete the .kar with all addons from the addons folder (if present)

Stop openHAB, perform the upgrade and follow the installation instructions for openHAB 3.2.x above.

### Upgrading SmartHome/J bindings installed from the JSON 3rd Party Add-on service

The newest version (and only that !) is available for installation in the UIs Add-ons section.
You'll always install the newest version, but installed addons keep their version.

Unfortunately there is no automatic or half-automatic update process.
If you want to upgrade after a new version is released, you have to manually uninstall and re-install the binding.
Your configurations (binding-configurations and thing configurations) are safe and will be picked up by the new version.
Things will automatically update their type/definition where necessary.

*Attention:* Even though we try to reduce breaking changes to an absolute minimum, please always look at the release notes prior to updating.

## Development

SmartHome/J add-ons are [Java](https://en.wikipedia.org/wiki/Java_(programming_language)) `.jar` and [Apache Karaf](https://karaf.apache.org) `.kar` files.
Regarding development, code-style and alike the same rules and tooling that apply (or are used) within openHAB are also used.

The following differences apply:

- null checks: some warnings have been increased to error level
- null checks: annotations are required 

Happy coding! 
Pull requests always welcome, and we'll try to review as soon as possible.
In case you need assistance, feel free to ask.
