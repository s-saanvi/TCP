import java.io.*; 
import java.net.Socket; 
import java.util.Scanner; 
 
public class TCPClient { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
 
        System.out.print("Enter server address: "); 
        String serverAddress = sc.nextLine(); 
 
        System.out.print("Enter file name to request: "); 
        String fileName = sc.nextLine(); 
 
        try (Socket socket = new Socket(serverAddress, 5000); 
             DataOutputStream dout = new DataOutputStream(socket.getOutputStream()); 
             DataInputStream din = new DataInputStream(socket.getInputStream())) { 

            dout.writeUTF(fileName); 
 
            String serverResponse = din.readUTF(); 
 
            if ("File found".equals(serverResponse)) { 
                long fileSize = din.readLong(); 
                
                System.out.println("Receiving file: " + fileName); 
             long bytesReceived = 0; 
                try (FileOutputStream fos = new FileOutputStream("received_" + fileName)) { 
                    byte[] buffer = new byte[1024]; 
                  
                    int bytesRead; 
                    while (bytesReceived < fileSize && (bytesRead = din.read(buffer)) > 0) { 
                        fos.write(buffer, 0, bytesRead); 
                        bytesReceived += bytesRead; 
                    } 
                } 
                System.out.println("File received successfully."); 
            System.out.println("File transfer completed. Total bytes received: "+bytesReceived); 
            } else { 
                System.out.println("Server response: " + serverResponse); 
            } 
        } catch (IOException e) { 
            System.err.println("Error: " + e.getMessage()); 
        } 
    } 
}