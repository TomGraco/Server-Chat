public class MainClient {
    public static void main(String[] args) {

        String hostName = "localhost";
        int foreignPort = 1410;

        Client client = new Client(hostName, foreignPort);
        client.start();

    }
}
