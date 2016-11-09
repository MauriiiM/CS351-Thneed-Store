/**
 * Created by mmonsivais on 11/3/16.
 */
public class ThneedStore
{
  private static ThneedStore store;
  private static int inventory = 0;
  private static int $balance$ = 1000;

  public ThneedStore()
  {

  }

  public static ThneedStore getStore()
  {
    if(store == null) store = new ThneedStore();
    return store;
  }

  synchronized int getInventory()
  {
    return inventory;
  }
}
