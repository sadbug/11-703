package sample.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable {
    private Socket socket;
    private Server server;
    private Thread thread;
    private PrintWriter pw;
    private BufferedReader is;

    public Connection(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;

        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);
            updateRooms();
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            String x = "";
            while (true) {

                x = is.readLine();
                String[] part = x.split(" ");
                System.out.println("i read from connection run" + x);
                switch (part[0]) {
                    case "create": {
                        Room room = new Room(server, (++Server.count)+"", this);
                        pw.println("waitStart " + Server.count);
                        server.getRooms().put(Server.count+"", room);
                        synchronized (is) {
                            is.wait();
                        }
                        break;
                    }
                    case "connect": {
                        System.out.println("i read connect " + x + "/" + part[0] + "/" + part[1]);
                        Room room = server.getRooms().get(part[1]);
                        room.addConnection(this);
                        synchronized (is) {
                            is.wait();
                        }
                        break;
                    }
                    case "updateRooms": {
                        pw.println("updateRooms");
                        for (String room: server.getRooms().keySet()) {
                            pw.println(room);
                            System.out.println(room);
                        }
                        pw.println("end");
                        System.out.println("i end");
                        break;
                    }
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getPw() {
        return pw;
    }

    public BufferedReader getIs() {
        return is;
    }


    public void updateRooms() {
        for (String room: server.getRooms().keySet()) {
            pw.println(room);
        }
        pw.println("end");
    }
}