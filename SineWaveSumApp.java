import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;

public class SineWaveSumApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Frame A
        JFrame frameA = new JFrame("Frame A");
        frameA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameA.setLayout(new BorderLayout());

        // Frame B
        JFrame frameB = new JFrame("Frame B");
        frameB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameB.setLayout(new BorderLayout());

        // Frame C
        JFrame frameC = new JFrame("Frame C");
        frameC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameC.setLayout(new BorderLayout());

        // Create and add components to Frame A and Frame B
        SineWavePanel wavePanelA = new SineWavePanel();
        SineWavePanel wavePanelB = new SineWavePanel();
        frameA.add(wavePanelA, BorderLayout.CENTER);
        frameB.add(wavePanelB, BorderLayout.CENTER);

        // Create and add components to Frame C
        SineWavePanel wavePanelC = new SineWavePanel();
        frameC.add(wavePanelC, BorderLayout.CENTER);

        // Create sliders for amplitude, frequency, and phase for Frame A and Frame B
        SliderPanel sliderPanelA = new SliderPanel(wavePanelA);
        SliderPanel sliderPanelB = new SliderPanel(wavePanelB);
        frameA.add(sliderPanelA, BorderLayout.SOUTH);
        frameB.add(sliderPanelB, BorderLayout.SOUTH);

        // Add a button to calculate and display the sum in Frame C
        JButton sumButton = new JButton("Calculate Sum");
        sumButton.addActionListener(e -> {
            wavePanelC.setAmplitude(wavePanelA.getAmplitude() + wavePanelB.getAmplitude());
            wavePanelC.setFrequency(wavePanelA.getFrequency() + wavePanelB.getFrequency());
            wavePanelC.setPhase(wavePanelA.getPhase() + wavePanelB.getPhase());
            wavePanelC.repaint();
        });
        frameC.add(sumButton, BorderLayout.SOUTH);

        frameA.setSize(400, 300);
        frameB.setSize(400, 300);
        frameC.setSize(400, 300);
        frameA.setVisible(true);
        frameB.setVisible(true);
        frameC.setVisible(true);
    }
}

class SineWavePanel extends JPanel {
    private double amplitude = 50.0;
    private double frequency = 1.0;
    private double phase = 0.0;

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
        repaint();
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
        repaint();
    }

    public void setPhase(double phase) {
        this.phase = phase;
        repaint();
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getPhase() {
        return phase;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D path = new Path2D.Double();

        for (int x = 0; x < width; x++) {
            double y = amplitude * Math.sin(2 * Math.PI * frequency * x / width + phase);
            if (x == 0) {
                path.moveTo(x, height / 2 - y);
            } else {
                path.lineTo(x, height / 2 - y);
            }
        }

        g2d.setColor(Color.BLUE);
        g2d.draw(path);
    }
}

class SliderPanel extends JPanel {
    private JSlider amplitudeSlider;
    private JSlider frequencySlider;
    private JSlider phaseSlider;
    private SineWavePanel wavePanel;

    public SliderPanel(SineWavePanel wavePanel) {
        this.wavePanel = wavePanel;
        amplitudeSlider = createSlider("Amplitude", 0, 100, (int) wavePanel.getAmplitude());
        frequencySlider = createSlider("Frequency", 1, 10, (int) wavePanel.getFrequency());
        phaseSlider = createSlider("Phase", 0, 360, (int) Math.toDegrees(wavePanel.getPhase()));

        setLayout(new GridLayout(3, 2));
        add(new JLabel("Amplitude:"));
        add(amplitudeSlider);
        add(new JLabel("Frequency:"));
        add(frequencySlider);
        add(new JLabel("Phase:"));
        add(phaseSlider);

        amplitudeSlider.addChangeListener(e -> wavePanel.setAmplitude(amplitudeSlider.getValue()));
        frequencySlider.addChangeListener(e -> wavePanel.setFrequency(frequencySlider.getValue()));
        phaseSlider.addChangeListener(e -> wavePanel.setPhase(Math.toRadians(phaseSlider.getValue())));
    }

    private JSlider createSlider(String label, int min, int max, int initialValue) {
        JSlider slider = new JSlider(min, max, initialValue);
        slider.setMajorTickSpacing((max - min) / 4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }
}
