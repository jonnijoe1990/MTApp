package wav_io;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;

public class _ue63 {

	public static void main(String[] args) {
	
		int samples = 0;
		int validBits = 0;
		long sampleRate = 0;
		long numFrames = 0; 
		int numChannels = 0;

		String inFilename = null;
		String outFilename = null;
		
		WavFile readWavFile = null;
		
		if (args.length < 1) {
			try { throw new WavFileException("At least one filename specified  (" + args.length + ")"); }
			catch (WavFileException e1) { e1.printStackTrace(); }
		}
	
		
		// ********************************************************
		// Implementierung bei einem Eingabeparameter

		inFilename=args[0];
		
		try {
			readWavFile = WavFile.read_wav(inFilename);
			
			// headerangaben
			numFrames = readWavFile.getNumFrames(); 
			
			// Anzahl der Kanaäle (mono/stereo)
			numChannels = readWavFile.getNumChannels();
			
			// Anzahl Abtastpunkte
			samples = (int) numFrames*numChannels;
			
			// Bitszahl
			validBits = readWavFile.getValidBits();
			
			// Abtastrate 
			sampleRate = readWavFile.getSampleRate();

// Implementierung print 

short[] echo = new short[samples];
double n = (Integer.parseInt(args[2]) / 1000.0 * sampleRate);

for (int i = 0; i < samples; i++) {
	// cast to int, cuts of decimal part
	int index = (int) (i - numChannels * n);
	// prevent channel hop for stereo 
	if (numChannels == 2)
		if (i % 2 == 0 && index % 2 != 0 || i % 2 != 0 && index % 2 == 0) 
			// increment to next index if channels are unequal
			index++;
	// prevent out of bounds
	if (index >= 0) 
		echo[i] = (short) (readWavFile.sound[index] * 0.6);
}
System.out.println("testsd");
for (int i = 0; i < samples; i++) {
	readWavFile.sound[i] =
		// prevent out of bounds
		(short) Math.max(
			-32768, 
			Math.min(
				32767,
				readWavFile.sound[i] + echo[i]
			)
		);
}

//
			
		} catch (IOException | WavFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (args.length == 1) 
			System.exit(0);
			
		
		// ***********************************************************
		// Implementierung bei Ein-und Ausgabeparameter (Speichern der Ausgabedatei)
		
		outFilename=args[1];
		
		
		// Implementierung
		
		
		// Speicherung
		try {
			WavFile.write_wav(outFilename, numChannels, numFrames, validBits, sampleRate, readWavFile.sound);
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		}
	}
}
