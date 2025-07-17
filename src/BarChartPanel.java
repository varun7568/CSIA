import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartPanel extends JPanel {
    private Map<String, Integer> data;

    public BarChartPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / data.size();
        int max = data.values().stream().max(Integer::compareTo).orElse(1);

        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int barHeight = (int) ((entry.getValue() / (double) max) * (height - 50));
            int x = i * barWidth + 10;
            int y = height - barHeight - 30;

            g.setColor(Color.BLUE);
            g.fillRect(x, y, barWidth - 20, barHeight);
            g.setColor(Color.BLACK);
            g.drawString(entry.getKey(), x, height - 10);
            g.drawString(String.valueOf(entry.getValue()), x, y - 5);
            i++;
        }
    }
}
