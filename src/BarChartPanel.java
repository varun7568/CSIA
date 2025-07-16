import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartPanel extends JPanel {
    private final Map<String, Integer> data;

    public BarChartPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        int barWidth = width / data.size();
        int max = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int value = entry.getValue();
            int barHeight = (int) ((value / (double) max) * (height - 60));

            int x = i * barWidth + 10;
            int y = height - barHeight - 30;

            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, barWidth - 20, barHeight);

            g2.setColor(Color.BLACK);
            g2.drawString(entry.getKey(), x, height - 10);
            g2.drawString(String.valueOf(value), x, y - 5);

            i++;
        }
    }
}
