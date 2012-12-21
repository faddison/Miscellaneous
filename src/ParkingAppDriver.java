
public class ParkingAppDriver
{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)
	{
		try
		{
			parkingapptest app = new parkingapptest();
			app.setUp();
			app.testParkingapptest();
			app.tearDown();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
