package org.subserver.functions;

import org.subserver.interfaces.ConsoleColors;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CreateProcess {

    public static void createProcess() throws IOException 
    {
        
        File SubserversFolder = new File("./subservers");
        
        // Create folder if not exists
        if (!SubserversFolder.exists()) {
            SubserversFolder.mkdir();
        }
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.print(ConsoleColors.ANSI_YELLOW + "Type name of server: " + ConsoleColors.ANSI_RESET);
        
        String name = scanner.nextLine();
        
        name = name.toLowerCase();

        File SubserverFolder = new File("./subservers/" + name);
        
        if (SubserverFolder.exists()) 
        {
            System.out.println(ConsoleColors.ANSI_RED + "Server already exists! " + ConsoleColors.ANSI_RESET);
        }
        else
        {
            SubserverFolder.mkdir();
            
            File SubserverProcessFile = new File("./subservers/" + name + "/subserverprocess.conf");

            SubserverProcessFile.createNewFile();
            
            String str = "SH_START=java -Xmx7G -jar server.jar\nSTOP_COMMAND=stop";
            
            BufferedWriter writer = new BufferedWriter(new FileWriter("./subservers/" + name + "/subserverprocess.conf"));
            
            writer.write(str);
            
            writer.close();

            System.out.println(ConsoleColors.ANSI_YELLOW + "You will need to have a server jar inside the server folder, would you like to download one now ?" + ConsoleColors.ANSI_RESET);

            System.out.print(ConsoleColors.ANSI_YELLOW + "Type yes or no: " + ConsoleColors.ANSI_RESET);

            String answer = scanner.nextLine();

            if (answer.equals("yes")) 
            {
            
                System.out.println(ConsoleColors.ANSI_YELLOW + "By default, we will download the latest version of papermc\nbut you can change this in the server folder :D." + ConsoleColors.ANSI_RESET);

                System.out.println(ConsoleColors.ANSI_YELLOW + "Downloading..." + ConsoleColors.ANSI_RESET);

                 String jarUrl = "https://serverjars.com/api/fetchJar/servers/paper"; 
                 
                 String destinationPath = "./subservers/" + name + "/server.jar"; 

                    try {
                        // Create URL object
                        URL url = new URL(jarUrl);

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        connection.setRequestMethod("GET");

                        int responseCode = connection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            
                            InputStream inputStream = connection.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                            // Create output stream to write the data to a file
                            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);

                            // Read data from the input stream and write it to the output stream
                            byte[] buffer = new byte[1024];

                            int bytesRead;
                            
                            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }

                            // Close streams
                            fileOutputStream.close();
                            bufferedInputStream.close();
                            inputStream.close();

                            System.out.println(ConsoleColors.ANSI_GREEN + "JAR file downloaded successfully." + ConsoleColors.ANSI_RESET);
                        } 
                        else 
                        {
                            System.out.println("Failed to download JAR file. Response Code: " + responseCode);
                        }

                        // Close the connection
                        connection.disconnect();
                    } catch (IOException e) 
                    {
                        e.printStackTrace();
                    }

            
            }
            
            System.out.println(ConsoleColors.ANSI_GREEN + "Server created sucessful! Now, up your server from folder subservers/" + name + " and configure subserverprocess.conf." + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_GREEN + "Press enter to continue..." + ConsoleColors.ANSI_RESET);

            scanner.nextLine();
            

        }
        

        scanner.close();
        
    }
}
