import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{
  /**
   * @todo make false for turn in
   */
  private static final boolean DEBUG = true;

  private Socket clientSocket;
  private PrintWriter write;
  private BufferedReader reader;
  private long startNanoSec;
  private Scanner keyboard;
  private ClientListener listener;

  private int quantity;
  private float unitPrice;

  private float $balance$inStore;
  private volatile int thneedsInStore;
  private volatile boolean running = true;

  public Client(String host, int portNumber)
  {
    $balance$inStore = ThneedStore.getStore().getBalance();
    thneedsInStore = ThneedStore.getStore().getInventory();
    startNanoSec = System.nanoTime();
    System.out.println("Starting Client: " + timeDiff());

    keyboard = new Scanner(System.in);

    while (!openConnection(host, portNumber))
    {
    }

    listener = new ClientListener();
    System.out.println("Client(): Starting listener = : " + listener);
    listener.start();

    listenToUserRequests();

    closeAll();
  }

  private boolean openConnection(String host, int portNumber)
  {
    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      return false;
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host + " on port " + portNumber);
      e.printStackTrace();
      return false;
    }

    try
    {
      write = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open output stream");
      e.printStackTrace();
      return false;
    }
    try
    {
      reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open input stream");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private void listenToUserRequests()
  {
    String[] val;
    while_label:
    while (true)
    {
      String typedInput = keyboard.nextLine();
      if (typedInput == null) continue;
      if (typedInput.length() < 1) continue;
      val = typedInput.split(" ");

      char c = typedInput.charAt(0);
      switch (c)
      {
        case 'b':
          quantity = Integer.parseInt(val[1]);
          unitPrice = Float.parseFloat(val[2]);

          System.out.println(quantity);
          System.out.println($balance$inStore);
          if (quantity * unitPrice <= $balance$inStore)
          {
            write.println("b " + quantity + " " + unitPrice + " " + timeDiff());
          }
          break;
        case 's':
          quantity = Integer.parseInt(val[1]);
          unitPrice = Float.parseFloat(val[2]);

          if (quantity <= thneedsInStore)
          {
            write.println("s " + quantity + " " + unitPrice+ " " + timeDiff());
          }
          break;
        case 'i':
          System.out.printf("There are %d thneeds in the store \n", thneedsInStore);
          break;
        case 'q':
          running = false;
          break while_label;
      }
      write.println(typedInput);
    }
  }

  public void closeAll()
  {
    System.out.println("Client.closeAll()");

    if (write != null) write.close();
    if (reader != null)
    {
      try
      {
        reader.close();
        clientSocket.close();
      }
      catch (IOException e)
      {
        System.err.println("Client Error: Could not close");
        e.printStackTrace();
      }
    }

  }

  private String timeDiff()
  {
    long namoSecDiff = System.nanoTime() - startNanoSec;
    double secDiff = (double) namoSecDiff / 1_000_000_000.0;
    return String.format("%.6f", secDiff);

  }

  /**
   * @param args [server name] [server port]
   */
  public static void main(String[] args)
  {

    String host = null;
    int port = 0;

    try
    {
      if (DEBUG)
      {
        host = InetAddress.getLocalHost().getHostName();
        port = 5555;
      }
      else
      {
        host = args[0];
        port = Integer.parseInt(args[1]);
      }
      if (port < 1) throw new Exception();
    }
    catch (Exception e)
    {
      System.out.println("Usage: Client hostname portNumber");
      System.exit(0);
    }
    new Client(host, port);

  }

  class ClientListener extends Thread
  {
    public void run()
    {
      System.out.println("ClientListener.run()");
      while (running)
      {
        read();
      }
      if (DEBUG) System.out.println("client exited");
    }

    private void read()
    {
      try
      {
        System.out.println("Client: listening to socket");
        String msg = reader.readLine();
        if (msg.startsWith("Thneeds:"))
        {
          int idxOfNum = msg.indexOf(':') + 1;
          int n = Integer.parseInt(msg.substring(idxOfNum));
          thneedsInStore = n;
          System.out.println("Current Inventory of Thneeds (" + timeDiff() + ") = " + thneedsInStore);
        }
        else if (msg.startsWith("You just bought "))
        {
          System.out.println("Success: " + msg);
        }
        else if (msg.startsWith("Error"))
        {
          System.out.println("Failed: " + msg);
        }
        else
        {
          System.out.println("Unrecognized message from Server(" + timeDiff() + ") = " + msg);
        }

      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}