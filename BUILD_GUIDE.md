# Vijithapura Siege - Build & Distribution Guide

## Building the Windows Installer

### Prerequisites
1. ✅ Inno Setup 6 (already installed)
2. Java JDK 8 or higher
3. Internet connection (first build downloads JDK)

### Build Steps

#### Option 1: Single Command (Recommended)
Run this command to build everything and create the installer:

```powershell
./gradlew buildInstaller
```

This will:
1. Package the game with bundled JRE for Windows
2. Create a professional Windows installer (.exe)
3. Output to: `output/VijithapuraSiege-Setup-1.0.exe`

**Build time:** First build takes 5-10 minutes (downloads JDK), subsequent builds take 2-3 minutes.

#### Option 2: Step by Step

If you want to build in separate steps:

```powershell
# Step 1: Package Windows application
./gradlew packageWinX64

# Step 2: Compile installer (manual)
# Run Inno Setup Compiler and open installer.iss
```

### Output Files

After successful build, you'll find:

- **Installer**: `output/VijithapuraSiege-Setup-1.0.exe` 
  - This is the distributable installer
  - Size: ~80-100 MB (includes bundled JRE)
  - Users can install without needing Java

- **Packaged Game**: `lwjgl3/build/construo/winX64/`
  - Raw game files before installer creation
  - Can be distributed as portable version (ZIP it)

### Distribution

#### For End Users
Simply share the installer: `VijithapuraSiege-Setup-1.0.exe`

Users can:
1. Double-click the installer
2. Choose installation location
3. Create desktop/start menu shortcuts
4. Play the game immediately (no Java required)

#### System Requirements
- **OS**: Windows 7 or later (64-bit)
- **RAM**: 512 MB minimum, 1 GB recommended
- **Disk**: 200 MB free space
- **Display**: 1280x720 or higher

### Customizing the Installer

Edit `installer.iss` to customize:

```iss
#define MyAppVersion "1.0"        ; Change version number
#define MyAppPublisher "Your Name" ; Change publisher name
```

Available customizations:
- Company name
- Version number
- Installation directory
- Start menu folder name
- Desktop icon creation
- License agreement (add LICENSE.txt)

### Quick Build Commands

```powershell
# Build installer
./gradlew buildInstaller

# Build portable JAR (requires Java on user's PC)
./gradlew lwjgl3:jar

# Build Windows-only JAR (smaller, requires Java)
./gradlew lwjgl3:jarWin

# Run game in development
./gradlew lwjgl3:run
```

### Troubleshooting

**Problem**: "Inno Setup not found"
- **Solution**: The script looks for Inno Setup at `C:\Program Files (x86)\Inno Setup 6\ISCC.exe`
- If installed elsewhere, edit `lwjgl3/build.gradle` line with `isccPath`

**Problem**: Build fails at packageWinX64
- **Solution**: Check internet connection (downloads JDK on first build)
- Clear cache: `./gradlew clean`

**Problem**: Installer shows wrong icon
- **Solution**: Replace `lwjgl3/icons/logo.ico` with your icon (must be .ico format)

### Building for Other Platforms

```powershell
# macOS (M1/Apple Silicon)
./gradlew packageMacM1

# macOS (Intel)
./gradlew packageMacX64

# Linux
./gradlew packageLinuxX64
```

### Version Updates

When releasing a new version:

1. Update version in `installer.iss`:
   ```iss
   #define MyAppVersion "1.1"
   ```

2. Rebuild:
   ```powershell
   ./gradlew clean buildInstaller
   ```

3. The new installer will be named: `VijithapuraSiege-Setup-1.1.exe`

---

## Development Commands

```powershell
# Run game (development mode)
./gradlew run

# Run with debugging
./gradlew run --debug-jvm

# Build JAR only
./gradlew jar

# Clean build files
./gradlew clean
```

## File Structure

```
Game/
├── installer.iss              # Inno Setup script
├── BUILD_GUIDE.md            # This file
├── output/                   # Generated installers appear here
│   └── VijithapuraSiege-Setup-1.0.exe
├── lwjgl3/
│   ├── build/construo/winX64/    # Packaged game files
│   └── icons/                     # Application icons
└── assets/                   # Game assets
```

---

**Ready to distribute?** Run `./gradlew buildInstaller` and share the generated .exe file!
