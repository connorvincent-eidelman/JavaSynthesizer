package Synth;

import javax.swing.JPanel;

import Synth.utils.Utils;
import java.awt.*;
import java.util.function.Function;

public class WaveViewer extends JPanel {
    private Osc[] oscillators;

    public WaveViewer(Osc[] oscillators) {
        this.oscillators = oscillators;
        setBorder(Utils.WindowDesign.LINE_BORDER);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        final int PAD = 25;
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int numSamples = getWidth() - PAD * 2;
        double[] mixedSamples = new double[numSamples];
        for(Osc oscillator: oscillators) {
            double[] samples = oscillator.getSampleWaveform(numSamples);
            for (int i = 0; i < samples.length; ++i) {
                mixedSamples[i] += samples[i] / oscillators.length;
            }
        }
        int midY = getHeight() / 2;
        Function<Double, Integer> sampleToYCord = sample -> (int)(midY + sample * (midY-PAD));
        
        graphics2D.drawLine(PAD, midY, getWidth() - PAD, midY);
        graphics2D.drawLine(PAD, PAD, PAD, getHeight() - PAD);
        for (int i = 0; i< numSamples; ++i) {
            int nextY = i == numSamples - 1 ? sampleToYCord.apply(mixedSamples[i]) : sampleToYCord.apply(mixedSamples[i+1]);
            graphics2D.drawLine(PAD+i, sampleToYCord.apply(mixedSamples[i]), PAD + i + 1, nextY);
        }

    }

}
