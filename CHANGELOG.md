# Changelog

All notable changes to the "JAVA_HOME SDK Sync" plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.2] - 2025-10-22

### Changed
- Extended compatibility to support IntelliJ IDEA 2025.1.x (build 251) and 2025.2.x (build 252)

## [0.2.1] - 2025-10-22

### Changed
- Renamed plugin to "JAVA_HOME SDK Sync" for better clarity
- Refactored package structure from `com.ludosch.intellij` to `com.ludosch`
- Added platform module dependency to fix JetBrains Marketplace warning

### Added
- Plugin icon (SVG format)
- MIT License file
- Vendor contact information

## [0.2.0] - 2025-10-22

### Added
- Manual sync action in Tools menu ("Sync SDK from JAVA_HOME")
- Better error handling and validation for invalid JAVA_HOME paths

### Changed
- Improved SDK detection to avoid duplicate JDK entries
- Enhanced logging for troubleshooting

### Fixed
- Fixed case-sensitivity issues when matching existing SDKs
- Improved threading model for better stability

## [0.1.0] - Initial Release

### Added
- Automatic synchronization of Project SDK with JAVA_HOME environment variable on project open
- JDK auto-registration in IntelliJ's JDK table if not present
- Support for IntelliJ IDEA 2025.2+
