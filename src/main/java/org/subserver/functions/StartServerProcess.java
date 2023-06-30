package org.subserver.functions;

import org.subserver.interfaces.ConsoleColors;
import org.subserver.models.ServerInfo;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

import static org.subserver.Main.main;

public class StartServerProcess {

    public static void startServerProcess(String name, String[] args, HashMap<String, ServerInfo> serversOnline) throws Exception {
        
        System.out.print("\033[H\033[2J");
        
        System.out.println(ConsoleColors.ANSI_YELLOW + "o - Checking if server exists" + ConsoleColors.ANSI_RESET);


        File file = new File("./subservers/" + name);
        
       
        if (!file.exists()) 
        {
            System.out.println(ConsoleColors.ANSI_RED + "[ERROR] The informed server was not found, leaving in 5 seconds" + ConsoleColors.ANSI_RESET);
            
            Thread.sleep(5000);
            
            System.out.print("\033[H\033[2J");
            
            main(args);
            
            return;
        }

        System.out.println(ConsoleColors.ANSI_GREEN + "o - Server founded, checking if the server is not running" + ConsoleColors.ANSI_RESET);
        
        if (serversOnline.containsKey(name)) 
        {
            System.out.println(ConsoleColors.ANSI_RED + "[ERROR] The informed server is already running" + ConsoleColors.ANSI_RESET);
            
            Thread.sleep(5000);
            
            System.out.print("\033[H\033[2J");
            
            main(args);
            
            return;
        }

        System.out.println(ConsoleColors.ANSI_GREEN + "o - Checking server configuration file" + ConsoleColors.ANSI_RESET);
        
        File fileConfig = new File("./subservers/" + name + "/subserverprocess.conf");
        
        if (!fileConfig.exists()) 
        {
            System.out.println(ConsoleColors.ANSI_RED + "[ERROR] The informed server do not have a configuration file, please check" + ConsoleColors.ANSI_RESET);

            Thread.sleep(5000);
            
            System.out.print("\033[H\033[2J");
            
            main(args);
            
            return;
        }

        String content = "";
        
        Scanner myReader = new Scanner(fileConfig);
        
        while (myReader.hasNextLine()) 
        {
            String data = myReader.nextLine();
            content += data + "\n";
        }

        myReader.close();


        //Check if the EULA file exists inside the server folder and if not create it
        File fileEULA = new File("./subservers/" + name + "/eula.txt");

        if(!fileEULA.exists())
        {

            System.out.println(ConsoleColors.ANSI_GREEN + "o - EULA file not found, creating it" + ConsoleColors.ANSI_RESET);

            FileWriter myWriter = new FileWriter("./subservers/" + name + "/eula.txt");

            myWriter.write("eula=true");

            myWriter.close();

        }
        
        System.out.println(ConsoleColors.ANSI_GREEN + "o - Starting server in the backgroud" + ConsoleColors.ANSI_RESET);
        
        ProcessBuilder processBuilder = new ProcessBuilder(content.split("\n")[0].replace("SH_START=", "").split(" "));
        
        processBuilder.directory(new File("./subservers/" + name + "/"));
        
        Process process = processBuilder.start();
        
        InputStream inputStream = process.getInputStream();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        String line;
        
        while ((line = reader.readLine()) != null) 
        {
            if (line.contains("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.")) {
                
                System.out.println(ConsoleColors.ANSI_RED + "[ERROR] You need to agree with the EULA, please check the eula file inside the server" + ConsoleColors.ANSI_RESET);
                
                Thread.sleep(5000);
                
                System.out.print("\033[H\033[2J");
                
                main(args);
                
                return;
            }

            if (line.contains("For help, type \"help\"")) {

                System.out.println("Java Version: " + System.getProperty("java.version"));
                
                String pid = !System.getProperty("java.version").startsWith("1.8.0") ? process.toString().split("=")[1].split(",")[0]:getProcessPid(process) + "";
                
                System.out.println(ConsoleColors.ANSI_GREEN + "The server is running :D, use the PID " + pid + " to close it or check more information" + ConsoleColors.ANSI_RESET);
                
                ServerInfo serverInfo = new ServerInfo(Integer.parseInt(pid), name, line);
                
                serversOnline.put(name, serverInfo);
                
                Thread.sleep(5000);
                
                System.out.print("\033[H\033[2J");
                
                main(args);
            }

            ServerInfo serverInfo = serversOnline.get(name);
            
            if (serverInfo != null && serverInfo.getLogs() != null) 
            {
                serverInfo.setLogs(serverInfo.getLogs() + "\n" + line);
            }
        }
        
        int exitCode = process.waitFor();
        
        if (exitCode == 1) 
        {
            System.out.println(ConsoleColors.ANSI_RED + "[ERROR] There was a issue while opening the server, please check\nIf JAVA is installed in the host\nIf there is a server.jar inside the server folder" + ConsoleColors.ANSI_RESET);
        }
    }

    private static int getProcessPid(Process process) throws Exception {
        
        Class<?> processClass = process.getClass();
        
        if (processClass.getName().equals("java.lang.UNIXProcess")) 
        {
            Field pidField = processClass.getDeclaredField("pid");
            
            pidField.setAccessible(true);
            
            return (int) pidField.get(process);

        } else 
        {
            throw new UnsupportedOperationException("Obter PID não é suportado nesta plataforma.");
        }
    }
}