package Synth;

import javax.swing.*;
import java.awt.*;

public class SynthControlContainer extends JPanel {
    protected boolean on;
    private SynthGUI synth;
    protected Point mouseClickLocation;

    public SynthControlContainer(SynthGUI synth) {
        this.synth = synth;

    }

    public Point getMouseClickLocation() {
        return mouseClickLocation;
    }

    public void setMouseClickLocation(Point mouseClickLocation) {
        this.mouseClickLocation = mouseClickLocation;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;

    }

    @Override
    public Component add(String name, Component comp) {
        
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        
        return super.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        
        super.add(comp, constraints, index);
    }

    @Override
    public void add(PopupMenu popup) {
        
        super.add(popup);
    }

    @Override
    public Component add(Component component) {
        component.addKeyListener(synth.getKeyAdapter());
        return super.add(component);

    }

}