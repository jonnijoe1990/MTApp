## Instructions 
1. Compile using VSCode (will throw error), bin folder is created 

    alternatively use 'javac -d bin $(find src -name "*.java")'

2. example terminal usage with parameters: 

    wav_io: java --class-path=bin wav_io.wave_io media_in/Musik_LSG.wav media_out/Musik_LSG.wav
    
    bmp_io: java --class-path=bin bmp_io.bmp_io media_in/Detail_LSG.bmp media_out/Detail_LSG.bmp 