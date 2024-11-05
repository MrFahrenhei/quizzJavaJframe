package com.app.quizz.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DotEnv {
    private final Map<String, String> envVars = new HashMap<>();
    public DotEnv(String filePath){
        try{
            Files.lines(Paths.get(filePath))
                    .filter(line -> line.contains("=") && !line.startsWith("#"))
                    .forEach(line ->{
                        String[] parts = line.split("=", 2);
                        if(parts.length == 2){
                            envVars.put(parts[0].trim(), parts[1].trim());
                        }
                    });
        }catch(IOException e){
            System.out.println("Error reading .env file: " + e.getMessage());
        }
    }
    public String get(String key) {
        return envVars.get(key);
    }
}
