import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatingAppClient
{
    public static void main(String[] args) throws  IOException
    {
        try
        {
            Socket sc = new Socket("127.0.0.1", 5278);
            Scanner in = new Scanner(System.in);
            DataInputStream is = new DataInputStream(sc.getInputStream());
            DataOutputStream os = new DataOutputStream(sc.getOutputStream());
            System.out.print(is.readUTF());
            String name = in.next();
            os.writeUTF(name);
            ClientR cR = new ClientR(sc);                //Thread for reading
            ClientW cW = new ClientW(sc);                //Thread for writing
            cR.t.start();
            cW.t.start();
        }
        catch(IOException e)
        {
            System.out.println("Server is off!!!");
        }
    }
}
class  ClientR implements Runnable
{
    Thread t;
    String msg;
    DataInputStream is;
    Socket sc;
    ClientR(Socket sc) throws IOException
    {
        this.sc = sc;
        t = new Thread(this);
        is = new DataInputStream(sc.getInputStream());
    }
    public void run()
    {
        try
        {
            while(true)
            {
                msg = is.readUTF();
                System.out.println(msg);
            }
        }
        catch(IOException e)
        {
            System.out.println("Good bye");
        }
    }
}
class  ClientW implements Runnable
{
    Thread t;
    Scanner msg;
    DataOutputStream os;
    private Socket sc;
    ClientW(Socket sc) throws IOException
    {
        this.sc = sc;
        t = new Thread(this);
        os = new DataOutputStream(sc.getOutputStream());
        msg = new Scanner(System.in);
    }
    public void run()
    {
        try
        {
            while(true)
            {
                String st = msg.nextLine();
                os.writeUTF(st);
                if(st.equals("Bye"))          //If any client want to exit
                {
                    break;
                }
            }
            os.close();
            sc.close();
        }
        catch(IOException e)
        {
            System.out.println("Server cannot read as it has been off/shut down unexpectedly.");
        }
    }
}
