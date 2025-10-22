# JAVA_HOME SDK Sync

IntelliJ IDEA plugin that synchronizes your Project SDK with the `JAVA_HOME` environment variable.

## Installation

**From JetBrains Marketplace:**
Settings → Plugins → Marketplace → Search "JAVA_HOME SDK Sync" → Install

**Manual:**
Download from [Releases](https://github.com/ludosch/intellij-sdk-java-home/releases) → Settings → Plugins → Install Plugin from Disk

## Usage

- **Automatic**: Notification on project startup with "Sync Now" button
- **Manual**: Tools → Sync SDK from JAVA_HOME

Works great with direnv, nix, devbox, asdf, or any tool that sets `JAVA_HOME` per project.

## Requirements

- IntelliJ IDEA 2025.2+
- `JAVA_HOME` environment variable set

## License

MIT License
