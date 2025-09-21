import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private JLabel statusLabel;

    public StatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        add(statusLabel, BorderLayout.WEST);
    }

    public void setWarning(String warning) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText("WARNING: " + warning);
    }

    public void setNormal(String message) {
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText(message);
    }

    public void clear() {
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Ready");
    }
}