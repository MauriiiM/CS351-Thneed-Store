/**
 * Created by mmonsivais on 11/3/16.
 */
public class ThneedStore
{
  private static ServerMaster serverMaster;
  private static ThneedStore store;
  private static int inventory = 0;
  private static int $balance$ = 1000;

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

  synchronized static int getInventory()
  {
    return inventory;
  }

  synchronized static float getBalance()
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
