package com.example.uploading.file;

/**
 * @author Javid Molaei
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.Socket;

public class Client {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        // Create Client Socket connect to port 900
        try (Socket socket = new Socket("localhost", 900)) {
            SampleDto sampleDto = new SampleDto();
            sampleDto.setName("javid");
            sampleDto.setAge(29);
//            // Convert the object to JSON.
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(sampleDto);


            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Sending the File to the Server");
//            dataOutputStream.write(json.getBytes());
//            System.out.println("dto sent");
            // Call SendFile Method

            // Create a Gson object
            Gson gson = new Gson();
            // Convert the DTO to JSON
            String json = gson.toJson(sampleDto);

            dataOutputStream.writeUTF(json);
            sendFile("C:\\Users\\javid\\Desktop\\docs\\reactSB.bmp");
            dataInputStream.close();
            dataInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // sendFile function define here
    private static void sendFile(String path) throws Exception {
        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Here we send the File to Server
        dataOutputStream.writeLong(file.length());
        // Here we break file into chunks
        byte[] buffer = new byte[1024*1024];
        int counter = 1;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            // Send the file to Server Socket
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
            System.out.println("sent part #"+ counter);
            counter++;
        }
        // close the file here
        fileInputStream.close();
    }

    File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
        return convFile;
    }
}