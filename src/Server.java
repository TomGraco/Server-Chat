import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<ServerWorker> workers = new ArrayList<>();
    private int port;
    private String admin;
    private ServerSocket serverSocket;

    public Server(int port, String admin) {
        this.port = port;
        this.admin = admin;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + "\033[36;4m"+port+"\033[m");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection sucessfull!");
                ServerWorker serverWorker = new ServerWorker(clientSocket);
                workers.add(serverWorker);
                Thread t1 = new Thread(serverWorker);
                t1.start();
            }
        } catch (IOException e) {
            System.out.println("Server exeption " + e.getMessage());
        }
    }

    public String listClients() {
        StringBuilder builder = new StringBuilder("\n\n");

        synchronized (workers) {
            for (ServerWorker sw : workers) {
                builder.append("\t");
                builder.append(sw.clientName);
                builder.append("\n");

            }
        }
        return builder.toString();
    }

    public synchronized void sendToAll(String message) {
        synchronized (workers) {
            for (ServerWorker worker : workers) {
                worker.send(message);
            }
        }
    }

    public void kick(String clientName) {
        synchronized (workers) {
            for (ServerWorker worker : workers) {
                if (worker.clientName.equalsIgnoreCase(clientName)) {
                    worker.send("You have been kicked from the chat by the admin, u peasant! 🐔");
                    worker.cleanup();
                    sendToAll(clientName + " has been kicked from the chat! ☠️");
                    return;
                }
            }
        }
        System.out.println("Client -" + clientName + "- not found.");
    }

    private class ServerWorker implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;
        private List<String> jokes;
        private volatile boolean conected = true;

        public ServerWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
            jokes = new ArrayList<>();
            loadJokes();

            try {
                this.out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                System.out.println("Worker IO execption " + e.getMessage());
            }
        }

        public void loadJokes() {
            jokes.add("\033[31;4mToday, I asked my phone 'Siri, why am I still single?' and it activated the front camera.\033[31;m");
            jokes.add("\033[31;4mI don't have a carbon footprint. I just drive everywhere.\033[31;m");
            jokes.add("\033[31;4mThe doctor gave me one year to live, so I shot him with my gun. The judge gave me 15 years. Problem solved.\033[31;m");
            jokes.add("\033[31;4mWhat's red and bad for your teeth? A brick.\033[31;m");
            jokes.add("\033[31;4mYou're not completely useless. You can always be used as a bad example.\033[31;m");
        }

        @Override
        public void run() {
            try {
                out.println("\033[33;1m ❤️ Welcome to Tom's humble server! Please enter your name:  ❤️\033[m");
                clientName = in.readLine();
                if (clientName == null || clientName.isEmpty()) {
                    cleanup();
                    return;
                }
                sendToAll("\033[34;4m"+clientName+"\033[m" + " has joined the chat. ✅");
                out.println("You are now connected, type your messages below: ⏬");

                String message;
                while (conected && (message = in.readLine()) != null) {
                    System.out.println(clientName + ": " + message);
                    if (message.startsWith("/")) {
                        comandList(message);
                    } else {
                        sendToAll("\033[34;1m"+clientName +"\033[m"+ ": " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection lost with " + clientName + " 💩");
            } finally {
                cleanup();
            }
        }

        public void comandList(String message) {

            String reaction = "";

            if (message.startsWith("/scream")) {
                reaction = clientName + ": 📢 " + message.substring(8).toUpperCase();
            } else if (message.startsWith("/joke")) {
                reaction = clientName + " has a joke to tell: " + jokes.get((int) (Math.floor(Math.random() * jokes.size())));
            } else if (message.startsWith("/flip")) {
                reaction = clientName + ": (╯°□°）╯︵ ┻━┻";
            } else if (message.startsWith("/shit")) {
                reaction = clientName + ": 💩💩💩";
            } else if (message.startsWith("/quit")) {
                sendToAll(clientName + " has left the chat. 🚪");
                conected=false;
                cleanup();
                return;
            } else if (message.startsWith("/ls")) {
                reaction = "Connected clients: " + listClients();
                out.println(reaction);
                return;
            } else {
                System.out.println("Command not recognized!");
                return;
            }
            sendToAll(reaction);

        }

        public void send(String message) {
            out.println(message);
        }

        public void cleanup() {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error during cleanup for " + clientName);
            } finally {
                synchronized (workers) {workers.remove(this);}
                System.out.println(clientName + " has left the chat.");
            }
        }

    }


}
