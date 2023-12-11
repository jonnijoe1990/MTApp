package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue52 {

	private static void setPixels(BmpImage bmp, PixelColor[][] pixels) {
		for(int x = 0; x != pixels.length; x++) {
			for(int y = 0; y != pixels[x].length; y++) {
				bmp.image.setRgbPixel(x, y, pixels[x][y]); 
			}
		}
	}

	private static PixelColor[][] getPixels(BmpImage bmp) {
		PixelColor[][] ret = new PixelColor[bmp.image.getWidth()][bmp.image.getHeight()]; 
		for(int x = 0; x != bmp.image.getWidth(); x++) {
			for(int y = 0; y != bmp.image.getHeight(); y++) {
				ret[x][y] = bmp.image.getRgbPixel(x, y); 
			}
		}
		return ret; 
	}

	private static double[][] rgbToYcbcrMatrix = {
		{0.299, 0.587, 0.114},
		{-0.169, -0.331, 0.5},
		{0.5, -0.419, -0.081}
	};

	private static PixelColor rgbToY(PixelColor px) {
		return new PixelColor(
			(int) (px.r * rgbToYcbcrMatrix[0][0]), 
			(int) (px.g * rgbToYcbcrMatrix[0][1]) + 128, 
			(int) (px.b * rgbToYcbcrMatrix[0][2]) + 128 
		);
	}

	private static PixelColor rgbToCb(PixelColor px) {
		return new PixelColor(
			(int) (px.r * rgbToYcbcrMatrix[1][0] + 128), 
			(int) (px.g * rgbToYcbcrMatrix[1][1] + 128), 
			(int) (px.b * rgbToYcbcrMatrix[1][2] + 128) 
		);
	}

	private static PixelColor rgbToCr(PixelColor px) {
		return new PixelColor(
			(int) (px.r * rgbToYcbcrMatrix[2][0] + 128), 
			(int) (px.g * rgbToYcbcrMatrix[2][1] + 128), 
			(int) (px.b * rgbToYcbcrMatrix[2][2] + 128) 
		);
	}

	private static PixelColor rgbToYcbr(PixelColor px) {
		return new PixelColor(
			(int) (px.r * rgbToYcbcrMatrix[0][0] + px.g * rgbToYcbcrMatrix[0][1] + px.b * rgbToYcbcrMatrix[0][2]),
			(int) (px.r * rgbToYcbcrMatrix[1][0] + px.g * rgbToYcbcrMatrix[1][1] + px.b * rgbToYcbcrMatrix[1][2]) + 128,
			(int) (px.r * rgbToYcbcrMatrix[2][0] + px.g * rgbToYcbcrMatrix[2][1] + px.b * rgbToYcbcrMatrix[2][2]) + 128

		);
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
			// convert
			for (int x = 0; x != data.length; x++) {
				for (int y = 0; y != data[x].length; y++) {
					data[x][y] = rgbToCb(data[x][y]); 
				}
			}
			// set data 
			setPixels(bmp, data); 
			BmpWriter.write_bmp(out, bmp);
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
