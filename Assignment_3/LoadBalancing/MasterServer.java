import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MasterServer
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(5278);
            SocketReceiver socketReceiver = new SocketReceiver(serverSocket);
            socketReceiver.t.start();                                                 //Thread for receiving new client
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
        }
    }
}
//Following class contain socket and its corresponding inputstream and outputstream
class Node
{
    public Socket socket;
    public DataInputStream inputStream;
    public DataOutputStream outputStream;

    public Node(Socket socket)
    {
        try
        {
            this.socket = socket;
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex)
        {
            System.out.println("Problem in node constructor");
        }
    }
}

class SocketReceiver implements Runnable
{
    TakeInputThread takeInputThread;
    SendOutputThread sendOutputThread;
    ServerSocket serverSocket;
    public Thread t;
    ArrayList<Node> socketNodeArrayList;
    ArrayList<Worker> workerArrayList = new ArrayList<>();
    ArrayList<NodeAndInputOutput> nodeAndInputOutputArrayList;
    public SocketReceiver(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
        t = new Thread(this);
        socketNodeArrayList = new ArrayList<>();
        Worker worker1 = new Worker(10);
        worker1.t.start();
        Worker worker2 = new Worker(10);
        worker2.t.start();
        Worker worker3 = new Worker(10);
        worker3.t.start();
        workerArrayList.add(worker1);
        workerArrayList.add(worker2);
        workerArrayList.add(worker3);


        takeInputThread = new TakeInputThread(socketNodeArrayList, workerArrayList, nodeAndInputOutputArrayList);
        takeInputThread.t.start();
        sendOutputThread = new SendOutputThread(workerArrayList);
        sendOutputThread.t.start();
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("client added");
                Node node = new Node(socket);
                //nd.os.writeUTF("Welcome to our chat room. Enter your name: ");
                //nd.setName(nd.is.readUTF());
                socketNodeArrayList.add(node);
                //System.out.printf("%s has been added.\n", nd.getName());
                Thread.sleep(1000);                                             //to receive text and serve to all client
            }
        }
        catch (IOException e)
        {
            System.out.println("problem in adding client");
        }
        catch(InterruptedException ex)
        {
            System.out.println(ex.toString());
        }
        
    }
}

class NodeAndInputOutput
{
    Node node;
    double input;
    double output;
    NodeAndInputOutput(Node node, double input)
    {
        this.node = node;
        this.input = input;
    }
}

class TakeInputThread implements Runnable
{
    private Node nd;
    public Thread t;
    ArrayList<Node> socketNodeArrayList;
    ArrayList<Worker> workerArrayList;
    ArrayList<NodeAndInputOutput> nodeAndInputOutputArrayList;

    public TakeInputThread(ArrayList<Node> socketNodeArrayList, ArrayList<Worker> workerArrayList, ArrayList<NodeAndInputOutput> nodeAndInputOutputArrayList)
    {
        t = new Thread(this);
        this.socketNodeArrayList = socketNodeArrayList;
        this.workerArrayList = workerArrayList;
        this.nodeAndInputOutputArrayList = nodeAndInputOutputArrayList;
    }
    Worker getLowWorker()
    {
        double load = 1.0;
        double newLoad;
        Worker lowLoadWorker = null;
        for(Worker worker: workerArrayList)
        {
            newLoad = (worker.inputQueue.size()/worker.queueSize);
            if(newLoad < load)
            {
                lowLoadWorker = worker;
                load = newLoad;
            }
        }
        return lowLoadWorker;
    }
    @Override
    public void run()
    {
        double number;
        try
        {
            while(true)
            {
                System.out.println("TakeInputThread running");
                Iterator<Node> iterator = socketNodeArrayList.iterator();
                Node node;
                while(iterator.hasNext())
                {
                    node = iterator.next();
                    System.out.println("wait for input");
                    number = node.inputStream.readDouble();
                    System.out.println("input taken from client =" + number);
                    NodeAndInputOutput nodeAndInputOutput = new NodeAndInputOutput(node, number);
                    getLowWorker().inputQueue.add(nodeAndInputOutput);
                    System.out.println("input added to inputQueue");
                }
                Thread.sleep(1000);
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        
    }
}

class SendOutputThread implements Runnable
{
    Thread t;
    ArrayList<Worker> workerArrayList;
    NodeAndInputOutput nodeAndInputOutput;
    SendOutputThread(ArrayList<Worker> workerArrayList)
    {
        this.workerArrayList = workerArrayList;
        t = new Thread(this);
    }
    public void run()
    {
        try
        {
            while(true)
            {
                for(Worker w: workerArrayList)
                {
                    //System.out.println("sender is running" + workerArrayList.size());
                    while(!w.outputQueue.isEmpty())
                    {
                        //System.out.println("outputQueue not empty");
                        nodeAndInputOutput = w.outputQueue.poll();
                        nodeAndInputOutput.node.outputStream.writeUTF(""+nodeAndInputOutput.input +":"+ nodeAndInputOutput.output);
                    }
                }
                Thread.sleep(1000);
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
}

class Worker implements Runnable
{
    int queueSize;
    ArrayDeque<NodeAndInputOutput> inputQueue = new ArrayDeque<>();
    ArrayDeque<NodeAndInputOutput> outputQueue = new ArrayDeque<>();
    Thread t;
    NodeAndInputOutput nodeAndInputOutput;
    Worker(int queueSize)
    {
        this.queueSize = queueSize;
        t = new Thread(this);
    }
    public void run()
    {
        try
        {
            while(true)
            {
                System.out.println("worker is running");
                while(!inputQueue.isEmpty())
                {
                    nodeAndInputOutput = inputQueue.poll();
                    double output = Math.pow(nodeAndInputOutput.input, 2);
                    nodeAndInputOutput.output = output;
                    outputQueue.add(nodeAndInputOutput);
                    System.out.println("output added");
                }
                Thread.sleep(1000);
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
}