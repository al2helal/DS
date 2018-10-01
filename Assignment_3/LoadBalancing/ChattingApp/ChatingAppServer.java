import java.io.*;
import java.net.*;
import java.util.*;

public class ChatingAppServer {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(5278);
            SocketReceiver sr = new SocketReceiver(ss);
            sr.t.start();                                                      //Thread for receiving new client
        } catch (IOException ex) {
            System.out.println("Problem in serversocket.");
        }
    }
}
//Following class contain socket and its corresponding inputstream and outputstream
class Node {

    public Socket sc;
    public DataInputStream is;
    public DataOutputStream os;
    private String name;

    public Node(Socket s) {
        try {
            sc = s;
            is = new DataInputStream(s.getInputStream());
            os = new DataOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Problem in node constructor");
        }
    }
    public void setName(String s){
        name = s;
    }
    public String getName(){
        return name;
    }
}

class SocketReceiver implements Runnable {

    private final ServerSocket ss;
    public Thread t;
    private final List<Node> socketList;                           //Arraylist for saving socket of client

    public SocketReceiver(ServerSocket s) {
        ss = s;
        t = new Thread(this);
        socketList = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket sc = ss.accept();
                Node nd = new Node(sc);
                nd.os.writeUTF("Welcome to our chat room. Enter your name: ");
                nd.setName(nd.is.readUTF());
                socketList.add(nd);
                System.out.printf("%s has been added.\n", nd.getName());
                DataServe ds = new DataServe(nd, socketList);            //This class start a thread for each socket
                ds.t.start();                                               //to receive text and serve to all client
            }
        } catch (IOException e) {
            System.out.println("problem in adding client");
        }
    }
}

class DataServe implements Runnable {

    private final Node nd;
    public Thread t;
    private final List<Node> sl;

    public DataServe(Node n, List<Node> sls) {
        nd = n;
        t = new Thread(this);
        sl = sls;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = nd.is.readUTF();
                if (msg.equals("Bye")) {
                    nd.is.close();
                    nd.os.close();
                    nd.sc.close();
                    String tmp = nd.getName();
                    sl.remove(nd);
                    System.out.println(nd.getName()+" gone.");
                    if(sl.isEmpty()){
                        System.out.println("No client");
                    }
                    break;
                }
                for (Node n : sl) {                                                //Serving data to all client
                    if (n == nd) {                                                 //Except that who send this
                        continue;
                    }
                    n.os.writeUTF(nd.getName() + "-> " + msg);
                }
            }
        } catch (IOException e) {
            System.out.println("problem in data serving");
        }
    }
}
