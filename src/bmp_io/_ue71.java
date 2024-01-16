package bmp_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class _ue71 {

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
	 * Generates array of pixelsums
	 * @param bmp source bmp
	 * @param kernel assumed to be 3x3
	 * @return array with pixelsum for each pixel
	 */
    private static PixelColor[][] getPixelSumsByKernel(BmpImage bmp, Double[][] kernel) {
        PixelColor[][] src = getPixels(bmp);
        int width = src.length; 
        int height = src[0].length;
        PixelColor[][] res = new PixelColor[width][height];
        for (int i = 0; i != width; i++) {
            for (int k = 0; k != height; k++) {
				// for each pixel: loop over our 3x3 kernel
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kk = -1; kk <= 1; kk++) {
                        int srcI = i + ki;
                        int srcK = k + kk; 
						PixelColor refColor = handleBorder(src, srcI, srcK);
						// map kernel indices back to bounds (-1/1) -> (0,2)
						int kernelI = ki + 1;
						int kernelK = kk + 1;
						// add product of multiplication of refColor and kernel value to our pixel result
						res[i][k] = new PixelColor(
							res[i][k].r + refColor.r * kernel[kernelI][kernelK],
							res[i][k].g + refColor.g * kernel[kernelI][kernelK],
							res[i][k].b + refColor.b * kernel[kernelI][kernelK]
						);
                    }
                }
            } 
        }
        return res;
    }

	private static PixelColor[][] lowpassFilter(BmpImage bmp, Double[][] kernel) {
		PixelColor sums = getPixelSumsByKernel(bmp, kernel);
		for (int i = 0; i < sums.length; i++) {
			for (int k = 0; k < sums[0].length; k++) {
				sums[i][k] = new PixelColor(
					sums[i][k].r / 9.0,
					sums[i][k].g / 9.0,
					sums[i][k].b / 9.0
				);
			}
		}
		return sums;
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
			PixelColor[][] sourceData = ; 
			// set data 
			setPixels(bmp, lowpassFilter(getPixels(bmp))); 
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