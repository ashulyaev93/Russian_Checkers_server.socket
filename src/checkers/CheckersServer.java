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
    private static final int PORT = 8080;
    private static final HashMap<Character, Integer> charToX = new HashMap<Character, Integer>();
    private static final HashMap<Character, Integer> charToY = new HashMap<Character, Integer>();
    private static final Socket[] sockets = new Socket[2]; //ждём сокеты;

    public CheckersServer(Logic logic) throws IOException {
        ServerSocket server = null; //создаём сервер;
        ChessBoardData data = logic.data;
        int playerCount = 0;

        charToX.put('a',0);
        charToX.put('b',1);
        charToX.put('c',2);
        charToX.put('d',3);
        charToX.put('e',4);
        charToX.put('f',5);
        charToX.put('g',6);
        charToX.put('h',7);

        charToY.put('1',7);
        charToY.put('2',6);
        charToY.put('3',5);
        charToY.put('4',4);
        charToY.put('5',3);
        charToY.put('6',2);
        charToY.put('7',1);
        charToY.put('8',0);

        try {
            server = new ServerSocket(PORT); //определяем порт;
            System.out.println("Сервер запущен");

            while (!logic.data.gameExit) {
                Socket socket = server.accept(); //точка подключения;
                System.out.println("Клиент подключен");
                playerCount++;
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//возврат потока вывода;
                if (playerCount > 2) {
                    out.print("/end");
                    System.out.println("Играть могут не более двух человек!");
                    socket.close();
                    continue;
                }
                if(playerCount == 2 && (!logic.data.whiteIsHuman || !logic.data.blackIsHuman)){
                    out.print("/end");
                    System.out.println("Один из игроков робот!");
                    socket.close();
                    playerCount--;
                    continue;
                }
                sockets[playerCount - 1] = socket;
                processSocket(socket, out, logic);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void processSocket(Socket socket, PrintWriter out, Logic logic) throws IOException{
            Scanner in = new Scanner(socket.getInputStream());//входящий поток;
//            Scanner console = new Scanner(System.in);//ввод сообщений в консоль;

            Thread t1 = new Thread(new Runnable() { //поток выхода и его запуск;
                @Override
                public void run() {
                    while (true) {
                        String str = in.nextLine().toLowerCase();
                        if(str.equals("/end")) {
                            out.println("/end");
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

                        int x1 = charToX.get(str.charAt(0)) * 55 + 25;
                        int x2 = charToX.get(str.charAt(2)) * 55 + 25;

                        int y1 = charToY.get(str.charAt(1)) * 55 + 25;
                        int y2 = charToY.get(str.charAt(3)) * 55 + 25;

                        Cell cell1 = logic.getCellByXY(x1,y1);
                        Cell cell2 = logic.getCellByXY(x2, y2);

                        System.out.println("cell1: " + cell1 + " " + "cell2: " + cell2);

                        if(socket == sockets[0] && !cell1.isWhite()){
                            System.out.println("Вы играете за белых!");
                            continue;
                        }

                        if(socket == sockets[1] && !cell1.isBlack()){
                            System.out.println("Вы играете за черных!");
                            continue;
                        }

                        logic.userStep(cell1, cell2);

                        System.out.println("Client " + str);
                    }
                }
            });
            t1.start();

//            Thread t2 = new Thread(new Runnable() { //поток сообщений и его запуск;
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
//            t2.setDaemon(true);//фоновый процесс, который очищает кэш, актуализирует значения. В данном случае для чтения с консоли;
//            t2.start();
//
//            //отладка;
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
//            try {
//                server.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
    }
}