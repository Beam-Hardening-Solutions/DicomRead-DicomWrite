/*
Author Luke Stowe
Author Ashley Deane
 */


import java.io.*;
import java.util.Arrays;
import java.util.Collections;


class Reader {
    public static void main(String[] argv) throws Exception {
        //Read from an input stream
        InputStream is = new BufferedInputStream(new FileInputStream("im3.dcm"));
        DataInputStream in = new DataInputStream(is);
        Byte [] bytes = new Byte[600000];
        Byte [] endOfhead1 = {(byte)0xe0,(byte)0x7f,(byte)0x10,(byte)0x00,(byte)0x4f,
                (byte)0x57,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x00};//512
        Byte [] endOfhead2 = {(byte)0xe0,(byte)0x7f,(byte)0x10,(byte)0x00,(byte)0x4f,
         (byte)0x57,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0x40,(byte)0x08,(byte)0x00};//520

        int pSize = 520;
        int positionToWrite=0;
        int pixelStart=0;


        //handle writing back to a file
        BufferedOutputStream o =new BufferedOutputStream(new FileOutputStream("im2.dcm"));
        DataOutputStream out = new DataOutputStream(o);
        int count = 0;
        try
        {
            while(in.available() > 0)// end of file + count size
            {
                bytes [count] = in.readByte();
                count++;
            }
            if(Collections.indexOfSubList(Arrays.asList(bytes),Arrays.asList(endOfhead2))+endOfhead2.length > 12)
            {
                 pixelStart = Collections.indexOfSubList(Arrays.asList(bytes),Arrays.asList(endOfhead2))+endOfhead2.length;
                 pSize = 520;
            }

            else
            {
                 pixelStart=Collections.indexOfSubList(Arrays.asList(bytes),Arrays.asList(endOfhead1))+endOfhead1.length;
                 pSize = 512;
            }

            Pixel [][]pixelData = new Pixel[pSize][pSize];
            positionToWrite=pixelStart;
            System.out.println("Pixel values start at : "+pixelStart);


            for(int i = 0; i < pSize ; i++)
            {
                for(int j = 0; j < pSize*2;j+=2) { //increment by two so dont merge wrong bytes

                    Pixel p = new Pixel(bytes[pixelStart+1],bytes[pixelStart]);
                    pixelData[i][j/2]=p;
                    pixelStart+=2;
                }

            }

            System.out.println(pixelData[250][300].getPixelValue());

            //test to change byte value from D to I
            bytes[128]=73;

            for(int i=0;i<4001;i++)
            {
                //write to new  image
                out.writeByte(bytes[i]);
            }


            // close stuff
            out.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }



    }
}