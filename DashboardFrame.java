import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DashboardFrame extends JFrame {

    private String loggedInUser;
    private JLabel lblLastPnr;

    public DashboardFrame(String username) {
        this.loggedInUser = username;

        setTitle("Online Reservation System - Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                refreshLastPnr();
            }
        });

        initComponents();
    }

    private void initComponents() {
        JLabel lblWelcome = new JLabel("Welcome, " + loggedInUser);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);

        lblLastPnr = new JLabel("");
        lblLastPnr.setHorizontalAlignment(SwingConstants.CENTER);
        refreshLastPnr();

        JButton btnReservation = new JButton("Book Ticket (Reservation)");
        JButton btnViewPNR = new JButton("View Reservation (PNR Search)");
        JButton btnCancel = new JButton("Cancel Reservation");
        JButton btnLogout = new JButton("Logout");
        JButton btnDeleteAccount = new JButton("Delete Account");

        btnReservation.addActionListener(e -> {
            ReservationFrame r = new ReservationFrame(this, loggedInUser);
            r.setVisible(true);
            this.setVisible(false);
        });

        btnViewPNR.addActionListener(e -> {
            ViewReservationFrame v = new ViewReservationFrame(this);
            v.setVisible(true);
            this.setVisible(false);
        });

        btnCancel.addActionListener(e -> {
            CancellationFrame c = new CancellationFrame(this);
            c.setVisible(true);
            this.setVisible(false);
        });

        btnLogout.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                this.dispose();
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });

        btnDeleteAccount.addActionListener(e -> deleteAccount());
        if ("admin".equalsIgnoreCase(loggedInUser)) {
            btnDeleteAccount.setEnabled(false);
            btnDeleteAccount.setToolTipText("Default admin account cannot be deleted.");
        }

        JPanel panelButtons = new JPanel(new GridLayout(5, 1, 10, 10));
        panelButtons.add(btnReservation);
        panelButtons.add(btnViewPNR);
        panelButtons.add(btnCancel);
        panelButtons.add(btnLogout);
        panelButtons.add(btnDeleteAccount);

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel top = new JPanel(new GridLayout(2, 1, 4, 4));
        top.add(lblWelcome);
        top.add(lblLastPnr);
        container.add(top, BorderLayout.NORTH);
        container.add(panelButtons, BorderLayout.CENTER);

        setContentPane(container);
    }

    private void refreshLastPnr() {
        String pnr = DataStore.getLastBookedPnrForUser(loggedInUser);
        if (pnr == null || pnr.trim().isEmpty()) {
            lblLastPnr.setText("Last booked PNR: (none yet)");
        } else {
            lblLastPnr.setText("Last booked PNR: " + pnr);
        }
    }

    private void deleteAccount() {
        if ("admin".equalsIgnoreCase(loggedInUser)) {
            JOptionPane.showMessageDialog(this,
                    "Admin account cannot be deleted.",
                    "Not Allowed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel p = new JPanel(new GridLayout(2, 1, 6, 6));
        p.add(new JLabel("Enter your password to delete account:"));
        JPasswordField pf = new JPasswordField(18);
        p.add(pf);

        int option = JOptionPane.showConfirmDialog(this, p,
                "Delete Account (" + loggedInUser + ")", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (option != JOptionPane.OK_OPTION) return;

        String pass = new String(pf.getPassword());
        boolean ok = DataStore.deleteUser(loggedInUser, pass);
        if (!ok) {
            JOptionPane.showMessageDialog(this,
                    "Could not delete account. Please check your password.",
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Account deleted successfully.",
                "Deleted",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
    }
}