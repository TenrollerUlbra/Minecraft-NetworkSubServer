package org.subserver.functions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.subserver.models.Server;


public class ListServers {
    
    public static List<Server> listallservers()
    {

        List<Server> servers = new  ArrayList<Server>();

        String folderPath = "./subservers/";
        
        File folder = new File(folderPath);
        
        if (folder.exists() && folder.isDirectory()) 
        {
            File[] files = folder.listFiles();
            
            if (files != null) 
            {
                for (File file : files) 
                {
                    if (file.isDirectory()) 
                    {
                        //Create a new server object and add it to the list with the name and if it is valid.
                        servers.add(new Server(file.getName(), CheckIfValid(file.getAbsolutePath())));
                    }
                }
            }


        }

        return servers;

    }

    private static boolean CheckIfValid(String absolutePath) {
       

        File file = new File(absolutePath + "/server.jar");
        
        //Check if jar file exists inside the folder
        if(file.exists() && !file.isDirectory()) 
        { 
            return true;
        }

        return false;

    }

    
}
