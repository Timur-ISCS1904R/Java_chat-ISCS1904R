package network;
import client.DataBase;
import client.GUI;

import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import java.nio.*;
public class TCPCon {
    private Socket socket;
    private Thread thread;
    private BufferedReader in;
    private BufferedWriter wr;
    private Listener listner;
    private DataBase db;
    public TCPCon(Listener listner,String ip,int port) throws IOException{
        this(listner,new Socket(ip,port));
    }
    public TCPCon(Listener listner,Socket socket) throws IOException {
        this.socket = socket;
        this.listner=listner;
        socket.getInputStream();
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        wr=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listner.ConSucces(TCPCon.this);
                    while(!thread.isInterrupted()){
                        String m=in.readLine();
                        listner.Receiv(TCPCon.this,m);
                    }
                } catch (IOException e) {
listner.Exep(TCPCon.this,e);
                } finally{
listner.DisCon(TCPCon.this);

                }
            }
        });
        thread.start();
    }
    public synchronized void disconnect(){
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listner.Exep(TCPCon.this,e);
        }
    }
    public synchronized void sendMes(String m) {
        try {
            wr.write(m+"\n\r");
            wr.flush();
        } catch (IOException e) {
            listner.Exep(TCPCon.this,e);
            disconnect();
        }
    }
    @Override
    public String toString() {
        return "ip адрес:"+socket.getInetAddress()+":"+socket.getPort() ;
    }

}
