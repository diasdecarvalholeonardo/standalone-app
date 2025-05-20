Set oWS = WScript.CreateObject("WScript.Shell")
sLinkFile = oWS.SpecialFolders("Desktop") & "\ScreenSender.lnk"
Set oLink = oWS.CreateShortcut(sLinkFile)
oLink.TargetPath = "javaw.exe"
oLink.Arguments = " -jar C:\SAAS\standalone-app\target\screen-sender-1.0.0-jar-with-dependencies.jar"
oLink.IconLocation = "C:\SAAS\standalone-app\src\main\resources\icon.ico"
oLink.WorkingDirectory = "C:\SAAS\standalone-app"
oLink.WindowStyle = 1
oLink.Description = "Iniciar Screen Sender"
oLink.Save
