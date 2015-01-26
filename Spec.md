## Reading
We won't be able to read in bits directly in java need to read in bytes of 2 for a word essentially then use logical and operations to reverse the endian in order to make it readable for us. Endian only matters for us just to see the data.

example would be:

byte b = in.readByte();//read byte from input stream
byte endian = b&0x4096 //handle endian logical and with 2^12


out.writeByte(b);//order will be important when writing out

we should store the bytes in a byte array which is already an object in java for efficiency.

bytes are read in however pixels are stored in two bytes so we will have to merge the bytes perhaps in a short or int aswell as fix endian

-------------------------------------------

## Writing
Writing should be easy once we get reading working we will be using DataInputStream for input and DataOutputStream for output. If everything is ordered in a byte array where two bytes represent a pixel then we can just use and output stream to put these values into a file DICOM in this case.

Example for writing a pixel value to a dicom file:

Byte [] pixels;// Byte array with values will be 2d for matrix info
DataOutputStream out = new DataOutputStream(new File("new.dcm"));//output
out.write(Byte[0]);
out.wrtie(Byte[1]);

---------------------------------------------

##  Header File 
Going to be dealing with ascaii values the the bytes DICOM marks the start of the header which has patient information but it will also tell us some useful information such as the amount of pixels which will help us to build our data structure to hold the values.

------------------------------------------------


## Libraries 
java.io.*; // for input and output streams reading and writing the files.
------ Useful Methods -------
readFile()//reading bytes from file
writeFile()//writing bytes to file
fixEndian()// logical and to make readable
generateMatrix()// build matrix with logical anded values
printMatrix()//display matrix
phantomDetect()//edge detection

-------------------------------------------------

## Little Endian
2 bytes 512 or 520 in binary is 1000000000 so split into first 8 bits which are 8 zeros and leftover two bits which get put after to make it little endian
so 512 in little endian is 0x0002. 520 is 1000001000 which in little endian is 0x0802

------------------------------------------------

## Get past header to pixel steps
Find height and width for the image
Tags for header
Hex two numbers is a byte
Rows = 00 28 00 10(BigEndian) what we are looking for 28 00 10 00 (Tag)LE
Unsigned Short Integer"US" in word format size is two bytes = 55 53(big endian)(data type) value representation is always big endian.BE

Total of what we are looking for  28 00 10 00 55 53 next will be value length(32 bit unsigned int)(LE) then the value two bytes if the value length is 2 which is a word the size  so 2 byte first (LE)

example : 28 00 10 00 55 53 02 00 00 02 //512 rows
example : 28 00 10 00 55 53 02 00 08 02 //520 rows

Columns = 00 28 00 11(BigEndian) what we are looking for 28 00 11 00 (Tag)
example : 28 00 11 00 55 53 02 00 00 02 //512 columns
example : 28 00 11 00 55 53 02 00 08 02 //520 columns

To find end of header we are looking for value representation(VR) "OW" other word
Tag = e0 7F 10 00 little endian
VR is OW = 4f 57 Big endian
Next two bytes for the OW are always 00 00
Then next 4 bytes are the size of the pixel data little endian(520 rows * 520 columns * 2 bytes deep) 2 bytes make a pixel is = 540800(decimal)= hex 00084080  big endian 00 08 40 80 little endian 80 40 08 00

512 * 512 * 2 =524288 or hex 00080000 so looking for e0 7f 10 00 4f 57 00 00 00 00 08 00

The total is e0 7f 10 00 4f 57 00 00 80 40 08 00 next two bytes are pixel number one in little endian.

-----------------------------------------------------

So to check end of header we will need to write a method that takes in byte array and another byte array contaning the bytes above we want to find just the index in the original array where it occurs.

To check for this we will check the array as array list to see if it contains the smaller array list of the end of header information.

we need to find a byte sequence for row size column size and then the index of the end of header or OW other word value representation.


so from e0 above plus 12 will be the first pixel data.

--------------------------------------------------------

## Pixel

The pixel are little endian format this is effectively backwards to how we would read it in this means that we have to read in two bytes and perform a bit wise operation to put them the right way round for us to interpurt (2nd byte first). We will have to mask off some unused bits using AND operations.

We will make a class Pixel to encapsulate this idea. It will take in two bytes as parameters in the constructor and will either perform bitwise operation here or in a separate method depending on how we wish to access pixel information.

A data structure either array or 2d array will be used to store all Pixel objects. 

When the necessary corrections have been made to the corrupt pixel values we will need a method to reverse the bitwise operations to return two bytes. These bytes will then be returned in the correct order(Little Endian) into the array containing the header file this allows for a new DICOM image to be correctly generated.

Pixel class has bytes first second if pixelValue has not been changed then these can just be pulled out and written back into the array of everything with "second" being written first to accomadate little endian

However if the value has been changed we will need a bitwise operator. first we copy the int then we mask off 8 bits and write that into a byte then mask off the opposite 8 bits in the copy and store in a byte then these will have to be written in the write order back out to the main array.
----------------------------------------------------------

## Plans To Be Completed ASAP
- Pixel Class
- Methods for bitwise operations(Pixel Class)
- Edge Detection
- Inspection of Corrupt Data
- Algorithm to fix corrupt data


---------------------------------------------------------
