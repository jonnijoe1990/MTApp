package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue72 {


	/**
	 * set the pixels of a BmpImage to a 2d pixel array
	 * @param bmp
	 * @param pixels
	 */
	private static void setPixels(BmpImage bmp, PixelColor[][] pixels) {
		for(int x = 0; x != pixels.length; x++) {
			for(int y = 0; y != pixels[x].length; y++) {
				bmp.image.setRgbPixel(x, y, pixels[x][y]); 
			}
		}
	}

	/**
	 * set the pixels of a BmpImage to a 2d pixel array
	 * @param bmp
	 * @return get a 2d pixel array from a BmpImage
	 */
	private static PixelColor[][] getPixels(BmpImage bmp) {
		PixelColor[][] ret = new PixelColor[bmp.image.getWidth()][bmp.image.getHeight()]; 
		for(int x = 0; x != bmp.image.getWidth(); x++) {
			for(int y = 0; y != bmp.image.getHeight(); y++) {
				ret[x][y] = bmp.image.getRgbPixel(x, y); 
			}
		}
		return ret; 
	}

	/**
	 * 
	 * @param src
	 * @param i
	 * @param k
	 * @return value of index i,k in src, or a default color if out of bounds 
	 */
	private static PixelColor handleBorder(PixelColor[][] src, int i, int k) {
		PixelColor def = new PixelColor(127,127,127);
		if (i >= 0 && k >= 0 && i < src.length && k < src[0].length) 
			return src[i][k];
		else
			return def;
	}

	/**
	 * Generates a 3x3 grid, containing the components of the product of kernel and src for each pixel
	 * @param src 2d-PixelColor-array
	 * @param kernel assumed to be 3x3
	 * @return 5d-double-array (2 for pixels, 2 for kernel grid, 1 for components)
	 */
    private static double[][][][][] getPixelProductGridByKernel(PixelColor[][] src, double[][] kernel) {
        int width = src.length; 
        int height = src[0].length;
        double[][][][][] res = new double[width][height][3][3][3];
        for (int i = 0; i != width; i++) {
            for (int k = 0; k != height; k++) {
				// for each pixel: loop over our 3x3 kernel, sum up results
                for (int ki = -1; ki <= 1; ki++) {
					for (int kk = -1; kk <= 1; kk++) {
                        int srcI = i + ki;
                        int srcK = k + kk; 
						PixelColor refColor = handleBorder(src, srcI, srcK);
						// map kernel indices back to bounds (-1/1) -> (0,2)
						int kernelI = ki + 1;
						int kernelK = kk + 1;
						// add product of multiplication of refColor and kernel value to our pixel result
						res[i][k][kernelI][kernelK][0] = (refColor.r * kernel[kernelI][kernelK]); 
						res[i][k][kernelI][kernelK][1] = (refColor.g * kernel[kernelI][kernelK]); 
						res[i][k][kernelI][kernelK][2] = (refColor.b * kernel[kernelI][kernelK]); 
                    }
                }
            } 
        }
        return res;
    }


	/**
	 * apply Low-pass filter to bmp
	 * @param pixels
	 * @param kernel
	 */
	private static void applyLpFilter(BmpImage bmp) {
		double[][] kernel = new double[][]{
			{1, 1, 1},
			{1, 1, 1},
			{1, 1, 1}
		};
		// get pixels from bmp
		PixelColor[][] pixels = getPixels(bmp);
		// get 3x3 grids of components of pixels, with double values for each kernel field
		double[][][][][] gridsByPixels = getPixelProductGridByKernel(pixels, kernel);
		for (int i = 0; i < gridsByPixels.length; i++) {
			for (int k = 0; k < gridsByPixels[0].length; k++) {
				double[][][] gridOfComponents = gridsByPixels[i][k];
				// init rgb sums at 0
				double r, g, b;
				r = g = b = 0;
				// sum up grid
				for (int ki = 0; ki < 3; ki++) {
					for (int kk = 0; kk < 3; kk++) {
						r += gridOfComponents[ki][kk][0];
						g += gridOfComponents[ki][kk][1];
						b += gridOfComponents[ki][kk][2];
					}
				}
				// divide by number of sumands, write in pixels
				pixels[i][k] = new PixelColor(
					(int) (r / 9.0),
					(int) (g / 9.0),
					(int) (b / 9.0)
				);
			}
		}
		setPixels(bmp, pixels);
	}

	/**
	 * set contrast and brightness for a single pixel
	 * @param px
	 * @param k
	 * @param h
	 * @return adjusted pixel
	 */
	private static PixelColor setContrastAndBrightness(PixelColor px, double k, double h) {
		int r = (int) (k * (px.r - 128) + 128 + h);
		int g = (int) (k * (px.g - 128) + 128 + h);
		int b = (int) (k * (px.b - 128) + 128 + h);
		return new PixelColor(r, g, b);
	}

	/**
	 * subtract a pixel from another
	 * @param value
	 * @param sub
	 * @return result pixel
	 */
	private static PixelColor getDiffPixel(PixelColor value, PixelColor sub) {
		return new PixelColor(
			value.r - sub.r,
			value.g - sub.g,
			value.b - sub.b
		);
	}

	// implementation

	
	/**
	 * map pixelcolor to its bounds [0, 255]
	 * @param src
	 * @return mapped pixelColor
	*/
	private static PixelColor mapToBounds(PixelColor src) {
		return new PixelColor(
			Math.min(255, Math.max(0,src.r)),
			Math.min(255, Math.max(0,src.g)),
			Math.min(255, Math.max(0,src.b))
		);
	}

	private static void applyDiffToFilter(BmpImage bmp) {
		// get original pixels
		PixelColor[][] pixels = getPixels(bmp);
		// apply lowpass filter
		applyLpFilter(bmp);
		// get filtered pixels
		PixelColor[][] filtered = getPixels(bmp);
		for (int i = 0; i < pixels.length; i++) {
			for (int k = 0; k < pixels[0].length; k++) {
				/*
				 * subtract filtered from original
				 * set contrast 0.8, brightness +100
				 * map to [0,255] bounds
				*/
				pixels[i][k] = mapToBounds(setContrastAndBrightness(
					getDiffPixel(pixels[i][k], filtered[i][k]), 
					0.8, 
					50
				));
			}
		}
		setPixels(bmp, pixels);
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
			applyDiffToFilter(bmp); 
			BmpWriter.write_bmp(out, bmp);
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		/*
		 * Eine Konstrast kleiner 1 bewirkt eine Stauchung der Verteilung um den Mittelwert der Helligkeit (255/2). 
		 * Eine Kontrast größer 1 bewirkt eine Streckung der Verteilung, sodass die Frequenz der Helligkeitsveränderungen ansteigt.
		 */
	}
}