package checkers;

import checkers.model.Logic;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CheckersClient {
//    public static void main(String[] args) {
//        new CheckersClient();
//    }
//    public CheckersClient(){
//        Socket socket = null;
//
//        try {
//            socket = new Socket("localhost", 8189);
//            System.out.println("Вы в сети");
//
//            Scanner in = new Scanner(socket.getInputStream());
//            Scanner cli = new Scanner(System.in);
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
////            OutputStream out = socket.getOutputStream();
//            String input = null;
//
//            do{
//                input = cli.nextLine();
//                out.print(input);
////                out.flush();
//            }
//            while(!input.equals("/end"));
//
//        }catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Вы в сети");

            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner console = new Scanner(System.in);

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = in.nextLine();
                        if(str.equals("/end")) {
                            out.println("/end");
                            break;
                        }
                        System.out.println("Server " + str);
                    }
                }
            });
            t1.start();

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.println("Введите сообщение");
                        String str = console.nextLine();
                        System.out.println("Сообщение отправлено!");
                        out.println(str);
                    }
                }
            });
            t2.setDaemon(true);
            t2.start();

            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


//    public static void main(String[] args) {
//        Socket socket = null;
//
//        try {
//            socket = new Socket("localhost", 8080);
//            System.out.println("Вы в сети");
//
//            Scanner in = new Scanner(socket.getInputStream());
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            Scanner console = new Scanner(System.in);
//
//            Thread t1 = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        String str = in.nextLine();
//                        if(str.equals("/end")) {
//                            out.println("/end");
//                            break;
//                        }
//                        System.out.println("Server " + str);
//                    }
//                }
//            });
//            t1.start();
//
//            Thread t2 = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        System.out.println("Введите сообщение");
//                        String str = console.nextLine();
//                        System.out.println("Сообщение отправлено!");
//                        out.println(str);
//                    }
//                }
//            });
//            t2.setDaemon(true);
//            t2.start();
//
//            try {
//                t1.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }