package Synth.utils;

import Synth.SynthControlContainer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import static java.lang.Math.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Utils {
    public static void handleProcedure(Procedure procedure, boolean printStackTrace) { // automates the printing of a
                                                                                       // stack trace.
        try {
            procedure.invoke(); // attempt to throw.

        } catch (Exception e) {
            if (printStackTrace) {
                e.printStackTrace();
            }
        }
    }

    public static class ParameterHandling {
        public static final Robot PARAMETER_ROBOT;
        static {
            try {
                PARAMETER_ROBOT = new Robot();
            } catch (AWTException e) {
                throw new ExceptionInInitializerError("cannot construct robot instance.");
            }
        }

        private ParameterHandling() {
        }

        public static void addParameterMouseListeners(Component component, SynthControlContainer container, int minVal,
                int maxVal, int valStep, RefWrapper<Integer> parameter, Procedure onChangeProcedure) {
            component.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank_cursor");
                    component.setCursor(BLANK_CURSOR);
                    container.setMouseClickLocation(e.getLocationOnScreen());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    component.setCursor(Cursor.getDefaultCursor());

                }

            });
            component.addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (container.getMouseClickLocation().y != e.getYOnScreen()) {
                        boolean mouseMovingUp = container.getMouseClickLocation().y - e.getYOnScreen() > 0;
                        if (mouseMovingUp && parameter.val < maxVal) {
                            parameter.val += valStep;
                        } else if (!mouseMovingUp && parameter.val > minVal) {
                            parameter.val -= valStep;

                        }
                        if (onChangeProcedure != null) {
                            handleProcedure(onChangeProcedure, true);
                        }
                        PARAMETER_ROBOT.mouseMove(container.getMouseClickLocation().x,
                                container.getMouseClickLocation().y);
                    }

                }

            });

        }
    }

    public static class WindowDesign {
        public static final Border LINE_BORDER = BorderFactory.createLineBorder(Color.black);

    }

    public static class Math {

        public static double offsetTone(double baseFrequency, double frequencyMultiplier) {
            return baseFrequency * pow(2.0, frequencyMultiplier);

        }

        public static double frequencyToAngularFrequency(double freq) {
            return 2 * PI * freq;
        }

        public static double getKeyFreq(double keyNum) {
            return pow(2.0, (keyNum - 49.0) / 12.0) * 440;

        }

    }

}
