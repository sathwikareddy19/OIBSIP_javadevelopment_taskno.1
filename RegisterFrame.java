import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame extends JFrame {

    private final JFrame parent;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirm;

    public RegisterFrame(JFrame parent) {
        this.parent = parent;

        setTitle("Online Reservation System - Register");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        initComponents();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Create New Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblUsername = new JLabel("New Login ID:");
        JLabel lblPassword = new JLabel("New Password:");
        JLabel lblConfirm = new JLabel("Confirm Password:");

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirm = new JPasswordField(20);

        JButton btnCreate = new JButton("Create Account");
        JButton btnClear = new JButton("Clear");
        JButton btnBack = new JButton("Back to Login");

        btnCreate.addActionListener(e -> createAccount());
        btnClear.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            txtConfirm.setText("");
        });
        btnBack.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
        });

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        form.add(lblUsername, gbc);
        gbc.gridx = 1;
        form.add(txtUsername, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(lblPassword, gbc);
        gbc.gridx = 1;
        form.add(txtPassword, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(lblConfirm, gbc);
        gbc.gridx = 1;
        form.add(txtConfirm, gbc);
        row++;

        JPanel buttons = new JPanel();
        buttons.add(btnCreate);
        buttons.add(btnClear);
        buttons.add(btnBack);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void onClose() {
        int option = JOptionPane.showConfirmDialog(this,
                "Cancel account creation and go back to Login?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            parent.setVisible(true);
        }
    }

    private void createAccount() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Login ID must be at least 3 characters.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = DataStore.registerUser(username, password);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "This Login ID already exists (or invalid).",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Account created successfully! Please login.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
        parent.setVisible(true);
    }
}

