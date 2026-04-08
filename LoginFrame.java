import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister, btnClear, btnExit;

    public LoginFrame() {
        setTitle("Online Reservation System - Login");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        initComponents();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Online Reservation System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblUsername = new JLabel("Login ID:");
        JLabel lblPassword = new JLabel("Password:");

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        btnClear = new JButton("Clear");
        btnExit = new JButton("Exit");

        btnLogin.addActionListener(this::handleLogin);
        btnRegister.addActionListener(e -> {
            RegisterFrame rf = new RegisterFrame(this);
            rf.setVisible(true);
            this.setVisible(false);
        });
        btnClear.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
        });
        btnExit.addActionListener(e -> System.exit(0));

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(lblUsername, gbc);
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(lblPassword, gbc);
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnLogin);
        panelButtons.add(btnRegister);
        panelButtons.add(btnClear);
        panelButtons.add(btnExit);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Login ID and Password",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DataStore.validateLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            DashboardFrame dashboard = new DashboardFrame(username);
            dashboard.setVisible(true);
            dashboard.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Login ID or Password",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}