/**
 * Implements the Singleton Pattern hence wh the getter and the self object are static;
 *
 */
public class ThneedStore
{
  private ServerMaster serverMaster;
  private static ThneedStore store;
  private int inventory = 0;
  private int $balance$ = 1000;

  public ThneedStore(ServerMaster serverMaster)
  {
    this.serverMaster = serverMaster;
  }

  public static ThneedStore getStore(ServerMaster serverMaster)
  {
    if (store == null) store = new ThneedStore(serverMaster);
    return store;
  }

  public static ThneedStore getStore()
  {
    return store;
  }

  synchronized int getInventory()
  {
    return inventory;
  }

  synchronized float getBalance()
  {
    return $balance$;
  }

  synchronized void buyThneeds(int bought, float unitCost, String time)
  {
    if (bought * unitCost <= $balance$)
    {
      inventory += bought;
      $balance$ -= unitCost * bought;
      serverMaster.broadcast(time + ": inventory=" + inventory + " : treasury=" + $balance$);
    }
  }

  synchronized void sellThneeds(int sold, float unitCost, String time)
  {
    System.out.println("FFFFFFFFFFFFFFFfff");
    if (sold <= inventory)
    {
      inventory -= sold;
      $balance$ += unitCost * sold;
      serverMaster.broadcast(time + ": inventory=" + inventory + " : treasury=" + $balance$);
    }
  }
}
