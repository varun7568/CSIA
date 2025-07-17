import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PieChartPanel extends JPanel {
    private Map<String, Integer> data;
    private int total;

    public PieChartPanel(Map<String, Integer> data) {
        this.data = data;
        for (int value : data.values()) total += value;
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        int x = 50, y = 50, diameter = 300;
        int startAngle = 0;
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};

        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label = entry.getKey();
            int value = entry.getValue();
            int angle = (int) Math.round(360.0 * value / total);

            g2.setColor(colors[i % colors.length]);
            g2.fillArc(x, y, diameter, diameter, startAngle, angle);
            g2.drawString(label + " (" + value + ")", x + diameter + 20, y + 20 + i * 20);
            startAngle += angle;
            i++;
        }
    }
}
