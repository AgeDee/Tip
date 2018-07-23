package sample;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class VoipConnection {

    //Zmienne używane przez część kodu, przechwytującą głos z mikrofonu

    boolean stopaudioCapture = false;
    ByteArrayOutputStream byteOutputStreamClient;
    AudioFormat adFormat;
    TargetDataLine targetDataLineClient;
//    AudioInputStream InputStreamClient;
//    SourceDataLine sourceLineClient;

    //Zmienne używane przez część kodu, odbierającą głos od nadawcy

    AudioInputStream InputStreamServer;
    SourceDataLine sourceLineServer;
    boolean microphoneON = false;

    //Wspólne zmienne
    DatagramSocket appSocket;

    //Zmienne servera VoIP
    boolean serverStopped = false;





    public VoipConnection(){
        //captureAudio();
        //receiveCall();


    }

    public void captureAudio() {
        try {
            adFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
            targetDataLineClient = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLineClient.open(adFormat);
            targetDataLineClient.start();

            Thread captureThread = new Thread(new CaptureThread());
            captureThread.start();

            microphoneON = true;
            System.out.println("Microphone turned on !!");
        } catch (Exception e) {
            StackTraceElement stackEle[] = e.getStackTrace();
            for (StackTraceElement val : stackEle) {
                System.out.println(val);
            }
            System.exit(0);
        }
    }

    public void captureAudio(String receiverIpAddress, int receiverPort) {
        try {
            adFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
            targetDataLineClient = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLineClient.open(adFormat);
            targetDataLineClient.start();

            Thread captureThread = new Thread(new CaptureThread(receiverIpAddress, receiverPort));
            captureThread.start();

            microphoneON = true;
            System.out.println("Microphone turned on !!");
        } catch (Exception e) {
            StackTraceElement stackEle[] = e.getStackTrace();
            for (StackTraceElement val : stackEle) {
                System.out.println(val);
            }
            System.exit(0);
        }
    }

    public void stopCapture(){
        stopaudioCapture = true;
        microphoneON = false;
        targetDataLineClient.close();
        if(!appSocket.isClosed()){
            appSocket.close();
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 20000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

    class CaptureThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        String receiverIpAddress = null;
        int receiverPort = 9999;

        CaptureThread(String receiverIpAddress, int receiverPort){
            this.receiverIpAddress = receiverIpAddress;
            this.receiverPort = receiverPort;
        }

        CaptureThread(){}

        public void run() {

            byteOutputStreamClient = new ByteArrayOutputStream();
            stopaudioCapture = false;
            try {
                //DatagramSocket clientSocket = new DatagramSocket(8786);
                //DatagramSocket clientSocket = new DatagramSocket(8786, InetAddress.getByName("127.0.0.1"));
                //appSocket = new DatagramSocket(8786, InetAddress.getByName("127.0.0.1"));
                InetAddress IPAddress = InetAddress.getByName(receiverIpAddress);
                while (!stopaudioCapture) {
                    int cnt = targetDataLineClient.read(tempBuffer, 0, tempBuffer.length);
                    if (cnt > 0) {
                        DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, IPAddress, receiverPort);
                        //clientSocket.send(sendPacket);
                        appSocket.send(sendPacket);
                        byteOutputStreamClient.write(tempBuffer, 0, cnt);
                    }
                }
                byteOutputStreamClient.close();
            } catch (Exception e) {
                System.out.println("CaptureThread::run()\n" + e);
            }
        }
    }

    //Kod serwerowy
    public void receiveCall(){
        try {
            serverStopped = false;
            appSocket = new DatagramSocket(9999, InetAddress.getByName(LoginController.user_ip));
            //appSocket = new DatagramSocket(9999, InetAddress.getByName("192.168.0.15"));
            byte[] receiveData = new byte[10000];
            while (!serverStopped) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                //serverSocket.receive(receivePacket);
                if(!appSocket.isClosed()) {
                    appSocket.receive(receivePacket);
                    if (!microphoneON) {
                        //Włączenie nasłuciwania przez mikrofon oraz ustawienie flagi microphoneON na true wewnątrz funkcji
                        captureAudio(receivePacket.getAddress().getHostAddress(), receivePacket.getPort());

                    }
                    System.out.println("RECEIVED: " + receivePacket.getAddress().getHostAddress() + " " + receivePacket.getPort());
                    try {
                        byte audioData[] = receivePacket.getData();
                        InputStream byteInputStream = new ByteArrayInputStream(audioData);
                        AudioFormat adFormat = getAudioFormat();
                        InputStreamServer = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
                        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
                        sourceLineServer = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                        sourceLineServer.open(adFormat);
                        sourceLineServer.start();
                        Thread playThread = new Thread(new PlayThread());
                        playThread.start();
                    } catch (Exception e) {
                        System.out.println(e);
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Socket closed in receiveCall");
        }
    }

    public void stopServer(){
        serverStopped = true;
        if(!appSocket.isClosed()){
            appSocket.close();
        }
    }

    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        public void run() {
            try {
                int cnt;
                while ((cnt = InputStreamServer.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        sourceLineServer.write(tempBuffer, 0, cnt);
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }


}
