import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner scanner;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean conected;

    public Client(String hostName, int foreignPort) {

        try {
            clientSocket = new Socket(hostName, foreignPort);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            scanner = new Scanner(System.in);
            conected=true;
        } catch (IOException ex) {
            System.out.println("Error connecting exception " + ex.getMessage());
            conected=false;
        }
    }

    public void start() {

        Thread clientThread = new Thread(new ClientRunnable());
        clientThread.start();

        System.out.println("Connected to server!");
        String name = scanner.nextLine();
        out.println(name);

        while(conected) {

            String message = scanner.nextLine();
            if (message.isEmpty()) {
                continue;
            }
            out.println(message);
            if (message.equalsIgnoreCase("/quit")) {
                conected = false;
            }
        }
        cleanup();

    }

    public void cleanup() {
        try {

            conected = false;
            if (scanner != null) scanner.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Disconnected from server!");
        } catch (IOException e) {
            System.out.println("Error closing client " + e.getMessage());
        }
    }

    private class ClientRunnable implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    if (message.contains("Disconnected") || message.contains("kicked")) {
                        conected = false; // Stop reading if disconnected or kicked
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection to server lost.");
                conected=false;
            }finally {
                cleanup();
            }
        }
    }


}
