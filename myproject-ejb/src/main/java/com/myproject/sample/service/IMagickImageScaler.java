package com.myproject.sample.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class IMagickImageScaler implements ImageScaler{
    private static final File IM_DIR = new File("C:\\ImageMagick-7.0.2-Q16");

    @Override
    public String identify(String filename) {
        String result = "not found";
        ProcessBuilder processBuilder = new ProcessBuilder("CMD", "/C", "magick.exe", "identify", STORAGE + filename);
        processBuilder.directory(IM_DIR);
        try {
            Process process = processBuilder.inheritIO().start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            result = builder.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return result;
    }

    @Override
    public void scale(String filename, long width, long height){
        String params = width + "x" + height;
        scale(filename, params);
    }

    @Override
    public void scale(String filename, long percentage){
        String params = percentage + "%";
        scale(filename, params);
    }


    private void scale(String filename, String params){
        try {
            new ProcessBuilder("CMD", "/C",
                    "magick.exe", "convert", STORAGE + filename,
                    "-resize", params, STORAGE + "resized-" + filename)
                    .directory(IM_DIR)
                    .inheritIO()
                    .start();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ImageScaler im = new IMagickImageScaler();
        System.out.println(im.identify("Koala.jpg"));
        im.scale("Koala.jpg", 150);
    }

}