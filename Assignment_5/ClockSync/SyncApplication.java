import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SyncApplication
{
    public static void main(String[] args)
    {
        ArrayList<Socket> workerList = new ArrayList<>();
        try
        {
            ServerSocket serverSocket = new ServerSocket(3666);
            while(true)
            {
                Socket socket = new Socket();
                socket = serverSocket.accept();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(workerList);
                objectOutputStream.close();  
                workerList.add(socket);
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
        }
    }
}
class Sender implements Runnable
{
    Socket socket;
    ObjectInputStream objectInputStream;
    DataOutputStream dataOutputStream;
    Sender(Socket socket)
    {
        objectInputStream = new Socket(socket.getInputStream());
        dataOutputStream = new Socket(socket.getOutputStream());
        this.socket = socket;
    }
    public void run()
    {
        while(true)
        {
            Socket wSocket = objectInputStream.readObject();
            DataOutputStream dO = new Socket(wSocket.getOutputStream());
            dO.writeUTF("Time");
            dO.close();
            dataOutputStream.writeInt(new Socket(wSocket.getInputStream()).readInt());
            dataOutputStream.close();
            objectInputStream.close();
        }
    }
}