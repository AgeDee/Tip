package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageCommunicationClientClass {
    private String msgServerIp;
    private int msgServerPort;

    private Socket clientCommunicationSocket;
    private InputStreamReader clientCommunicationMessageInput;
    private OutputStreamWriter clientCommunicationMessageOutput;

    private BufferedReader reader,commandReader;

    MessageCommunicationClientClass(String msgServerIp, int msgServerPort){
        this.msgServerIp = msgServerIp;
        this.msgServerPort = msgServerPort;
    }

    public void startMsgClient() {
        try {
            clientCommunicationSocket = new Socket(msgServerIp, msgServerPort);
            clientCommunicationMessageInput = new InputStreamReader(clientCommunicationSocket.getInputStream());
            commandReader = new BufferedReader(clientCommunicationMessageInput);
            clientCommunicationMessageOutput =
                    new OutputStreamWriter(clientCommunicationSocket.getOutputStream(),"UTF-8");


        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void sendMessage(String message){
        String messageToSend = message + "\n";
        try {
            clientCommunicationMessageOutput.write(messageToSend, 0, messageToSend.length());
            clientCommunicationMessageOutput.flush();
        }catch(Exception ex){
            System.out.println("Exception in sendMessage function");
            System.out.println(ex);
        }
    }

    public String receiveMessage(){
        String message = "";
        try{
            message = commandReader.readLine();
            return message;
        }catch(IOException ex){
            System.out.println("Exception in MessageCommunicationClientClass::receiveMessage");
            System.out.println(ex);
        }

        return message;
    }

    public void closeMsgClient(){
        try {
            clientCommunicationSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}