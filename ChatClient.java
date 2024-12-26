import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change if needed
    private static final int PORT = 5450;
    private static boolean waitingForInput = true; // Flag to track input status

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            new ReadThread(socket).start();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String message;

            while (true) {
                if (waitingForInput) {
                    System.out.print("Enter Your Message Here: ");
                    message = scanner.nextLine();
                    out.println(message);
                    waitingForInput = false; // Set to false after sending a message
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ReadThread extends Thread {
        private BufferedReader in;

        public ReadThread(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Server's answer: " + message);
                    // After receiving a message, set waitingForInput to true to allow new input
                    waitingForInput = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}