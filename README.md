# JavaSynthesizer

JavaSynthesizer is a simple software synthesizer written in Java, featuring a graphical user interface and real-time audio synthesis using OpenAL via LWJGL. The project allows users to interactively control oscillator parameters and visualize waveforms.

## Features

- **Multiple Oscillators:** Three independent oscillators with selectable waveforms (Sine, Square, Saw, Triangle).
- **Parameter Controls:** Adjust tone offset and volume for each oscillator using mouse gestures.
- **Waveform Visualization:** Real-time waveform display for the mixed output of all oscillators.
- **Keyboard Input:** Play notes using your computer keyboard, mapped to musical frequencies.
- **Audio Output:** Real-time sound generation using OpenAL (LWJGL).

## Project Structure

- `src/Synth/`  
  Main source files for synthesizer logic, GUI, audio thread, and oscillator implementation.
- `src/Synth/utils/`  
  Utility classes for parameter handling, math functions, and reference wrappers.
- `lib/`  
  Required external libraries (LWJGL, OpenAL, jVST).
- `bin/`  
  Compiled class files.

## Getting Started

### Prerequisites

- Java 8 or higher
- LWJGL and OpenAL libraries (included in `lib/`)
- AWT/Swing compatible environment

### Build & Run

1. **Compile the project:**
   ```
   javac -cp "lib/*" -d bin src/Synth/*.java src/Synth/utils/*.java
   ```

2. **Run the synthesizer:**
   ```
   java -cp "bin:lib/*" Synth.MainClass
   ```

### Usage

- Use your keyboard to play notes (`z`, `s`, `x`, `d`, etc.).
- Adjust oscillator parameters by clicking and dragging on the controls.
- Select waveform types from the dropdown menu.
- View the mixed waveform in the right panel.

## Main Classes

- [`Synth.MainClass`](src/Synth/MainClass.java): Entry point of the application.
- [`Synth.SynthGUI`](src/Synth/SynthGUI.java): Main GUI and event handling.
- [`Synth.Osc`](src/Synth/Osc.java): Oscillator logic and controls.
- [`Synth.WaveViewer`](src/Synth/WaveViewer.java): Waveform visualization.
- [`Synth.AudioThread`](src/Synth/AudioThread.java): Real-time audio output.

## License

This project is for educational purposes.

## Credits

- [LWJGL](https://www.lwjgl.org/) for OpenAL bindings.
- Java Swing/AWT for GUI components.
