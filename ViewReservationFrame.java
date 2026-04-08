import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewReservationFrame extends JFrame {

    private JFrame parent;
    private JTextField txtPNR;
    private JTextArea txtDetails;
    private JButton btnSearch, btnClear, btnBack;

    public ViewReservationFrame(JFrame parent) {
        this.parent = parent;

        setTitle("Online Reservation System - View Reservation (PNR Search)");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (ViewReservationFrame.this.parent != null) {
                    ViewReservationFrame.this.parent.setVisible(true);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (ViewReservationFrame.this.parent != null) {
                    ViewReservationFrame.this.parent.setVisible(true);
                }
            }
        });

        initComponents();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("View Reservation (PNR Search)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblPNR = new JLabel("Enter PNR Number:");
        txtPNR = new JTextField(20);

        btnSearch = new JButton("Search");
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back to Dashboard");

        btnSearch.addActionListener(e -> searchPNR());
        btnClear.addActionListener(e -> {
            txtPNR.setText("");
            txtDetails.setText("");
        });
        btnBack.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
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

        setLayout(new BorderLayout(10, 10));
        add(lblTitle, BorderLayout.NORTH);
        add(panelTop, BorderLayout.PAGE_START);
        add(scroll, BorderLayout.CENTER);
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
            return;
        }

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
}