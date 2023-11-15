[legacyfabric]: https://legacyfabric.net

# Bismuth

Work in progress Minecraft client for 1.8.9. Built for the [Legacy Fabric][legacyfabric] mod loader.

(TODO: Screenshots)

## Contents

- [Features](#features)
- [Planned Features](#planned-features)
- [Known Issues](#known-issues)

- [Contributing](#contributing)
  - [Building](#building)
  - [Development](#development)
    - [IntelliJ IDEA](#intellij-idea)
  - [Pull Request Guidelines](#pull-request-guidelines)

## Features

- Account Switcher (WIP - waiting for API access).
- Modifying the game title.
- Camera tweak options.
  - Disable view bobbing for the camera/hand separately.
  - Minimal view bobbing reduces the amount of bobbing.
  - Disable hurt cam.
- Hide the crosshair in third person.
- Fullbright toggle.
- Keybind to stop rendering entities.
- Screenshot manager to copy/delete/open after they are taken.
- Scrollable item tooltips when hovering.
- Cleaning up the title screen.
  - Removing the splash text.
  - Removing the broken Realms button.
  - Removing the language change button.
- Disabling the Snooper (telemetry).
  - Originally, I was unaware of the option to disable it in vanilla.
    However, I have kept this feature in the client as it returns
    all calls to the Snooper to prevent any data being sent.
    This is enabled by default.
- Changing the startup logo.
  - This can be done with resource packs, however the plan is to make
    it easier for users to simply choose an image from their computer
    without having to make a resource pack.
- Zoom key (defaults to C).
  - Modifies the FOV (temporarily) to make the camera appear to be closer.
- Multiplayer direct connect status pinger.
  - Shows the status of the server IP entered in the Direct Connect multiplayer menu.
- Customizable Discord presence (currently through language files, very limited).

## Planned Features

- Prevent rendering of certain particles and particle multipliers.
- Customizable chat box - resizable, movable, custom chat tabs, etc.
- Raw mouse input - attempts to reduce the time it takes to retrieve mouse movement.
- AutoGG, AutoTip (Hypixel), etc.
- Cloud height and removal.
- Removable fog.
- User interface to change settings.
- Preventing chunks from unloading if the old render distance is less than the new distance.
- Showing capes (and maybe more) from other clients (you still need to buy them from the original client).
- Better keybind system (including fixing perspective key when bound to a mouse button).

## Known Issues

- Scrollable tooltips prevent a lot of mouse clicks from being registered.

## Reporting Issues

When discovering issues with Bismuth, it is recommended to check the [Known Issues](#known-issues) section
Once you are confident that the issue is not already known, you can open an issue on GitHub.

Please also make sure that you have the latest version of Bismuth installed. Also make sure to try
with default configurations.

When opening an issue, please make sure to include as much information as possible.
This includes your crash report, Bismuth version, which launcher you are using, and any other mods
that you have installed.

In your reproduction steps, you should provide a step-by-step guide on how to reproduce the issue.
This includes any settings that you have changed from the default. If you are able to reproduce the issue with a fresh
installation of Bismuth, please mention that.

## Contributing

Contributions to Bismuth are welcome and highly appreciated!

If you don't have any ideas in mind, you can check out the [Known Issues](#known-issues) or
the [Planned Features](#planned-features) sections.
Please make sure to check for any pull requests that may already be open and that mention
those issues/features before opening a new one.

Once you have found something to contribute on, it is recommended to open a Pull Request early.
You can mark the PR as a draft. This will prevent duplicate PRs for the same issue/feature.

### Building

Building the project is as simple as running `./gradlew build` in the root directory of the project.
Gradle will automatically download all the required dependencies and build the project.

### Development

To start a development client, you can use `./gradlew runClient` in the root directory of the project.
This will start a Minecraft client with the Bismuth mod installed. The `.minecraft` directory for this
Minecraft client is located in the `run` folder.

#### IntelliJ IDEA

It is recommended that you use IntelliJ IDEA for development.

Please also make sure to prevent formatting the language files for Bismuth. You can
read more about this in `src/main/resources/assets/bismuth/lang/en_us.json` file.

### Pull Request Guidelines

When you create a PR, GitHub should give you a template to follow. Please fill out the template as best as you can.
If you are unsure about something, you can leave it blank or ask for help in the PR comments.

Please make sure to list all of the changes that you have made in the PR description.
If you have made changes to the UI, please include screenshots of the changes.

If you are adding a new feature, please make sure to include a description of the feature and how it works.
