import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class ServerMaster
{
  private ServerSocket serverSocket;
  private LinkedList<ServerWorker> allConnections = new LinkedList<>();
  private ThneedStore store;

  public ServerMaster(int portNumber)
  {
    store = ThneedStore.getStore(this); //will create the store because it's using Singleton Pattern
    try
    {
      serverSocket = new ServerSocket(portNumber);
    }
    catch (IOException e)
    {
      System.err.println("Server error: Opening socket failed.");
      e.printStackTrace();
      System.exit(-1);
    }

    waitForConnection(portNumber);
  }

  public ThneedStore getStore()
  {
    return store;
  }

  public synchronized void removeWorker(ServerWorker worker)
  {
    allConnections.remove(worker);
  }

  public void waitForConnection(int port)
  {
    String host = "";
    try
    {
      host = InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
    while (true)
    {
      System.out.println("ServerMaster(" + host + "): waiting for Connection on port: " + port);
      try
      {
        Socket client = serverSocket.accept();
        ServerWorker worker = new ServerWorker(this, client);
        worker.start();
        System.out.println("ServerMaster: *********** new Connection");
        allConnections.add(worker);
        worker.send("ServerMaster says hello!");
        worker.send("$" + store.getInventory() + " " + store.getBalance());
      }
      catch (IOException e)
      {
        System.err.println("Server error: Failed to connect to client.");
        e.printStackTrace();
      }
    }
  }

  public void broadcast(String s)
  {
    for (ServerWorker workers : allConnections)
    {
      workers.send(s);
    }
  }

  public void removeServerWorker(ServerWorker serverWorker)
  {
    allConnections.remove(allConnections.indexOf(serverWorker));
//    System.out.println("removed " + serverWorker);
  }

  public static void main(String args[])
  {
    //Valid port numbers are 1024 through 65535.
    //  ports under 1024 are reserved for system services http, ftp, etc.
    int port = 5555; //default
    if (args.length > 0)
    {
      try
      {
        port = Integer.parseInt(args[0]);
        if (port < 1) throw new Exception();
      }
      catch (Exception e)
      {
        System.out.println("Usage: ServerMaster portNumber");
        System.exit(0);
      }
    }

    new ServerMaster(port);
  }
}