package Synth;

import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import Synth.utils.Utils;

public class SynthGUI {

    private static final HashMap<Character, Double> KEY_FREQUENCIES = new HashMap<>(); // key on keyboard to frequency

    private boolean shouldGenerate;
    private final Osc[] oscillators = new Osc[3];

    private final WaveViewer waveViewer = new WaveViewer(oscillators);

    private final JFrame frame = new JFrame("Synth GUI");
    private final AudioThread audioThread = new AudioThread(() -> {
        if (!shouldGenerate) {
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for (int i = 0; i < AudioThread.BUFFER_SIZE; ++i) {
            double d = 0;
            for (Osc o : oscillators) {
                d += o.nextSample() / oscillators.length; // (1 +0.5) / 2 = 0.75
            }
            s[i] = (short) (Short.MAX_VALUE * d);

        }
        return s;
    });
    private final KeyAdapter keyAdapter = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            if (!KEY_FREQUENCIES.containsKey(e.getKeyChar())) {
                return;
            }

            if (!audioThread.isRunning()) {
                for (Osc o : oscillators) {
                    o.setFrequency(KEY_FREQUENCIES.get(e.getKeyChar()));
                }
                shouldGenerate = true;
                audioThread.triggerPlayback();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            shouldGenerate = false;
        }

    };

    static {
        final int STARTING_KEY = 16; // C2
        final int KEY_FREQ_INCREMENT = 1;
        final char[] KEYS = "zsxdcvgbhnjmq2w3er5t6y7ui9o0p[=]".toCharArray();
        for (int i = STARTING_KEY,
                key = 0; i < KEYS.length * KEY_FREQ_INCREMENT + STARTING_KEY; i += KEY_FREQ_INCREMENT, ++key) {
            KEY_FREQUENCIES.put(KEYS[key], Utils.Math.getKeyFreq(i));

        }
        for (Double d : KEY_FREQUENCIES.values()) {
            System.out.println(d);
        }

    }

    SynthGUI() {
        int y = 0;
        for (int i = 0; i < oscillators.length; ++i) {
            oscillators[i] = new Osc(this);
            oscillators[i].setLocation(5, y);
            frame.add(oscillators[i]);
            y += 105;
        }
        frame.addKeyListener(keyAdapter);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                audioThread.close();
            }

        });
        waveViewer.setBounds(290, 0, 310, 310);
        frame.add(waveViewer);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // What to do when user closes the JFrame
                                                                          // (here we opt to dispose of JFrame but not
                                                                          // close application)
        frame.setSize(613, 345);
        frame.setResizable(false);
        frame.setLayout(null); // layout items can be freely placed.
        frame.setLocationRelativeTo(null); // frame is centered on screen rather than placed relative to a component.
        frame.setVisible(true); // frame will appear

    }

    public KeyAdapter getKeyAdapter() {
        return keyAdapter;
    }

    public void updateWaveviewer() {
        waveViewer.repaint();
    }

    public static class AudioInfo {
        public static final int SAMPLE_RATE = 44100;
    }

}
