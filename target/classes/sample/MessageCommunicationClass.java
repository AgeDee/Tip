package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static sample.MainController.mainMessageClient;
import static sample.MainController.voipConnection;

public class MessageCommunicationClass {

    public static String callerIP;

    public void startMsgServer(String msgServerIp, int msgServerPort) throws Exception{

        ServerSocket serverSocket = new ServerSocket(msgServerPort,50, InetAddress.getByName(msgServerIp));

        while (true) {
            ServerMessageCommunicationClass messageServer = new ServerMessageCommunicationClass(serverSocket.accept());
        }

    }


}


class ServerMessageCommunicationClass extends Thread {
    UserDAO userDAO = new UserDAO();
    BlockedUserDAO blockedUserDAO = new BlockedUserDAO();

    Socket clientCommunicationSocket;
    InputStreamReader clientCommunicationMessageInput;
    OutputStreamWriter clientCommunicationMessageOutput;

    BufferedReader commandReader;

    private boolean exit = false;

    //Loader okna rozmowy potrzebny do zatrzymania timera
    public static FXMLLoader loaderOfCallWindow;




    public ServerMessageCommunicationClass(Socket connectedSocket){
        try {
            clientCommunicationSocket = connectedSocket;
            clientCommunicationMessageInput = new InputStreamReader(clientCommunicationSocket.getInputStream());
            commandReader = new BufferedReader(clientCommunicationMessageInput);
            clientCommunicationMessageOutput =
                    new OutputStreamWriter(clientCommunicationSocket.getOutputStream(), "UTF-8");
            MainController.recipientIp = connectedSocket.getInetAddress().getHostAddress();


            start();
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void run(){
        try{
            exit = false;
            while(!exit){
                String msg = commandReader.readLine();
                switch (msg) {
                    case "CONNECT":
                    {
//                        //Uruchamiamy server voip
//                        new Thread(() -> {
//                            System.out.println("Voip Server started!!!");
//                            MainController.voipConnection.receiveCall();
//                        }).start();

//                        String messageToSend = "OK" + "\n";
//                        clientCommunicationMessageOutput.write(messageToSend,0,messageToSend.length());
//                        clientCommunicationMessageOutput.flush();
                        boolean ifRejected = false;

                        List<BlockedUser> blockedUsersResult = blockedUserDAO.getByUserId(userDAO.findByUserIpAddress(LoginController.user_ip).getUserId());
                        for(BlockedUser u : blockedUsersResult){
                            if(u.getBlockedId() == userDAO.findByUserIpAddress(MainController.recipientIp).getUserId()){
                                ifRejected = true;
                            }
                        }


                        if(!ifRejected) {
                            Platform.runLater(() -> {

                                try {

                                    //Otwieranie okna informującego o tym, że ktoś dzwoni
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("connectionWindow.fxml"));
                                    Parent root = loader.load();
                                    Stage stage = new Stage();
                                    stage.setTitle("Próba połączenia");
                                    Scene scene = new Scene(root);
                                    scene.getStylesheets().add("sample/style.css");
                                    stage.setScene(scene);

                                    ConnectionWindowController connectionWindowController = loader.getController();
                                    connectionWindowController.setCallerLogin(userDAO.findByUserIpAddress(MainController.recipientIp).getLogin());

                                    //Akcja na przyciśnięcie Buttona "Odbierz"
                                    connectionWindowController.getCallButton.setOnAction((event) -> {
                                        //Uruchamiamy server voip
                                        new Thread(() -> {
                                            System.out.println("Voip Server started!!!");
                                            voipConnection.receiveCall();
                                        }).start();

                                        try {
                                            String messageToSend = "OK" + "\n";
                                            clientCommunicationMessageOutput.write(messageToSend, 0, messageToSend.length());
                                            clientCommunicationMessageOutput.flush();
                                        } catch (IOException ex) {
                                            System.out.println("Exception in ServerMessageCommunicationClass:: CONNECT message");
                                        }

                                        stage.close();

                                        FXMLLoader callWindowLoader = new FXMLLoader(getClass().getResource("callWindow.fxml"));
                                        loaderOfCallWindow = callWindowLoader;
                                        Parent rootForCallWindow = null;
                                        try {
                                            rootForCallWindow = callWindowLoader.load();
                                        } catch (IOException e) {
                                            System.out.println("Exception in CONNECT request:MessageCommunicationClass");
                                            System.out.println(e);
                                        }
                                        Stage stageForCallWindow = new Stage();
                                        stageForCallWindow.setTitle("Połączenie");
                                        Scene sceneForCallWindow = new Scene(rootForCallWindow);
                                        sceneForCallWindow.getStylesheets().add("sample/style.css");
                                        stageForCallWindow.setScene(sceneForCallWindow);

                                        CallWindowController controller = callWindowLoader.getController();
                                        //Ta funkcja szuka w bazie podanej nazwy, musimy mieć funkcje co przerabia ip na odpowiadający mu nick z bazy
                                        //controller.setTargetUser(clientCommunicationSocket.getInetAddress().getHostAddress());
                                        controller.setTargetUser(userDAO.findByUserIpAddress(MainController.recipientIp).getLogin());
                                        controller.startTimer();

                                        controller.endCallButton.setOnAction(e -> {
                                            stageForCallWindow.close();

                                            controller.stopTimer();

                                            //Rozłączanie po kliknięciu krzyżyka oraz ustawienie flagi microphoneON na false wewnatrz funkcji
                                            MainController.voipConnection.stopServer();
                                            MessageCommunicationClientClass messageClient = new MessageCommunicationClientClass(clientCommunicationSocket.getInetAddress().getHostAddress(), 8888);
                                            messageClient.startMsgClient();
                                            messageClient.sendMessage("CLOSE_WINDOW");
                                            MainController.voipConnection.stopCapture();
                                            messageClient.closeMsgClient();
                                        });

                                        stageForCallWindow.setOnCloseRequest(eventForCallWindow -> {
                                            System.out.println("Zakończenie połączenia");

                                            controller.stopTimer();

                                            //Rozłączanie po kliknięciu krzyżyka oraz ustawienie flagi microphoneON na false wewnatrz funkcji
                                            MainController.voipConnection.stopServer();
                                            MessageCommunicationClientClass messageClient = new MessageCommunicationClientClass(clientCommunicationSocket.getInetAddress().getHostAddress(), 8888);
                                            messageClient.startMsgClient();
                                            messageClient.sendMessage("CLOSE_WINDOW");
                                            MainController.voipConnection.stopCapture();
                                            messageClient.closeMsgClient();


                                        });
                                        MainController.callWindowStage = stageForCallWindow;
                                        stageForCallWindow.show();


                                    });

                                    connectionWindowController.rejectCallButton.setOnAction((event) -> {
                                        try {
                                            String messageToSend = "REJECT" + "\n";
                                            clientCommunicationMessageOutput.write(messageToSend, 0, messageToSend.length());
                                            clientCommunicationMessageOutput.flush();
                                        } catch (IOException ex) {
                                            System.out.println("Exception in ServerMessageCommunicationClass:: CONNECT message");
                                        }

                                        stage.close();
                                    });


                                    stage.show();

                                } catch (Exception ex) {
                                    System.out.println("Exception in ServerMessageCommunication:CONNECT");
                                    System.out.println(ex);
                                }
                            });
                        } else{
                            try {
                                String messageToSend = "REJECT" + "\n";
                                clientCommunicationMessageOutput.write(messageToSend, 0, messageToSend.length());
                                clientCommunicationMessageOutput.flush();
                            } catch (IOException ex) {
                                System.out.println("Exception in ServerMessageCommunicationClass:: blocked user rejected message");
                            }
                        }

                    }
                    break;
                    case "OK":
                    {
                        System.out.println("Komenda OK");
                    }
                    break;
                    case "REJECT":
                    {
                        System.out.println("Komenda Odrzuć");
                    }
                    break;
                    case "DISCONNECT":
                    {
                        //clientCommunicationDataOutput.write("221 Thank you for using NiceFTP\n",0,"221 Thank you for using NiceFTP\n".length());
                        //clientCommunicationDataOutput.flush();
                        Platform.runLater(() ->{
                            System.out.println("Komenda Rozłącz");
                            CallWindowController callWindowController = loaderOfCallWindow.getController();
                            callWindowController.stopTimer();
                            MainController.callWindowStage.close();
                            voipConnection.stopServer();
                            voipConnection.stopCapture();
                        });

                    }
                    break;
                    case "CLOSE_WINDOW":
                    {
                        Platform.runLater(() ->{
                            CallWindowController callWindowController = loaderOfCallWindow.getController();
                            callWindowController.stopTimer();
                            MainController.callWindowStage.close();
                            voipConnection.stopServer();
                            voipConnection.stopCapture();
                            MainController.mainMessageClient.closeMsgClient();
                        });

                    }
                    break;
                    case "QUIT":
                    {
                        System.out.println("Komenda Wyłącz Server Komunikacji");
                        exit = true;

                    }
                    break;
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }


    }
}
