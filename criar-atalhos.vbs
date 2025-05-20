Set objShell = CreateObject("WScript.Shell")

projectPath = objShell.CurrentDirectory
batCliente = projectPath & "\target\run-cliente.bat"
batServidor = projectPath & "\target\run-servidor.bat"
icone = projectPath & "\src\main\resources\icon.ico"

' Atalho Cliente
Set linkCliente = objShell.CreateShortcut(projectPath & "\Iniciar Cliente.lnk")
linkCliente.TargetPath = batCliente
linkCliente.WindowStyle = 7
linkCliente.IconLocation = icone
linkCliente.WorkingDirectory = projectPath
linkCliente.Save

' Atalho Servidor
Set linkServidor = objShell.CreateShortcut(projectPath & "\Iniciar Servidor.lnk")
linkServidor.TargetPath = batServidor
linkServidor.WindowStyle = 7
linkServidor.IconLocation = icone
linkServidor.WorkingDirectory = projectPath
linkServidor.Save
