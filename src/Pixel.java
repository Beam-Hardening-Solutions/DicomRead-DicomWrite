/**
 * Created by lukestowe on 01/12/14.
 */
public class Pixel {
    private Byte first;
    private Byte second;
    private int secondIn;
    private int firstIn;
    private int pixelValue;

    Pixel(Byte b1,Byte b2)
    {
        first=b1;
        second=b2;
        firstIn = b1 & 0xffff;
        secondIn = b2 & 0xff;
        pixelValue = firstIn << 8 | secondIn;

    }
    int getPixelValue()
    {
        return pixelValue;
    }
    void setPixelValue(int newValue)
    {
        pixelValue=newValue;
    }




}
