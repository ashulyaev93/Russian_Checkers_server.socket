package checkers;

import checkers.model.Cell;
import checkers.model.ChessBoardData;
import checkers.model.Logic;
import checkers.view.ChessBoard;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class CheckersServer {
//    public CheckersServer(Logic logic) {
//        ServerSocket server = null; //создаём сервер;
//        Socket socket = null; //создаём сокет;
//
//        try {
//            server = new ServerSocket(8189); //определяем порт;
//            System.out.println("Сервер запущен");
//            socket = server.accept(); //точка подключения;
//            System.out.println("Клиент подключен");
//
//            Scanner in = new Scanner(socket.getInputStream());//входящий поток;
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//возврат потока вывода;
//            Scanner console = new Scanner(System.in);//ввод сообщений в консоль;
//
//            Thread t1 = new Thread(new Runnable() { //поток выхода и его запуск;
//                @Override
//                public void run() {
//                    while (true) {
//                        String str = in.nextLine();
//                        if (str.equals("/end")) {
//                            out.println("/end");
//                            break;
//                        }
//                        System.out.println("Client " + str);
//                    }
//                }
//            });
//            t1.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public CheckersServer(Logic logic) {

        ServerSocket server = null; //создаём сервер;
        Socket socket = null; //создаём сокет;
        ChessBoardData data = logic.data;

        try {
            server = new ServerSocket(8080); //определяем порт;
            System.out.println("Сервер запущен");
            socket = server.accept(); //точка подключения;
            System.out.println("Клиент подключен");

            Scanner in = new Scanner(socket.getInputStream());//входящий поток;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//возврат потока вывода;
            Scanner console = new Scanner(System.in);//ввод сообщений в консоль;

            Thread t1 = new Thread(new Runnable() { //поток выхода и его запуск;
                @Override
                public void run() {
                    while (true) {
                        String str = in.nextLine().toLowerCase();
                        if(str.equals("/end")) {
                            out.println("/end");
                            break;
                        }
                        if(str.length()!=4) {
                            out.println("Должны быть введены четыре буквы!");
                            continue;
                        }
                        if(str.charAt(0) < 'a' || str.charAt(0) > 'h'){
                            out.println("Используйте буквы от a до h");
                            continue;
                        }
                        if(str.charAt(1) < '1' || str.charAt(1) > '8'){
                            out.println("Используйте цифры от 1 до 8");
                            continue;
                        }
                        if(str.charAt(2) < 'a' || str.charAt(2) > 'h'){
                            out.println("Используйте буквы от a до h");
                            continue;
                        }
                        if(str.charAt(3) < '1' || str.charAt(3) > '8'){
                            out.println("Используйте цифры от 1 до 8");
                            continue;
                        }
                        int x1 = Character.getNumericValue(str.charAt(0));// - Character.getNumericValue('a') * 55 + 25;
                        int x2 = (int)str.charAt(2) - (int)'a'  * 55 + 25;

                        int y1 = (int)str.charAt(1) - (int)'1' * 55 + 25;
                        int y2 = (int)str.charAt(3) - (int)'1' * 55 + 25;

                        System.out.println("x1 = " + " " + x1 + "y1 = " + y1);
                        System.out.println(str.charAt(0));

//                        Cell cell1 = logic.getCellByXY(x1,y1);
//                        Cell cell2 = logic.getCellByXY(x2, y2);
//
//                        System.out.println(cell1 + " " + cell2);//x = 25 y = 300 x = 80 y = 245 (a3b4)

//                        logic.userStep(cell2, cell1);

                        System.out.println("Client " + str);
                    }
                }
            });
            t1.start();

            Thread t2 = new Thread(new Runnable() { //поток сообщений и его запуск;
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
            t2.setDaemon(true);//фоновый процесс, который очищает кэш, актуализирует значения. В данном случае для чтения с консоли;
            t2.start();

            //отладка;
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
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


//    Scanner in = new Scanner(socket.getInputStream());//входящий поток;
//    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//возврат потока вывода;
//    Scanner console = new Scanner(System.in);//ввод сообщений в консоль;
//
//    Thread t1 = new Thread(new Runnable() { //поток выхода и его запуск;
//        @Override
//        public void run() {
//            while (true) {
//                String str = in.nextLine();
//                if(str.equals("/end")) {
//                    out.println("/end");
//                    break;
//                }
//                System.out.println("Client " + str);
//            }
//        }
//    });
//            t1.start();