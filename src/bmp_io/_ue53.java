package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue53 {

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

	/*
	 * calculates the result of a 3x3 and a 3x1 matrix
	 */
	private static PixelColor matrixMulti(double[][] twoDim, double[] oneDim) {
		double[] res = new double[3];
		for (int i = 0; i < 3; i++) {
			res[i] = 0;
			for (int k = 0; k < 3; k++) {
				res[i] += twoDim[i][k] * oneDim[k];
			}
		}
		return new PixelColor((int) res[0], (int) res[1], (int) res[2]); 
	}

	private static PixelColor ycbrToRgb(PixelColor px) {
		// create constant conversion matrix
		double[][] multMatrix = new double[][]{
			{1, 	0, 			1.403},
			{1, 	-0.344, 	-0.714},
			{1, 	1.773, 		0}
		};
		// create matrix for source pixel
		double[] pxMatrix = new double[]{
			(double) px.r, 
			(double) px.g - 128, 
			(double) px.b - 128
		};

		// get result of multiplication return
		return matrixMulti(multMatrix, pxMatrix);
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
					data[x][y] = ycbrToRgb(data[x][y]); 
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
