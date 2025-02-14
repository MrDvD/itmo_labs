package com.itmo.mrdvd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws FileNotFoundException, IOException {
    String envVar = "CONSOLE_CPATH";
    String collectionPath = System.getenv(envVar);
    if (collectionPath == null) {
        System.out.format("[ERROR] Environment variable '%s' does not exist.\n", envVar);
    } else {
        if (new File(collectionPath).isFile()) {
            FileInputStream file = new FileInputStream(collectionPath);
            BufferedInputStream in = new BufferedInputStream(file);
            int ch = in.read();
            while (ch != -1) {
                System.out.print((char) ch);
                ch = in.read();
            }
            in.close();
        } else {
            System.out.format("[ERROR] File '%s' does not exist.\n", collectionPath); // what's with permissions?   
        }
    }
  }
}
