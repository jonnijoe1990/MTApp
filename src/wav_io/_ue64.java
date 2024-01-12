package wav_io;

import java.io.IOException;

public class _ue64 {

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

short[] filter = new short[samples];

for (int i = 0; i < samples; i++) {
	// cast to int, cuts of decimal part
	int index = i - numChannels;
	// prevent out of bounds
	if (index > 0) 
		filter[i] = (short) (readWavFile.sound[index] * 0.45);
}
	
	

int sign = -1; // use low pass by default 
if (args.length == 3) 
	// use high pass if a 3rd argument is emitted
	sign = 1;


for (int i = 0; i < samples; i++) {
	readWavFile.sound[i] =
		// prevent out of bounds
		(short) Math.max(
			-32768, 
			Math.min(
				32767,
				0.5 * readWavFile.sound[i] + sign * filter[i]
			)
		);
} 

/*
 * Der Frequenzverlauf, der eine "kurve" nach unten beinhält ist der Lowpass-Filter (Niedrige Frequenzen dringen durch).
 * Analog dazu ist der Frequenzverlauf mit der steigenden Kurve ein Highpass-Filter
 */

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
