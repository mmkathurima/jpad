package com.timestored.swingxx;

import org.jdesktop.swingx.JXStatusBar;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.util.Date;

public class QueryStatusBar
        extends JXStatusBar {
    private static final long serialVersionUID = 1L;
    private final JProgressBar pbar;
    private final JLabel statusLabel = new JLabel("Ready");
    private final JLabel rowCountLabel;
    private final JLabel timingLabel;
    private long startTick;

    public QueryStatusBar() {
        JXStatusBar.Constraint fillConstraint = new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);
        JXStatusBar.Constraint fixedWidthConstraint = new JXStatusBar.Constraint();
        fixedWidthConstraint.setFixedWidth(100);
        this.pbar = new JProgressBar();
        this.rowCountLabel = new JLabel("Count = 0  ");
        this.timingLabel = new JLabel("Time = 0 ms   ");

        this.add(this.statusLabel, fillConstraint);
        this.add(this.rowCountLabel);
        this.add(this.timingLabel);
        this.add(this.pbar, fixedWidthConstraint);
    }

    public void startQuery(String query) {
        this.startTick = (new Date()).getTime();
        this.display("sent query: " + query, true, -1L, -1);
    }

    public void endQuery(String statusText, int count) {
        long millisTaken = (new Date()).getTime() - this.startTick;
        this.startTick = (new Date()).getTime();
        this.display(statusText, false, millisTaken, count);
    }

    private void display(String statusText, boolean waiting, long millisTaken, int count) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                int c = waiting ? 3 : 0;
                QueryStatusBar.this.setCursor(Cursor.getPredefinedCursor(c));
                String s = statusText;
                if (s.length() > 60) {
                    s = statusText.substring(0, 55) + "...";
                }
                QueryStatusBar.this.statusLabel.setText(s);
                QueryStatusBar.this.pbar.setIndeterminate(waiting);

                String countTxt = "Count = ?";
                if (count >= 0) {
                    countTxt = "Count = " + count;
                }
                String timerTxt = "Time = ? ms";
                if (millisTaken >= 0L) {
                    timerTxt = "Time = " + millisTaken + " ms";
                }
                QueryStatusBar.this.timingLabel.setText(timerTxt);
                QueryStatusBar.this.rowCountLabel.setText(countTxt);
            }
        });
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\QueryStatusBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */