/**
 * Created by mmonsivais on 11/3/16.
 */
public class ThneedStore
{
  private static int inventory = 0;
  private static int $balance$ = 1000;

  public ThneedStore()
  {

  }

  synchronized int getInventory()
  {
    return inventory;
  }
}
