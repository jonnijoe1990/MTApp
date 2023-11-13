package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue36 {

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

	private static PixelColor reduceColorBits(PixelColor source, int exp) {
		double doubleExp = Math.pow(2, exp); 
		int intExp = (int) doubleExp; 
		// perform int devision with exp
		// multiply the result with exp for compensation of brightness
		// cast the result to int
		source.r = (int) (source.r / intExp * doubleExp); 
		source.g = (int) (source.g / intExp * doubleExp); 
		source.b = (int) (source.b / intExp * doubleExp); 
		return source; 
	}
	public static void main(String[] args) throws IOException {
		

		// enter file name here 
		String fileName = "media_in/Detail_LSG.bmp";
	
		BmpImage bmp = null;

		try {
			InputStream in = new FileInputStream(fileName);
			bmp = BmpReader.read_bmp(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		OutputStream out = new FileOutputStream(fileName);

		try {
			// get data 
			PixelColor[][] data = getPixels(bmp); 
			// reduce bits
			for (int x = 0; x != data.length; x++) {
				for (int y = 0; y != data[x].length; y++) {
					data[x][y] = reduceColorBits(data[x][y], 6);
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
