import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CancellationFrame extends JFrame {

    private JFrame parent;
    private JTextField txtPNR;
    private JTextArea txtDetails;
    private JButton btnSearch, btnCancel, btnBack, btnClear;

    private Reservation currentReservation;

    public CancellationFrame(JFrame parent) {
        this.parent = parent;

        setTitle("Online Reservation System - Cancellation");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (CancellationFrame.this.parent != null) {
                    CancellationFrame.this.parent.setVisible(true);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (CancellationFrame.this.parent != null) {
                    CancellationFrame.this.parent.setVisible(true);
                }
            }
        });

        initComponents();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Cancellation Form");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblPNR = new JLabel("Enter PNR Number:");
        txtPNR = new JTextField(20);

        btnSearch = new JButton("Search");
        btnCancel = new JButton("Cancel Ticket");
        btnBack = new JButton("Back to Dashboard");
        btnClear = new JButton("Clear");

        btnSearch.addActionListener(e -> searchPNR());
        btnCancel.addActionListener(e -> cancelTicket());
        btnBack.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
        });
        btnClear.addActionListener(e -> {
            txtPNR.setText("");
            txtDetails.setText("");
            currentReservation = null;
        });

        txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtDetails);

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.add(lblPNR);
        leftTop.add(txtPNR);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTop.add(btnSearch); // top-right
        rightTop.add(btnClear);
        rightTop.add(btnBack);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(leftTop, BorderLayout.WEST);
        panelTop.add(rightTop, BorderLayout.EAST);

        JPanel panelBottomButtons = new JPanel();
        panelBottomButtons.add(btnCancel);

        setLayout(new BorderLayout(10, 10));
        add(lblTitle, BorderLayout.NORTH);
        add(panelTop, BorderLayout.PAGE_START);
        add(scroll, BorderLayout.CENTER);
        add(panelBottomButtons, BorderLayout.SOUTH);
    }

    private void searchPNR() {
        String pnr = txtPNR.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter PNR number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reservation res = DataStore.findReservationByPNR(pnr);
        if (res == null) {
            JOptionPane.showMessageDialog(this, "No reservation found for given PNR.",
                    "Not Found", JOptionPane.INFORMATION_MESSAGE);
            txtDetails.setText("");
            currentReservation = null;
            return;
        }

        currentReservation = res;

        StringBuilder sb = new StringBuilder();
        sb.append("PNR Number: ").append(res.getPnrNumber()).append("\n");
        sb.append("Passenger Name: ").append(res.getPassengerName()).append("\n");
        sb.append("Passenger Age: ").append(res.getPassengerAge()).append("\n");
        sb.append("Train Number: ").append(res.getTrain().getTrainNumber()).append("\n");
        sb.append("Train Name: ").append(res.getTrain().getTrainName()).append("\n");
        sb.append("Class Type: ").append(res.getClassType()).append("\n");
        sb.append("Date of Journey: ").append(res.getJourneyDate()).append("\n");
        sb.append("From: ").append(res.getSourceStation()).append("\n");
        sb.append("To: ").append(res.getDestinationStation()).append("\n");

        txtDetails.setText(sb.toString());
    }

    private void cancelTicket() {
        if (currentReservation == null) {
            JOptionPane.showMessageDialog(this, "Please search for a valid PNR first.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this ticket?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = DataStore.cancelReservation(currentReservation.getPnrNumber());
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Reservation cancelled successfully and seat availability updated.",
                    "Cancellation Successful", JOptionPane.INFORMATION_MESSAGE);
            txtDetails.setText("");
            txtPNR.setText("");
            currentReservation = null;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: Could not cancel reservation.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}