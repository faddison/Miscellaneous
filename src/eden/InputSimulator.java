package eden;

public class InputSimulator {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		// TODO Auto-generated method stub
		
		Thread.sleep(3000);
		for (int i = 0; i < 100; i++)
		{
//			robot.keyPress(KeyEvent.VK_K);
			Thread.sleep(500);
			System.out.println("i");
//			robot.keyRelease(KeyEvent.VK_K);
		}

	}

}
