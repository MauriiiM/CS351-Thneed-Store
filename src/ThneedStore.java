/**
 * Implements the Singleton Pattern hence wh the getter and the self object are static;
 *
 */
public class ThneedStore
{
  private ServerMaster serverMaster;
  private static ThneedStore store;
  private int inventory;
  private int $balance$;

  public ThneedStore(ServerMaster serverMaster)
  {
    this.serverMaster = serverMaster;
    inventory = 0;
    $balance$ = 1000;
  }

  public static ThneedStore getStore(ServerMaster serverMaster)
  {
    if (store == null) store = new ThneedStore(serverMaster);
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
    if (sold <= inventory)
    {
      inventory -= sold;
      $balance$ += unitCost * sold;
      serverMaster.broadcast(time + ": inventory=" + inventory + " : treasury=" + $balance$);
    }
  }
}
