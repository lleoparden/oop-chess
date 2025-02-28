package com.example.oop_chess;

import java.io.*;

public class Save_And_Load {
    static CurrentGameState obj = new CurrentGameState();

    // Save Method (Serialization)
    public static void write() {
        try {
            // Overwrite the file completely by recreating it
            File file = new File("SaveFile.dat");
            if (file.exists()) {
                file.delete(); // Delete the existing file to clear its content
            }

            try (FileOutputStream fileOut = new FileOutputStream(file);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

                out.writeObject(obj);
                System.out.println("Object has been serialized and saved to SaveFile.dat");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read Method (Deserialization)
    public static void read() {
        try (FileInputStream fileIn = new FileInputStream("SaveFile.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            obj = (CurrentGameState) in.readObject();
            obj.timeBlack=obj.blackElapsedTime;
            obj.timeWhite=obj.whiteElapsedTime;


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}