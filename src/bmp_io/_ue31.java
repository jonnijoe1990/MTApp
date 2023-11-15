package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class _ue31 {
	
	public static void main(String[] args) throws IOException {
		BmpImage horizontalBmp = null; 
		BmpImage verticalBmp = null; 	

		// enter file paths w/o endings here 
		String horizontalFile = "media_in/grating_H9";
		String verticalFile = "media_in/grating_V3"; 
		
		try {
			// try to get bmpImage objects from both bmp files 
			InputStream horizontalIn = new FileInputStream(horizontalFile + ".bmp");
			InputStream verticalIn = new FileInputStream(verticalFile + ".bmp");
			horizontalBmp = BmpReader.read_bmp(horizontalIn); 
			verticalBmp = BmpReader.read_bmp(verticalIn); 
		} catch (IOException e) {
			e.printStackTrace();
		}

		OutputStream horizontalOut = new FileOutputStream(horizontalFile + ".ascii");
		OutputStream verticalOut = new FileOutputStream(verticalFile + ".ascii");

		// create data strings for both files
		String horizontalDataString = ""; 
		String verticalDataString = ""; 
		
		try {
			// horizontal
			PixelColor[][] horizontalPixels = new PixelColor[horizontalBmp.image.getWidth()][horizontalBmp.image.getHeight()]; 
			for (int x = 0; x != horizontalBmp.image.getWidth(); x++) {
				for (int y = 0; y != horizontalBmp.image.getHeight(); y++) {
					// set pixel data 
					horizontalPixels[x][y] = horizontalBmp.image.getRgbPixel(x, y);
					// set horizontal data String for first row
					if (y == 0) {
						horizontalDataString += horizontalPixels[x][y].r + " " + horizontalPixels[x][y].g + " " + horizontalPixels[x][y].b;
						if (x < horizontalBmp.image.getWidth() - 1) {
							// append new line, except for last entry
							horizontalDataString += "\n";
						}
					} 
				}
			}
			horizontalOut.write(horizontalDataString.getBytes()); // write data 
			horizontalOut.flush(); // ensure file is fully written 

			// vertical
			PixelColor[][] verticalPixels = new PixelColor[verticalBmp.image.getWidth()][verticalBmp.image.getHeight()]; 
			for (int x = 0; x != verticalBmp.image.getWidth(); x++) {
				for (int y = 0; y != verticalBmp.image.getHeight(); y++) {
					// set pixel data 
					verticalPixels[x][y] = verticalBmp.image.getRgbPixel(x, y);
					// set vertical data String for first col
					if (x == 0) {
						verticalDataString += verticalPixels[x][y].r + " " + verticalPixels[x][y].g + " " + verticalPixels[x][y].b;
						if (y < verticalBmp.image.getHeight() - 1) {
							// append new line, except for last entry
							verticalDataString += "\n";
						}
					} 
				}
			}
			verticalOut.write(verticalDataString.getBytes()); // write data 
			verticalOut.flush(); // ensure file is fully written 
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close output streams
			horizontalOut.close();
			verticalOut.close();
		}
		/*
		 * Periodenlänge ist horizontal 63px, vertikal 69px
		*/
	}
}
