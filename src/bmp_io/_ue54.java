package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue54 {

	private static PixelColor[][] getPixels(BmpImage bmp) {
		PixelColor[][] ret = new PixelColor[bmp.image.getWidth()][bmp.image.getHeight()]; 
		for(int x = 0; x != bmp.image.getWidth(); x++) {
			for(int y = 0; y != bmp.image.getHeight(); y++) {
				ret[x][y] = bmp.image.getRgbPixel(x, y); 
			}
		}
		return ret; 
	}
	
	private static String getHistogramOutString(PixelColor[][] pixels) {
		// create result array with slots for every value for y [0-255]
		int[] res = new int[256];
		for (int i=0; i < pixels.length; i++) {
			for (int k=0; k < pixels[i].length; k++) {
				int y = pixels[i][k].r; 
				// increment counter for value of y
				res[y]++;
			}
		}
		String txtContent = "";
		for (int val : res) {
			// append counter and newline char
			txtContent += val + "\n";
		}
		return txtContent;
	}

	public static void main(String[] args) throws IOException {
		
		String inFilename = null;
		String outFilename = null;
		
		BmpImage bmp = null;

		if (args.length < 1) {
			System.out.println("At least one filename specified  (" + args.length + ")"); 
			System.exit(0);
		}
		
		
		// ****************************************************
		// Implementierung bei einem Eingabeparamter
		inFilename = args[0];
		
		try {
			InputStream in = new FileInputStream(inFilename);
			bmp = BmpReader.read_bmp(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		if (args.length == 1) 
			System.exit(0);
	
		// ***************************************************
	    // Implementierung bei Ein- und Ausgabeparameter (speichern in eine Datei (2. Argument))

		outFilename = args[1];
		OutputStream out = new FileOutputStream(outFilename);


		// Speicherung 
		try {
			// get data 
			PixelColor[][] data = getPixels(bmp);
			// get out string 
			String outString = getHistogramOutString(data);
			// write out string to file 
			out.write(outString.getBytes()); 
			// ensure file is fully written
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
