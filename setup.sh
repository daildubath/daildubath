#!/bin/bash

# ==========================================
# INITIALIZATION
# ==========================================
# Array to keep track of any failures throughout the entire script
FAILED_APPS=()

echo "Performing initial system update and upgrade..."
# Doing this once at the top saves time!
sudo apt update && sudo apt upgrade -y

# ==========================================
# INSTALL BRAVE BROWSER
# ==========================================
echo "Installing Brave Browser..."
# Running the official script and catching failures
if ! curl -fsS https://dl.brave.com/install.sh | sh; then
    echo "ERROR: Brave Browser installation failed."
    FAILED_APPS+=("Brave Browser")
else
    echo "Brave Browser installed successfully!"
fi

# ==========================================
# INSTALL VISUAL STUDIO CODE
# ==========================================
echo "Starting Visual Studio Code installation..."

echo "Installing dependencies (wget, gpg)..."
if ! sudo apt install -y wget gpg; then
    echo "ERROR: VS Code dependencies failed."
    FAILED_APPS+=("VS Code (Dependencies failed)")
else
    # Download and install the Microsoft signing key
    echo "Downloading Microsoft signing key..."
    wget -qO- https://packages.microsoft.com/keys/microsoft.asc | sudo gpg --dearmor -yes -o /usr/share/keyrings/microsoft.gpg

    # Add the VS Code repository to your sources
    echo "Adding VS Code repository..."
    echo "Types: deb" > vscode.sources
    echo "URIs: https://packages.microsoft.com/repos/code" >> vscode.sources
    echo "Suites: stable" >> vscode.sources
    echo "Components: main" >> vscode.sources
    echo "Architectures: amd64,arm64,armhf" >> vscode.sources
    echo "Signed-By: /usr/share/keyrings/microsoft.gpg" >> vscode.sources
    sudo mv vscode.sources /etc/apt/sources.list.d/vscode.sources

    # Update package cache JUST for the newly added VS Code repository
    echo "Updating apt cache for new repository..."
    sudo apt update

    # Finally, install VS Code and log it if it fails
    echo "Installing VS Code..."
    if ! sudo apt install -y code; then
        echo "ERROR: VS Code installation failed."
        FAILED_APPS+=("Visual Studio Code")
    else
        echo "Visual Studio Code installed successfully!"
    fi
fi

echo "Updating system package lists..."
sudo apt update

echo "Installing standard repository applications..."
# Using -y automatically answers "yes" to the installation prompts
echo "Installing standard repository applications..."
STANDARD_APPS=(
    git flameshot krita steam-installer linssid remmina 
    wireshark kazam kdenlive audacity blanket btop
)

for app in "${STANDARD_APPS[@]}"; do
    echo "Installing $app..."
    if ! sudo apt install -y "$app"; then
        FAILED_APPS+=("$app")
    fi
done

echo "Standard applications installed successfully!"

# ==========================================
# PART 1.5: REMOVING DEFAULT BLOATWARE
# ==========================================

echo "Removing unneeded default applications..."
APPS_TO_REMOVE=(
    firefox sticky warpinator hypnotix rhythmbox 
    thunderbird transmission-gtk transmission-common hexchat
)

for app in "${APPS_TO_REMOVE[@]}"; do
    echo "Removing $app..."
    if ! sudo apt purge -y "$app"; then
        FAILED_APPS+=("Failed to remove: $app")
    fi
done

echo "Cleaning up leftover dependencies..."
sudo apt autoremove -y

# ==========================================
# PART 3: EXTERNAL PACKAGES & FLATPAKS
# ==========================================

echo "Creating temporary directory for downloads..."
mkdir -p ~/setup_downloads
cd ~/setup_downloads

# 1. Minecraft Launcher
echo "Installing Minecraft Launcher..."
if wget -qO Minecraft.deb "https://launcher.mojang.com/download/Minecraft.deb" && sudo apt install -y ./Minecraft.deb; then
    echo "Minecraft installed."
else
    FAILED_APPS+=("Minecraft Launcher")
fi

# 2. Discord
echo "Installing Discord..."
if wget -qO discord.deb "https://discord.com/api/download?platform=linux&format=deb" && sudo apt install -y ./discord.deb; then
    echo "Discord installed."
else
    FAILED_APPS+=("Discord")
fi

# 3. Zoom
echo "Installing Zoom..."
if wget -qO zoom_amd64.deb "https://zoom.us/client/latest/zoom_amd64.deb" && sudo apt install -y ./zoom_amd64.deb; then
    echo "Zoom installed."
else
    FAILED_APPS+=("Zoom")
fi

# 4. Joplin (Official Bash Script)
echo "Installing Joplin..."
if wget -O - https://raw.githubusercontent.com/laurent22/joplin/dev/Joplin_install_and_update.sh | bash; then
    echo "Joplin installed."
else
    FAILED_APPS+=("Joplin")
fi

# 5. Bitwarden (Via Flatpak)
echo "Installing Bitwarden via Flatpak..."
if flatpak install -y flathub com.bitwarden.desktop; then
    echo "Bitwarden installed."
else
    FAILED_APPS+=("Bitwarden")
fi

# 6. JetBrains Toolbox
echo "Installing JetBrains Toolbox..."
# Ensure libfuse2 is installed (required for Jetbrains and AppImages on newer Ubuntu/Mint versions)
sudo apt install -y libfuse2
if wget -qO jetbrains.tar.gz "https://data.services.jetbrains.com/products/download?code=TBA&platform=linux" && tar -xzf jetbrains.tar.gz; then
    # Find the extracted folder name dynamically
    DIR_NAME=$(tar -tf jetbrains.tar.gz | head -1 | cut -f1 -d"/")
    echo "Initializing JetBrains Toolbox..."
    # Run the toolbox silently in the background so it installs itself to your app menu
    ./"$DIR_NAME"/jetbrains-toolbox &
    sleep 3
    echo "JetBrains Toolbox installed!"
else
    FAILED_APPS+=("JetBrains Toolbox")
fi

# 7. OpenTabletDriver (For Huion)
echo "Installing OpenTabletDriver..."
if wget -qO opentabletdriver.deb "https://github.com/OpenTabletDriver/OpenTabletDriver/releases/latest/download/opentabletdriver_0.6.7-1_x64.deb" && sudo apt install -y ./opentabletdriver.deb; then
    echo "OpenTabletDriver installed."
else
    FAILED_APPS+=("OpenTabletDriver")
fi

# Clean up temporary downloads
echo "Cleaning up temporary files..."
cd ~
rm -rf ~/setup_downloads


# ==========================================
# PART 4: RESTORE CINNAMON CONFIGURATION
# ==========================================
echo "Restoring Cinnamon desktop configuration..."

# Check if the backup files exist directly in your home directory (~/)
if [[ -f ~/cinnamon-files.tar.gz ]] && [[ -f ~/cinnamon-backup.ini ]]; then
    echo "Found backup files in home folder! Restoring applets, desklets, and settings..."
    
    # Create the directory if it doesn't exist
    mkdir -p ~/.local/share/cinnamon
    
    # Extract the files silently from the home directory
    tar -xzf ~/cinnamon-files.tar.gz -C ~/.local/share/cinnamon
    
    # Load the settings into Cinnamon from the home directory
    dconf load /org/cinnamon/ < ~/cinnamon-backup.ini
    
    echo "Cinnamon configuration restored!"
else
    echo "WARNING: cinnamon-files.tar.gz or cinnamon-backup.ini not found in ~/."
    echo "Skipping Cinnamon configuration restore."
    FAILED_APPS+=("Cinnamon Theme/Applet Restore (Files missing)")
fi

# ==========================================
# SCRIPT SUMMARY & TO-DO LIST
# ==========================================


# Clear the screen so the summary is easy to read
clear

# Ensure the Documents directory exists
mkdir -p ~/Documents
LOG_FILE="$HOME/Documents/errors.txt"

# Everything inside these brackets { } will be printed to the screen AND saved to the text file
{
    echo "======================================================================="
    echo "                        INSTALLATION COMPLETE                          "
    echo "======================================================================="

    if [ ${#FAILED_APPS[@]} -ne 0 ]; then
        echo "The following apps FAILED to install and need manual attention:"
        for app in "${FAILED_APPS[@]}"; do
            echo " - $app"
        done
    else
        echo "All scripted applications installed successfully!"
    fi

    echo ""
    echo "-----------------------------------------------------------------------"
    echo "                             TO DO LIST                                "
    echo "-----------------------------------------------------------------------"
    echo "1. VMWare Workstation / Player:"
    echo "   - Cannot be scripted easily. Download the .bundle file from Broadcom."
    echo "   - Run it in terminal: sudo chmod +x VMware-*.bundle && sudo ./VMware-*.bundle"
    echo ""
    echo "2. Wireshark Note:"
    echo "   - If you didn't click 'Yes' during the prompt earlier, you must run:"
    echo "     sudo dpkg-reconfigure wireshark-common"
    echo "     ...and add your user to the 'wireshark' group to capture packets."
    echo ""
    echo "3. OpenTabletDriver Note (Huion Tablet):"
    echo "   - You will likely need to reboot your computer before the tablet"
    echo "     drivers (udev rules) fully take effect."
    echo "======================================================================="
} | tee "$LOG_FILE"

echo ""
echo "A copy of this summary has been saved to: $LOG_FILE"
echo ""

# The script will pause here until you physically press Enter
read -p "Press [ENTER] to continue the script..."

echo ""
echo "A system reboot is required to apply the Huion tablet rules, group"
echo "permissions, and the restored Cinnamon desktop theme/layout."
echo ""

# Prompt for Reboot
read -p "Would you like to reboot now? (y/n): " REBOOT_CONFIRM

if [[ "$REBOOT_CONFIRM" =~ ^[Yy]$ ]]; then
    echo "Rebooting system..."
    sudo reboot
else
    echo "Reboot skipped. Please remember to reboot manually later to apply changes!"
fi