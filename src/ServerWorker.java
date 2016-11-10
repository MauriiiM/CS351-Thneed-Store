import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker extends Thread
{
  private Socket client;
  private PrintWriter clientWriter;
  private BufferedReader clientReader;
  private ServerMaster serverMaster;

  public ServerWorker(ServerMaster serverMaster, Socket client)
  {
    this.serverMaster = serverMaster;
    this.client = client;

    try
    {
      //          PrintWriter(OutputStream out, boolean autoFlushOutputBuffer)
      clientWriter = new PrintWriter(client.getOutputStream(), true);
    }
    catch (IOException e)
    {
      System.err.println("Server Worker: Could not open output stream");
      e.printStackTrace();
    }

    try
    {
      clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }
    catch (IOException e)
    {
      System.err.println("Server Worker: Could not open input stream");
      e.printStackTrace();
    }
  }

  //Called by ServerMaster
  public void send(String msg)
  {
    System.out.println("ServerWorker.send(" + msg + ")");
    clientWriter.println(msg);
  }

  public void run()
  {
    String typedInput;
    String val[];
    while (true)
    {
      try
      {
        typedInput = clientReader.readLine();
        System.out.println("typedInput = " + typedInput);
        if (typedInput.charAt(0) == 'b')
        {
          val = typedInput.split(" ");
          serverMaster.getStore().buyThneeds(Integer.parseInt(val[1]), Float.parseFloat(val[2]), val[3]);
        }
        else if (typedInput.charAt(0) == 's')
        {
          val = typedInput.split(" ");
          serverMaster.getStore().sellThneeds(Integer.parseInt(val[1]), Float.parseFloat(val[2]), val[3]);
        }
        else if(typedInput.equals("q"))
        {
          serverMaster.removeServerWorker(this);
        }
      }
      catch (IOException e)
      {
        System.err.println(e);
      }
    }
  }

}