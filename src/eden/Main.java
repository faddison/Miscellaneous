package eden;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Main 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Robot robot = new Robot();
		Thread.sleep(3000);
		for (int i = 0; i < 10; i++)
		{
//			robot.keyPress(KeyEvent.VK_K);
			robot.mouseMove(1337, 112);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			Thread.sleep(100);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			Thread.sleep(500);
			System.out.println("tab pressed");
//			robot.keyRelease(KeyEvent.VK_K);
		}
		
		
	}

}
