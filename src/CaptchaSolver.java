import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;


public class CaptchaSolver implements Runnable
{
    protected Client _client = null;
    protected String _captchaFilename = null;
    private Captcha _captcha = null;

    public CaptchaSolver(Client client, String captchaFilename)
    {
        this._client = client;
        this._captchaFilename = captchaFilename;
        this._captcha = new Captcha();
    }

    public Captcha getCaptcha()
    {
    	return this._captcha;
    }
    
    public void run()
    {
        try {
            // Put your CAPTCHA image file, file object, input stream,
            // or vector of bytes here:
            this._captcha = this._client.upload(this._captchaFilename);
            if (null != this._captcha) {
                System.out.println("CAPTCHA " + this._captchaFilename + " uploaded: " + this._captcha.id);

                // Poll for the uploaded CAPTCHA status.
                while (this._captcha.isUploaded() && !this._captcha.isSolved()) {
                    Thread.sleep(Client.POLLS_INTERVAL * 1000);
                    this._captcha = this._client.getCaptcha(this._captcha);
                }

                if (this._captcha.isSolved()) {
                    System.out.println("CAPTCHA " + this._captchaFilename + " solved: " + this._captcha.text);
                    // Report incorrectly solved CAPTCHA if neccessary.
                    // Make sure you've checked if the CAPTCHA was in fact
                    // incorrectly solved, or else you might get banned as
                    // abuser.
                    /*if (this._client.report(this._captcha)) {
                        System.out.println("CAPTCHA " + this._captchaFilename + " reported as incorrectly solved");
                    } else {
                        System.out.println("Failed reporting incorrectly solved CAPTCHA");
                    }*/
                } else {
                    System.out.println("Failed solving CAPTCHA");
                }
            }
        } catch (java.lang.Exception e) {
            System.err.println(e.toString());
        }
    }
}