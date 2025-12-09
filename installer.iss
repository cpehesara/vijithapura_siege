; Vijithapura Siege - Inno Setup Script
; This script creates a Windows installer for the game

#define MyAppName "Vijithapura Siege"
#define MyAppVersion "1.0"
#define MyAppPublisher "Vijithapura Games"
#define MyAppExeName "VijithapuraSiege.exe"
#define MyAppAssocName "Vijithapura Siege Game"
#define MyAppAssocExt ".vsg"

[Setup]
; Application information
AppId={{E8F9A1B2-3C4D-5E6F-7A8B-9C0D1E2F3A4B}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
; License file (optional - uncomment if you have a license file)
;LicenseFile=LICENSE.txt
; Output settings
OutputDir=output
OutputBaseFilename=VijithapuraSiege-Setup-{#MyAppVersion}
SetupIconFile=lwjgl3\icons\logo.ico
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern
; Privilege settings
PrivilegesRequired=lowest
; Uninstall settings
UninstallDisplayIcon={app}\{#MyAppExeName}
UninstallDisplayName={#MyAppName}

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 6.1; Check: not IsAdminInstallMode

[Files]
; Include all files from the packaged Windows build
Source: "lwjgl3\build\construo\winX64\roast\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Code]
function InitializeSetup(): Boolean;
begin
  Result := True;
end;
