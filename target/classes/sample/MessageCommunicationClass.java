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

public class MessageCommunicationClass {


    public void startMsgServer(String msgServerIp, int msgServerPort) throws Exception{

        ServerSocket serverSocket = new ServerSocket(msgServerPort,50, InetAddress.getByName(msgServerIp));

        while (true) {
            ServerMessageCommunicationClass messageServer = new ServerMessageCommunicationClass(serverSocket.accept());
        }

    }


}


class ServerMessageCommunicationClass extends Thread {
    Socket clientCommunicationSocket;
    InputStreamReader clientCommunicationMessageInput;
    OutputStreamWriter clientCommunicationMessageOutput;

    BufferedReader commandReader;

    private boolean exit = false;

    //Zmienne zarządzające serwerem VoIP


    public ServerMessageCommunicationClass(Socket connectedSocket){
        try {
            clientCommunicationSocket = connectedSocket;
            clientCommunicationMessageInput = new InputStreamReader(clientCommunicationSocket.getInputStream());
            commandReader = new BufferedReader(clientCommunicationMessageInput);
            clientCommunicationMessageOutput =
                    new OutputStreamWriter(clientCommunicationSocket.getOutputStream(), "UTF-8");
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

                            //Akcja na przyciśnięcie Buttona "Odbierz"
                            connectionWindowController.getCallButton.setOnAction((event) -> {
                                //Uruchamiamy server voip
                                new Thread(() -> {
                                    System.out.println("Voip Server started!!!");
                                    MainController.voipConnection.receiveCall();
                                }).start();

                                try {
                                    String messageToSend = "OK" + "\n";
                                    clientCommunicationMessageOutput.write(messageToSend, 0, messageToSend.length());
                                    clientCommunicationMessageOutput.flush();
                                } catch (IOException ex) {
                                    System.out.println("Exception in ServerMessageCommunicationClass:: CONNECT message");
                                }

                                stage.close();
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

                        }catch(Exception ex){
                            System.out.println("Exception in ServerMessageCommunication:CONNECT");
                            System.out.println(ex);
                        }
                        });

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
                        System.out.println("Komenda Rozłącz");
                        MainController.voipConnection.stopServer();
                        MainController.voipConnection.stopCapture();

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
