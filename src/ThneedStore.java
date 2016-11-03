/**
 * Created by mmonsivais on 11/3/16.
 */
public class ThneedStore
{
  private static int inventory;

  synchronized int getInventory()
  {
    return inventory;
  }
}
