package com.timestored.sqldash;

import com.google.common.collect.Maps;
import com.timestored.TimeStored;
import com.timestored.misc.HtmlUtils;
import com.timestored.sqldash.chart.ChartTheme;
import com.timestored.sqldash.chart.ViewStrategy;
import com.timestored.sqldash.chart.ViewStrategyFactory;
import com.timestored.sqldash.model.ChartWidget;
import com.timestored.sqldash.model.Widget;
import com.timestored.theme.Theme;
import org.jdesktop.swingx.combobox.MapComboBoxModel;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class ChartControlPanel
        extends JPanel
        implements Widget.Listener {
    private static final long serialVersionUID = 1L;
    private static final TimeStored.Page helpPage = TimeStored.Page.SQLDASH_HELP_EG;
    private static final String DEFAULT_TOOLTIP = "<html><div width='350px'><b>Charts</b><br><a href='" + helpPage.url() + "'>" + helpPage.niceUrl() + "</a></div></html>";

    private final JComboBox chartComboBox;

    private final MapComboBoxModel<String, ViewStrategy> chartStratComboBoxModel;

    private final MapComboBoxModel<String, ChartTheme> chartThemeComboBoxModel;

    private final ChartWidget app;

    private final JTextField titleTextField;

    private final JComboBox chartThemeComboBox;

    public ChartControlPanel(ChartWidget app) {
        this.app = app;

        List<ViewStrategy> chartStrats = ViewStrategyFactory.getStrategies();
        Map<String, ViewStrategy> descToChartStrat = Maps.newLinkedHashMap();
        for (ViewStrategy cs : chartStrats) {
            descToChartStrat.put(cs.getDescription(), cs);
        }
        this.chartStratComboBoxModel = new MapComboBoxModel(descToChartStrat);

        List<ChartTheme> chartThemes = ViewStrategyFactory.getThemes();
        Map<String, ChartTheme> descToChartTheme = Maps.newLinkedHashMap();
        for (ChartTheme ct : chartThemes) {
            descToChartTheme.put(ct.getTitle(), ct);
        }
        this.chartThemeComboBoxModel = new MapComboBoxModel(descToChartTheme);

        this.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        this.setLayout(new BoxLayout(this, 1));

        Theme.InputLabeller IL = Theme.getInputLabeller();
        this.chartComboBox = new JComboBox<String>(this.chartStratComboBoxModel);

        this.chartThemeComboBox = new JComboBox<String>(this.chartThemeComboBoxModel);

        this.titleTextField = new JTextField(15);

        if (app != null) {
            this.chartComboBox.setSelectedItem(app.getViewStrategy().getDescription());

            this.titleTextField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    app.setTitle(ChartControlPanel.this.titleTextField.getText());
                }
            });

            this.chartComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    int i = ChartControlPanel.this.chartComboBox.getSelectedIndex();
                    ViewStrategy vs = ChartControlPanel.this.chartStratComboBoxModel.getValue(i);
                    if (!app.getViewStrategy().equals(vs)) {
                        app.setViewStrategy(vs);
                    }
                }
            });

            this.chartThemeComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    int i = ChartControlPanel.this.chartThemeComboBox.getSelectedIndex();
                    ChartTheme th = ChartControlPanel.this.chartThemeComboBoxModel.getValue(i);
                    if (!app.getChartTheme().equals(th)) {
                        app.setChartTheme(th);
                    }
                }
            });
        } else {

            this.chartComboBox.setEnabled(false);
            this.chartThemeComboBox.setEnabled(false);
            this.titleTextField.setEnabled(false);
        }

        this.add(IL.get("Type:", this.chartComboBox, "chartComboBox", new CurrentViewInfoLinkLabel()));
        this.add(IL.get("Theme:", this.chartThemeComboBox, "chartThemeComboBox"));
        this.add(IL.get("Title:", this.titleTextField, "titleTextField"));

        if (app != null) {
            app.addListener(this);
        }
        this.refreshGui();
    }

    private void refreshGui() {
        boolean enabled = (this.app != null);
        this.chartComboBox.setEnabled(enabled);
        this.chartThemeComboBox.setEnabled(enabled);

        if (enabled) {

            if (!this.titleTextField.isFocusOwner()) {
                this.titleTextField.setText("" + this.app.getTitle());
            }

            if (!this.chartComboBox.isFocusOwner()) {
                ViewStrategy vs = this.app.getViewStrategy();
                this.chartComboBox.setSelectedItem(vs.getDescription());
            }

            if (!this.chartThemeComboBox.isFocusOwner()) {
                ChartTheme ct = this.app.getChartTheme();
                this.chartThemeComboBox.setSelectedItem(ct.getTitle());
            }
        }
    }

    public void configChanged(Widget app) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChartControlPanel.this.refreshGui();
            }
        });
    }

    public class CurrentViewInfoLinkLabel
            extends JLabel {
        private static final long serialVersionUID = 1L;

        public CurrentViewInfoLinkLabel() {
            super(Theme.CIcon.INFO.get());

            ToolTipManager.sharedInstance().registerComponent(this);

            if (HtmlUtils.isBrowseSupported()) {
                this.setCursor(Cursor.getPredefinedCursor(12));
                this.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        HtmlUtils.browse(helpPage.url());
                    }
                });
            }
        }

        public String getToolTipText() {
            String tooltip = DEFAULT_TOOLTIP;
            if (ChartControlPanel.this.app != null) {
                ViewStrategy vs = ChartControlPanel.this.app.getViewStrategy();
                tooltip = "<html><div width='350px'><b>" + vs.getDescription() + "</b>" + "<br>" + vs.getFormatExplainationHtml() + "<br><a href='" + helpPage.url() + "'>" + helpPage.niceUrl() + "</a></div></html>";
            }

            return tooltip;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\ChartControlPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */