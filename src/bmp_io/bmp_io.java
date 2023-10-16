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
		
		int hight = 0;
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
			// Zugriff auf Pixel mit bmp.image.getRgbPixel(x, y);
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
