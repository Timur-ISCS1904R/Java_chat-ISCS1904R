package network;
import client.DataBase;


public interface Listener {
    void ConSucces(TCPCon tcpcon);
    void Receiv(TCPCon tcpcon,String s);
    void DisCon(TCPCon tcpcon);
    void Exep(TCPCon tcpcon,Exception e);

}
