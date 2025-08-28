import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class BarChartPanel extends JPanel {
    private Map<?, Integer> data;

    public BarChartPanel(Map<?, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();

        List<?> keys = new ArrayList<>(data.keySet());

        if (!keys.isEmpty() && keys.get(0) instanceof YearMonth) {
            List<YearMonth> monthlyKeys = new ArrayList<>();
            for (Object key : keys) {
                monthlyKeys.add((YearMonth) key);
            }
            monthlyKeys.sort(Comparator.naturalOrder());
            keys = monthlyKeys;
        }

        int barWidth = width / (keys.size() + 1);
        int max = data.values().stream().max(Integer::compareTo).orElse(1);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics metrics = g.getFontMetrics();

        int i = 0;
        for (Object key : keys) {
            int value = data.get(key);
            int barHeight = (int) ((value / (double) max) * (height - 50));
            int x = i * barWidth + 10;
            int y = height - barHeight - 30;

            g.setColor(Color.BLUE);
            g.fillRect(x, y, barWidth - 20, barHeight);

            g.setColor(Color.BLACK);
            String label = key.toString();
            int labelWidth = metrics.stringWidth(label);
            g.drawString(label, x + (barWidth - 20 - labelWidth) / 2, height - 10);
            g.drawString(String.valueOf(value), x + (barWidth - 20 - metrics.stringWidth(String.valueOf(value))) / 2, y - 5);

            i++;
        }
    }
}