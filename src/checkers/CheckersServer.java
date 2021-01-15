package checkers;

import checkers.model.Cell;
import checkers.model.ChessBoardData;
import checkers.model.Logic;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class CheckersServer {
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

                        HashMap<Character, Integer> x = new HashMap<Character, Integer>();
                        HashMap<Character, Integer> y = new HashMap<Character, Integer>();

                        x.put('a',0);
                        x.put('b',1);
                        x.put('c',2);
                        x.put('d',3);
                        x.put('e',4);
                        x.put('f',5);
                        x.put('g',6);
                        x.put('h',7);

                        y.put('1',7);
                        y.put('2',6);
                        y.put('3',5);
                        y.put('4',4);
                        y.put('5',3);
                        y.put('6',2);
                        y.put('7',1);
                        y.put('8',0);

                        int x1 = x.get(str.charAt(0)) * 55 + 25;
                        int x2 = x.get(str.charAt(2)) * 55 + 25;

                        int y1 = y.get(str.charAt(1)) * 55 + 25;
                        int y2 = y.get(str.charAt(3)) * 55 + 25;

                        Cell cell1 = logic.getCellByXY(x1,y1);
                        Cell cell2 = logic.getCellByXY(x2, y2);

                        System.out.println("cell1: " + cell1 + " " + "cell2: " + cell2);

                        logic.userStep(cell1, cell2);

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