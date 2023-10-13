package wav_io;

import java.io.IOException;

public class wave_io {
	
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
			
			for (int i = 0; i < samples; i++) {
				System.out.println(readWavFile.sound[i]);
			}

			// Implementierung
			// Zugriff auf die einzelne Samples mit readWavFile.sound[i]
			
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
