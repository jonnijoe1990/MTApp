package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue33 {

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
	
	private static PixelColor[][] verticalDownSample(BmpImage source, int pixelsToSample) {
		// get data 
		PixelColor[][] data = getPixels(source); 
		for (int x = 0; x < data.length; x++) {
			int lastIndex = data[x].length - 1; 
			for (int y = 0; y < data[x].length; y += pixelsToSample + 1) { 
				int valueIndex = (y + pixelsToSample) > lastIndex ? lastIndex : y + pixelsToSample; 
				for (int k = 0; k < pixelsToSample; k++) {
					int overwriteIndex = y + k; 
					if (overwriteIndex <= lastIndex) {
						data[x][y+k] = data[x][valueIndex];
					}
				}

			}
		}
		return data; 
	}

	private static PixelColor[][] horizontalDownSample(BmpImage source, int pixelsToSample) {
		// get data 
		PixelColor[][] data = getPixels(source); 
		int lastIndex = data.length - 1; 
		for (int x = 0; x < data.length; x += pixelsToSample + 1) {
			for (int y = 0; y < data[x].length; y++) { 
				int valueIndex = (x + pixelsToSample) > lastIndex ? lastIndex : x + pixelsToSample; 
				for (int k = 0; k < pixelsToSample; k++) {
					int overwriteIndex = x + k; 
					if (overwriteIndex <= lastIndex) {
						data[x+k][y] = data[valueIndex][y];
					}
				}

			}
		}
		return data; 
	}


	public static void main(String[] args) throws IOException {
		

		// enter file name here 
		String fileName = "media_in/grating_H9.bmp";
		
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
			// modify bmp before write_bmp is called
			setPixels(bmp, horizontalDownSample(bmp, 16));
			BmpWriter.write_bmp(out, bmp);
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
