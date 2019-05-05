package sample.Control;

import javafx.application.Platform;
import sample.Views.PlayView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Logic implements Runnable{
    private PrintWriter pw;
    private BufferedReader is;
    private PlayView playView;
    public static boolean mark;
    public static boolean me;
    private boolean game;
    private Thread thread;

    public Logic() {
        int port = 3456;
        Socket s = null;

        try {
            s = new Socket("localhost", port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
        thread.start();
    }


    public Logic(String host) {
        int port = 3456;
        Socket s = null;

        try {
            s = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
        thread.start();
        initial();

    }

    public void initial() {
        playView = new PlayView();
        playView.setLogic(this);

        playView.setWriter(pw);
        playView.setReader(is);
        playView.updateView(playView.chooseRoom(updateRooms()));
        playView.show();
    }


    @Override
    public void run() {

        try {
            while (true) {
                System.out.println("i start thread");

                String x = "";
                while (true) {
                    x = is.readLine();
                    System.out.println("i read from run1 " + x);
                    mark = true;
                    if (x.equals("connect 1")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playView.updateView(playView.startPlay());
                            }
                        });
                        me = true;
                        break;

                    } else if (x.equals("connect 0")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playView.updateView(playView.startPlay());
                            }
                        });
                        me = false;
                        break;

                    } else if (x.contains("waitStart")) {
                        String finalX = x;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playView.updateView(playView.waitingOpponent(finalX.split(" ")[1]));
                            }
                        });

                    } else if (x.equals("updateRooms")) {
                        synchronized (is) {
                            is.notify();
                            is.wait();
                        }
                    } else {
                        System.out.println("i don't now what is it: " + x);
                    }
                }


                game = true;
                while (game) {
                    System.out.println("i wait me");
                    while (me) {
                        System.out.println(me);

                    }
                    if (!game) {
                        System.out.println("huhu blet");
                        break;
                    }
                    System.out.println("me: " + me + " game: " + game);

                    System.out.println("i wait my op");
                    x = is.readLine();
                    if (x.equals("lose")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playView.updateView(playView.printResult("Ваш соперник отключился"));
                            }
                        });
                        game = false;
                        break;
                    }
                    if (x.equals("new")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playView.updateView(playView.chooseRoom(updateRooms()));
                            }
                        });
                        game = false;
                        break;
                    }

                    System.out.println("i read from run2" + x);
                    String[] coord = x.split(" ");


                    playView.fv.getField().open(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
                    me = true;
                    if (coord.length == 3) {
                        if (coord[2].equals("fall")) {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    playView.updateView(playView.printResult("Вы проиграли"));
                                }
                            });
                        }
                        else if (coord[2].equals("all")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    playView.updateView(playView.printResult("Ничья! Попробуйте ещё раз!"));
                                }
                            });
                        }
                        me = false;
                        game = false;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            System.out.println("io");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendStep(int x, int y, boolean win, boolean all) {
        System.out.println("send Step " + me);
        if (me) {
            if (win) {
                pw.println(x + " " + y + " " + "fall");
                playView.updateView(playView.printResult("Вы одержали победу!"));
                game = false;
            }
            else if (all) {
                pw.println(x + " " + y + " " + "all");
                playView.updateView(playView.printResult("Ничья! Попробуйте ещё раз"));
                game = false;
            }
            else pw.println(x + " " + y);
            System.out.println(x + " " + y + ".");
            me = false;
        }
    }

    public List<String> updateRooms() {
        pw.println("updateRooms");
        List<String> result = new ArrayList<>();
        synchronized (is) {

            System.out.println("wait rooms from server");
            try {
                is.wait();
                String x = is.readLine();
                System.out.println("i read from updateRooms " + x);
                while (!x.equals("end")) {
                    result.add(x);
                    x = is.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            is.notifyAll();
        }

        return result;
    }

    public boolean createRoom() {
        pw.println("create");
        return true;
    }

    public boolean connectToRoom(String number) {
        pw.println("connect " + number);
        return true;
    }

    public boolean exit() {
        pw.println("exit");
        return true;
    }

    public PrintWriter getPw() {
        return pw;
    }



    public BufferedReader getIs() {
        return is;
    }


}