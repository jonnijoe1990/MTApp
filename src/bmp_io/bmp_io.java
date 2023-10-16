package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import wav_io.WavFileException;

public final class bmp_io {
	
	public static void main(String[] args) throws IOException {
		
		String inFilename = null;
		String outFilename = null;
		
		int height = 0;
		int width = 0;
		int bits = 0;
		
		// Klasse zum Lesen und Schreiben der Farbwerte eines Pixels
		PixelColor pc = null;
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
			
			// Implementierung
			width = bmp.image.getWidth(); 
			height = bmp.image.getHeight(); 
			
			for (int x = 0; x != width; x++) 
				for (int y = 0; y != height; y++) {
					// Zugriff auf Pixel mit bmp.image.getRgbPixel(x, y);
					PixelColor pOut = bmp.image.getRgbPixel(x, y);
					System.out.printf("R: %-3d ", pOut.r);
					System.out.printf("G: %-3d ", pOut.g);
					System.out.printf("B: %-3d ", pOut.b); 
					System.out.println(); 
				}
			
			// Setzen eines Pixels mit bmp.image.setRgbPixel(x, y, pc);
				
			
			
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
		
		// Implementierung
		
		
		// Speicherung 
		try {
			BmpWriter.write_bmp(out, bmp);
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
