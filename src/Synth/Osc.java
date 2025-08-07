package Synth;



import java.awt.event.*;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import Synth.utils.RefWrapper;
import Synth.utils.Utils;

public class Osc extends SynthControlContainer {

    private final int TONE_OFFSET_LIMIT = 200;
    private Wavetable wavetable = Wavetable.Sine;

    private double keyFrequency;
    private int wavetableStepSize;
    private int wavetableIndex;

    private RefWrapper<Integer> toneOffset = new RefWrapper<Integer>(0);
    private RefWrapper<Integer> volume = new RefWrapper<Integer>(100);

    public Osc(SynthGUI synth) {

        super(synth);
        JComboBox<Wavetable> comboBox = new JComboBox<>(Wavetable.values());

        comboBox.setSelectedItem(Wavetable.Sine);
        comboBox.setBounds(10, 10, 75, 25);
        comboBox.addItemListener(l -> {
            if (l.getStateChange() == ItemEvent.SELECTED) {
                wavetable = (Wavetable) l.getItem();

            }
            synth.updateWaveviewer();
        }

        );
        add(comboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165, 65, 50, 25);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        Utils.ParameterHandling.addParameterMouseListeners(toneParameter, this, -TONE_OFFSET_LIMIT, TONE_OFFSET_LIMIT,
                1, toneOffset, () -> {
                    applyToneOffset();
                    toneParameter.setText(" x" + String.format("%.3f", getToneOffset()));
                    synth.updateWaveviewer();
                });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172, 40, 75, 25);
        add(toneText);

        JLabel volumeParameter = new JLabel(" 100%");
        volumeParameter.setBounds(222, 65, 50, 25);
        volumeParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        Utils.ParameterHandling.addParameterMouseListeners(volumeParameter, this, 0, 100, 1, volume,
                () -> 
                {
                    volumeParameter.setText(" " + volume.val + "%");
                    synth.updateWaveviewer();
                });

        add(volumeParameter);
        JLabel volumeText = new JLabel("Volume");
        volumeText.setBounds(225, 40, 75, 25);
        add(volumeText);

        setSize(279, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }
    
    
    public double nextSample() {
        double sample = wavetable.getSamples()[wavetableIndex] * getVolumeMultiplier();
        wavetableIndex = (wavetableIndex + wavetableStepSize) % Wavetable.SIZE;
        return sample;

    }


    

    

    
    private void applyToneOffset() {
        wavetableStepSize = (int) (Wavetable.SIZE * Utils.Math.offsetTone(keyFrequency, getToneOffset())
                / SynthGUI.AudioInfo.SAMPLE_RATE);
    }

    public void setFrequency(double frequency) {
        keyFrequency = frequency;
        applyToneOffset();
    }

    public double[] getSampleWaveform(int numSamples) {
        double[] samples = new double[numSamples];
        double frequency = 1.0 / (numSamples /(double)SynthGUI.AudioInfo.SAMPLE_RATE) *3.0;
        int index = 0;
        int stepSize = (int)(Wavetable.SIZE * Utils.Math.offsetTone(frequency, getToneOffset()) / SynthGUI.AudioInfo.SAMPLE_RATE);
        for (int i = 0; i < numSamples; ++i) {
            samples[i] = wavetable.getSamples()[index] * getVolumeMultiplier();
            index = (index + stepSize) % Wavetable.SIZE;
            
        }
        return samples;
    }

    private double getToneOffset() {
        return toneOffset.val / 100.0;
    }

    private double getVolumeMultiplier() {
        return volume.val / 100.0;
    }

}
