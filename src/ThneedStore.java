/**
 * Created by mmonsivais on 11/3/16.
 */
public class ThneedStore
{
  private ServerMaster serverMaster;
  private static ThneedStore store;
  private static int inventory = 0;
  private static int $balance$ = 1000;

  public ThneedStore(ServerMaster serverMaster)
  {
   this.serverMaster = serverMaster;
  }

  public static ThneedStore getStore(ServerMaster serverMaster)
  {
    if(store == null) store = new ThneedStore(serverMaster);
    return store;
  }

  synchronized int getInventory()
  {
    return inventory;
  }
}
