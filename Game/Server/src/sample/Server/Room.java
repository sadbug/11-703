package sample.Server;

import java.io.IOException;

public class Room implements Runnable {
    private Connection connection1;
    private Connection connection2;
    private String name;
    private Thread thread;
    private Server server;

    public Room(Server server, String name, Connection connection) {
        System.out.println("i create new room");
        this.name = name;
        this.connection1 = connection;
        thread = new Thread(this);
        thread.start();
        this.server = server;
    }

    public boolean isFull() {
        return connection1 != null && connection2 != null;
    }

    public boolean addConnection(Connection connection) {
        if (connection1 == null) {
            connection1 = connection;
            if (isFull()) {
                connection2.getPw().println("connect 1");
                connection1.getPw().println("connect 0");
                server.getRooms().remove(name);

            }
            return true;
        }
        else if (connection2 == null) {
            connection2 = connection;

            if (isFull()) {
                connection1.getPw().println("connect 1");
                connection2.getPw().println("connect 0");
                server.getRooms().remove(name);

//                thread.start();
            }
            return true;
        }
        return false;
    }


    public boolean isEmpty() {
        return connection1 == null && connection2 == null;
    }

    @Override
    public void run() {
        String x = "";
        System.out.println("i start room action");
        while (!isEmpty()) {
            try {

                while (connection1.getIs().ready() ||(connection2 != null && connection2.getIs().ready())) {
                    if (connection1.getIs().ready()) {
                        x = connection1.getIs().readLine();
                        if (x.equals("exit")) {
                            if (connection2 != null) {
                                connection1.getPw().println("new");
                                connection2.getPw().println("lose");
                            }
                            exit();
                            break;
                        }
                        System.out.println("read from 2 " + x);
                        connection2.getPw().println(x);
                        System.out.println("print in 1 " + x);
                        if (x.contains("all")) {     // haha lol fall contains all)))0)
                            exit();
                            break;
                        }
                    }
                    else {
                        x = connection2.getIs().readLine();
                        if (x.equals("exit")) {
                            if (connection1 != null) {
                                connection2.getPw().println("new");
                                connection1.getPw().println("lose");
                            }
                            exit();
                            break;
                        }
                        System.out.println("read from 1 " + x);
                        connection1.getPw().println(x);
                        System.out.println("print in 2 " + x);
                        if (x.contains("all")) {
                            exit();
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        System.out.println("i exit");
        synchronized (connection1.getIs()) {
            connection1.getIs().notifyAll();
        }
        if (connection2 != null) {
            synchronized (connection2.getIs()) {
                connection2.getIs().notifyAll();
            }
        }
        connection1 = null;
        connection2 = null;

        server.getRooms().remove(name);

    }
}