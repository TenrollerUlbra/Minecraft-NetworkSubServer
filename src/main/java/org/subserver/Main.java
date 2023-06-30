package org.subserver;

import org.subserver.interfaces.ConsoleColors;
import org.subserver.models.ServerInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static org.subserver.functions.CreateProcess.createProcess;
import static org.subserver.functions.StartServerProcess.startServerProcess;

public class Main {

    static void ShowLogo()
    {

        System.out.println(ConsoleColors.ANSI_CYAN + " _____         _      _____                                     \r\n" + //
                "/  ___|       | |    /  ___|                                    \r\n" + //
                "\\ `--.  _   _ | |__  \\ `--.   ___  _ __ __   __  ___  _ __  ___ \r\n" + //
                " `--. \\| | | || '_ \\  `--. \\ / _ \\| '__|\\ \\ / / / _ \\| '__|/ __|\r\n" + //
                "/\\__/ /| |_| || |_) |/\\__/ /|  __/| |    \\ V / |  __/| |   \\__ \\\r\n" + //
                "\\____/  \\__,_||_.__/ \\____/  \\___||_|     \\_/   \\___||_|   |___/\r\n" + //
                "                                                                \r" + //
                "                                                                " + ConsoleColors.ANSI_RESET);        

    }

    public static HashMap<String, ServerInfo> serversOnline = new HashMap<String, ServerInfo>();

    public static void main(String[] args) throws Exception 
    {
        Runtime.getRuntime().addShutdownHook(new Thread() 
        {
            public void run() 
            {
                for (ServerInfo serverInfo : serversOnline.values()) 
                {
                    try 
                    {
                        ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9", String.valueOf(serverInfo.getPID()));
                        processBuilder.start();
                    } 
                    catch (IOException ignored) 
                    {

                    }
                }
            }
        });

        String command = "";

        Scanner scanner = new Scanner(System.in);

        do
        {

            System.out.print("\033[H\033[2J");
            System.out.flush();

            ShowLogo();

            System.out.println(ConsoleColors.ANSI_CYAN + "===========================================================" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_PURPLE + "HELP COMMANDS" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_PURPLE + "                             " + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "[" + ConsoleColors.ANSI_PURPLE + "1" + ConsoleColors.ANSI_CYAN + "]" + " - Create a subserver" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "[" + ConsoleColors.ANSI_PURPLE + "2" + ConsoleColors.ANSI_CYAN + "]" + " - Start a server" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "[" + ConsoleColors.ANSI_PURPLE + "3" + ConsoleColors.ANSI_CYAN + "]" + " - List the created Servers" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "[" + ConsoleColors.ANSI_PURPLE + "4" + ConsoleColors.ANSI_CYAN + "]" + " - Verify Server info" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "[" + ConsoleColors.ANSI_PURPLE + "5" + ConsoleColors.ANSI_CYAN + "]" + " - Stop SubProcess manager" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_CYAN + "===========================================================" + ConsoleColors.ANSI_RESET);
            
            System.out.print(ConsoleColors.ANSI_BLUE + "Enter a command: " + ConsoleColors.ANSI_RESET);

            command = "";

            command = scanner.nextLine();

            switch (command) 
            {
                case "1": 
                {
                    createProcess();

                    break;
                }
                case "2": 
                {
                    System.out.print(ConsoleColors.ANSI_BLUE + "Type name of server: " + ConsoleColors.ANSI_RESET);
                    
                    String name = scanner.nextLine();
                    
                    startServerProcess(name, args, serversOnline);
                    
                    break;
                }
                case "4": 
                {
                    System.out.print(ConsoleColors.ANSI_BLUE + "Type name of server: " + ConsoleColors.ANSI_RESET);
                    String name = scanner.nextLine();
                    ServerInfo serverInfo = serversOnline.get(name);

                    if (serverInfo == null) {
                        System.out.println(ConsoleColors.ANSI_RED + "Server not found!" + ConsoleColors.ANSI_RESET);
                        main(args);
                        break;
                    }

                        System.out.println(ConsoleColors.ANSI_CYAN + "===========================================================" + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "      SUBSERVER - CREATE A MINECRAFT NETWORK IN UNIQUE CONTAINER           " + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "===========================================================" + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_BLUE + "SERVER INFO - " + name + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "                             " + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "Server name: " + serverInfo.getName() + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "Server PID: " + serverInfo.getPID() + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "PRESS L TO VIEW LOGS SERVER AND PRESS S TO STOP" + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_CYAN + "=============================================================" + ConsoleColors.ANSI_RESET);
                        System.out.print(ConsoleColors.ANSI_BLUE + "Enter a command: " + ConsoleColors.ANSI_RESET);
                    
                        String command2 = scanner.nextLine();

                        if (command2.equalsIgnoreCase("l")) 
                        {
                            String stop = scanner.nextLine();
                            
                            while (true) {
                                
                                System.out.print("\033[H\033[2J");
                                
                                if (stop.equalsIgnoreCase("s")) 
                                {
                                    main(args);
                                    break;
                                }

                                Thread.sleep(1000);
                            }
                        }
                
                    break;
                }
                case "5": {
                    System.out.println(ConsoleColors.ANSI_RED + "Stopping SubProcess manager..." + ConsoleColors.ANSI_RESET);
                    
                    System.exit(0);

                    break;
                }
                case "stop": {

                    for (ServerInfo serverInfo : serversOnline.values()) 
                    {
                        try 
                        {
                            ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9", String.valueOf(serverInfo.getPID()));
                            processBuilder.start();
                        } 
                        catch (IOException ignored) 
                        {}
                    }
                    scanner.close();
                    System.exit(0);
                }
                default: 
                {
                    System.out.println(ConsoleColors.ANSI_RED + "Command not found!" + ConsoleColors.ANSI_RESET);
                    main(args);
                    break;
                }
            }
    } 
    while (command != "stop" && command != "5");
    

    }
}