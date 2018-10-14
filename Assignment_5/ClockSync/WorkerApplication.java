import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.DataInputStream;

public class WorkerAppliction
{
    public static void main(String[] args) 
    {
        WorkerProcess workerProcess = new WorkerProcess();
        workerProcess.connect();
    }
}
class WorkerProcess
{
    ArrayList<Socket> workersArray = new ArrayList<>();
    Response response;
    Clock clock;
    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    ArrayList<Socket> workersSockets = new ArrayList<>();
    Sender sender;
    void connect()
    {
        socket = new Socket("127.0.0.1",3666);
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        workersSockets = objectInputStream.getObjectInputFilter();
        objectInputStream.close();
        response = new Response();
        clock = new Clock(response);
        clock.t.start();

        sender = new Sender(response, workersSockets,objectOutputStream,socket);
    }
}
class Response
{
    int flag; // 0 neutral, 1 requesting mode, 2 receiving mode
    int time;
}
class Sender implements Runnable
{
    ArrayList<Socket> workersSockets;
    Thread t;
    Response response;
    Socket socket;
    Sender(Response response, ArrayList<Socket> workersSockets,Socket socket)
    {
        this.response = response;
        this.workersSockets = workersSockets;
        this.t = new Thread(this);
        this.socket = socket;
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }
    public void run()
    {
        while(true)
        {
            int i = new Random().nextInt(workersSockets.size());
            Socket s = workersSockets.get(i);
            if(s.isClosed())
            {
                workersSockets.remove(s);
            }
            else
            {
                dataOutputStream.wirteOb
            }
        }
    }
}
class Receiver
{
    ArrayList<Socket> workersSockets;
    Response response;
    Thread t;
    Receiver(Response response, ArrayList<Socket> workersSockets)
    {
        this.workersSockets = workersSockets;
        this.response = response;
        this.t = new Thread(this);
    }
    public void run()
    {
        while(true)
        {
            //wait for response
        }
    }
}

class Clock implements Runnable
{
    Thread t;
    Response response;
    int currentTime;
    Clock(Response response)
    {
        this.response = response;
        this.t = new Thread(this);
    }
    public void run()
    {
        while(true)
        {
            try
            {
                if(currentTime < resOrReq.time)
                {
                    currentTime += resOrReq.time + 1;
                    resOrReq.time = 0;
                }
                resOrReq.flag = new Random().nextInt(2);
                System.out.println(currentTime);
                TimeUnit.MILLISECONDS.sleep(1000);
                currentTime += 6;
            }
            catch(Exception ex)
            {
                System.out.println(ex.toString());
            }
        }
    }
}