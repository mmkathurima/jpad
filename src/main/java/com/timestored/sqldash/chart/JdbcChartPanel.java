package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;
import com.timestored.TimeStored;
import com.timestored.kdb.KError;
import com.timestored.misc.InfoLink;
import com.timestored.theme.Theme;
import kx.C;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcChartPanel
        extends JPanel {
    private static final Logger LOG = Logger.getLogger(JdbcChartPanel.class.getName());

    private ViewStrategy viewCreator;

    private ChartTheme theme;

    private UpdateableView updateableView;

    private ResultSet prevRS;
    private Exception e;

    private ChartFormatException lastChartFormatException;
    private ChartResultSet prevCRS;

    JdbcChartPanel(ViewStrategy viewCreator, ChartTheme theme) {
        this.viewCreator = Preconditions.checkNotNull(viewCreator);
        this.theme = Preconditions.checkNotNull(theme);
        this.updateableView = viewCreator.getView(theme);

        this.setLayout(new GridLayout(1, 0));
        this.add(this.updateableView.getComponent());
    }

    private static Component getChartFormatExplaination(ViewStrategy viewStrategy, ChartFormatException cfe) {
        String text = viewStrategy.getFormatExplainationHtml();
        JEditorPane editPane = new JEditorPane("text/html", "<html>" + text + "</html>");

        JPanel wrapPanel = Theme.getVerticalBoxPanel();
        wrapPanel.add(Theme.getHeader(viewStrategy.getDescription()));
        wrapPanel.add(new JLabel(cfe.getDetails()));
        wrapPanel.add(Theme.getSubHeader("Data Format Expected"));

        editPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapPanel.add(editPane);
        wrapPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JPanel p = new JPanel(new BorderLayout());
        p.add(InfoLink.getButton("See Example Charts", "Click here to see chart examples", TimeStored.Page.SQLDASH_HELP_EG), "West");

        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapPanel.add(p);

        return new JScrollPane(wrapPanel);
    }

    public void setTheme(ChartTheme theme) {
        if (!this.theme.equals(theme)) {
            this.theme = Preconditions.checkNotNull(theme);
            this.refreshGUI();
        }
    }

    public void setViewStrategy(ViewStrategy viewCreator) {
        if (!this.viewCreator.equals(viewCreator)) {
            this.viewCreator = Preconditions.checkNotNull(viewCreator);
            this.refreshGUI();
        }
    }

    private void refreshGUI() {
        LOG.fine("JdbcChartPanel refreshGUI()");

        if (EventQueue.isDispatchThread()) {
            this.runn();
        } else {

            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        JdbcChartPanel.this.runn();
                    }
                });
            } catch (InterruptedException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    private void runn() {
        Component c = null;
        try {
            this.updateableView = this.viewCreator.getView(this.theme);
            if (this.prevRS != null) {

                this.lastChartFormatException = null;
                this.updateableView.update(this.prevRS, this.prevCRS);
                c = this.updateableView.getComponent();
            } else if (this.e instanceof C.KException) {
                c = KError.getDescriptionComponent((C.KException) this.e);
            } else if (this.e != null) {
                String msg = "Error retrieving query";
                if (this.e != null) {
                    msg = this.e.getMessage();
                }
                Component errDetails = Theme.getTextArea("qryErr", msg);
                c = Theme.getErrorBox("Query Error", errDetails);
            } else {
                c = Theme.getTextArea("noRes", "No table returned.");
            }
        } catch (ChartFormatException cfe) {
            this.lastChartFormatException = cfe;
            c = getChartFormatExplaination(this.viewCreator, cfe);
        } catch (IllegalArgumentException iae) {
            final String txt = "Problem updating view from RecordSet";
            LOG.log(Level.SEVERE, txt, iae);
            c = Theme.getErrorBox("RS Error", Theme.getTextArea("errTxt", txt));
        } catch (NullPointerException npe) {
            final String txt = "Problem updating view from RecordSet";
            LOG.log(Level.SEVERE, txt, npe);
            c = Theme.getErrorBox("RS Error", Theme.getTextArea("errTxt", txt));
        }

        this.removeAll();
        this.add(c);
        this.revalidate();
    }

    public void update(ResultSet resultSet) {
        this.prevRS = resultSet;
        this.e = null;
        this.prevCRS = null;
        if (resultSet != null) {
            try {
                this.prevCRS = ChartResultSet.getInstance(resultSet);
            } catch (SQLException e) {
                LOG.log(Level.INFO, "could not create chartResultSet ", e);
            } catch (IllegalArgumentException e) {
                LOG.log(Level.WARNING, "could not create chartResultSet ", e);
            } catch (NullPointerException e) {
                LOG.log(Level.WARNING, "could not create chartResultSet ", e);
            }
        }
        this.refreshGUI();
    }

    public void update(Exception e) {
        this.prevRS = null;
        this.e = Preconditions.checkNotNull(e);
        this.prevCRS = null;
        this.refreshGUI();
    }

    public ChartFormatException getLastChartFormatException() {
        return this.lastChartFormatException;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\JdbcChartPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */