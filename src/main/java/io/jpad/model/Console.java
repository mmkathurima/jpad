package io.jpad.model;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class Console
        extends JInternalFrame
        implements InternalFrameListener, ActionListener, Runnable {
    private final PipedInputStream pin = new PipedInputStream();
    private final PipedInputStream pin2 = new PipedInputStream();
    private final JTextArea textArea;
    Thread errorThrower;
    private Thread reader;
    private Thread reader2;
    private boolean quit;

    public Console() {

        this.setTitle("Console");

        this.textArea = new JTextArea();

        this.textArea.setEditable(false);

        this.textArea.setFont(new Font("Dialog", 0, 10));

        JButton button = new JButton("Effacer");

        this.getContentPane().setLayout(new BorderLayout());

        this.getContentPane().add(new JScrollPane(this.textArea), "Center");

        this.getContentPane().add(button, "South");

        this.setVisible(true);

        this.addInternalFrameListener(this);

        button.addActionListener(this);

        this.redirect();

        this.setDefaultCloseOperation(1);

        this.setSize(300, 200);

        this.setClosable(true);

        this.setIconifiable(true);

        this.setMaximizable(true);

        this.setResizable(true);
    }

    private void redirect() {

        try {

            PipedOutputStream pout = new PipedOutputStream(this.pin);

            System.setOut(new PrintStream(pout, true));
        } catch (IOException io) {

            this.textArea.append("Couldn't redirect STDOUT to this console\n" + io
                    .getMessage());
        } catch (SecurityException se) {

            this.textArea.append("Couldn't redirect STDOUT to this console\n" + se
                    .getMessage());
        }

        try {

            PipedOutputStream pout2 = new PipedOutputStream(this.pin2);

            System.setErr(new PrintStream(pout2, true));
        } catch (IOException io) {

            this.textArea.append("Couldn't redirect STDERR to this console\n" + io
                    .getMessage());
        } catch (SecurityException se) {

            this.textArea.append("Couldn't redirect STDERR to this console\n" + se
                    .getMessage());
        }

        this.quit = false;

        this.reader = new Thread(this);

        this.reader.setDaemon(true);

        this.reader.start();

        this.reader2 = new Thread(this);

        this.reader2.setDaemon(true);

        this.reader2.start();

        this.errorThrower = new Thread(this);

        this.errorThrower.setDaemon(true);

        this.errorThrower.start();
    }

    public synchronized void actionPerformed(ActionEvent evt) {

        this.textArea.setText("");
    }

    public synchronized void run() {

        try {

            while (Thread.currentThread() == this.reader) {

                try {

                    this.wait(100L);
                } catch (InterruptedException ie) {
                }

                if (this.pin.available() != 0) {

                    String input = this.readLine(this.pin);

                    this.textArea.append(input);
                }

                if (this.quit) {

                    return;
                }
            }

            while (Thread.currentThread() == this.reader2) {

                try {

                    this.wait(100L);
                } catch (InterruptedException ie) {
                }

                if (this.pin2.available() != 0) {

                    String input = this.readLine(this.pin2);

                    this.textArea.append(input);
                }

                if (this.quit)
                    return;
            }
        } catch (Exception e) {

            this.textArea.append("\nConsole reports an Internal error.");

            this.textArea.append("The error is: " + e);
        }
    }

    protected synchronized String readLine(PipedInputStream in) throws IOException {

        String input = "";

        do {

            int available = in.available();

            if (available == 0)
                break;

            byte[] b = new byte[available];

            in.read(b);

            input = input + new String(b, 0, b.length);
        } while (!input.endsWith("\n") && !input.endsWith("\r\n") && !this.quit);

        return input;
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {

        this.setVisible(false);
    }

    public void internalFrameClosed(InternalFrameEvent e) {

        this.quit = true;

        this.notifyAll();

        try {

            this.reader.join(1000L);

            this.pin.close();
        } catch (Exception ex) {
        }

        try {

            this.reader2.join(1000L);

            this.pin2.close();
        } catch (Exception ex) {
        }
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\Console.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */