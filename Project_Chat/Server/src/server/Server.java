package server;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import network.Listener;
import network.TCPCon;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;
public class Server implements Listener{
    TCPCon server;
    public static void main(String[] args) {
        new Server();
    }
    private ArrayList<TCPCon> con=new ArrayList<>();
    private Server()  {
        System.out.println("Сервер запущен!");
        try {
            ServerSocket sersoc=new ServerSocket(2525);
            while(true){
                try{
                    new TCPCon(this,sersoc.accept());
                }catch (IOException e){
                    System.out.println("Ошибка"+e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public synchronized void ConSucces(TCPCon tcpcon) {
        con.add(tcpcon);
        SendAllCon("Поключился "+tcpcon+"\n\r"+"Колличество людей в чате: "+con.size()+"\n");
    }
    private String getNick() {
        String s=null;
        return s;
    }
    @Override
    public synchronized void Receiv(TCPCon tcpcon, String s) { SendAllCon(s);
    }
    @Override
    public synchronized void DisCon(TCPCon tcpcon) {
        con.remove(tcpcon);
        SendAllCon("Отключился "+tcpcon+"\n\r"+"Колличество людей в чате: "+con.size()+"\n");
    }


    @Override
    public synchronized void Exep(TCPCon tcpcon, Exception e) {
        System.out.println("Ошибка: "+e);
    }
    private void SendAllCon(String s){
        System.out.println(s);
        int l=con.size();
        for (int i = 0; i <l ; i++) con.get(i).sendMes(s);
    }
}
