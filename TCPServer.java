import java.io.*; 
import java.net.ServerSocket; 
import java.net.Socket; 
 
public class TCPServer { 
    public static void main(String[] args) { 
        try (ServerSocket serverSocket = new ServerSocket(5000)) { 
            System.out.println("Server is running on port 5000..."); 
 
            while (true) { 
                try (Socket socket = serverSocket.accept(); 
                     DataInputStream din = new DataInputStream(socket.getInputStream()); 
                     DataOutputStream dout = new DataOutputStream(socket.getOutputStream())) { 
 
                    System.out.println("Connected to client: " + socket.getInetAddress()); 

                    String requestedFile = din.readUTF(); 
                    System.out.println("Client requested file: " + requestedFile); 
 
                    File file = new File(requestedFile); 
 
                    if (file.exists()) { 
                        dout.writeUTF("File found"); 
                        dout.writeLong(file.length()); 

                        try (FileInputStream fin = new FileInputStream(file)) { 
                            byte[] buffer = new byte[1024]; 
                            int bytesRead; 
                            while ((bytesRead = fin.read(buffer)) > 0) { 
                                dout.write(buffer, 0, bytesRead); 
                            } 
                        } 
                        System.out.println("File sent successfully."); 
                    } else { 
                        dout.writeUTF("File not found"); 
                        System.out.println("Requested file not found: " + requestedFile); 
                    } 
                } catch (IOException e) { 
                    System.err.println("Error while communicating with client: " + e.getMessage()); 
                } 
            } 
        } catch (IOException e) { 
            System.err.println("Server error: " + e.getMessage()); 
        } 
    } 
}