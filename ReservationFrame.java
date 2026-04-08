import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationFrame extends JFrame {

    private JFrame parent;
    private final String loggedInUser;
    private JTextField txtPassengerName;
    private JTextField txtPassengerAge;
    private JComboBox<Train> cmbTrain;
    private DefaultComboBoxModel<Train> trainModel;
    private JTextField txtTrainNumber;
    private JTextField txtTrainName;
    private JComboBox<String> cmbClassType;
    private JTextField txtJourneyDate;
    private JButton btnPickDate;
    private JTextField txtSource;
    private JTextField txtDestination;
    private JLabel lblAvailableSeats;

    private JButton btnBook;
    private JButton btnClear;
    private JButton btnBack;

    private Train currentTrain;
    private LocalDate selectedJourneyDate;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    public ReservationFrame(JFrame parent, String loggedInUser) {
        this.parent = parent;
        this.loggedInUser = loggedInUser;

        setTitle("Online Reservation System - Ticket Reservation");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (ReservationFrame.this.parent != null) {
                    ReservationFrame.this.parent.setVisible(true);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (ReservationFrame.this.parent != null) {
                    ReservationFrame.this.parent.setVisible(true);
                }
            }
        });

        initComponents();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Ticket Reservation");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblPassengerName = new JLabel("Passenger Name:");
        JLabel lblPassengerAge = new JLabel("Passenger Age:");
        JLabel lblTrainSelect = new JLabel("Select Train:");
        JLabel lblTrainNumber = new JLabel("Train Number:");
        JLabel lblTrainName = new JLabel("Train Name:");
        JLabel lblClassType = new JLabel("Class Type:");
        JLabel lblJourneyDate = new JLabel("Date of Journey (YYYY-MM-DD):");
        JLabel lblSource = new JLabel("From (Source):");
        JLabel lblDestination = new JLabel("To (Destination):");
        JLabel lblAvail = new JLabel("Available Seats:");

        txtPassengerName = new JTextField(20);
        txtPassengerAge = new JTextField(5);
        trainModel = new DefaultComboBoxModel<>();
        cmbTrain = new JComboBox<>(trainModel);
        cmbTrain.setPrototypeDisplayValue(new Train(99999, "XXXXXXXXXXXXXXXXXXXXXXXXXXXX", 0));
        txtTrainNumber = new JTextField(10);
        txtTrainNumber.setEditable(false);
        txtTrainName = new JTextField(20);
        txtTrainName.setEditable(false);
        cmbClassType = new JComboBox<>(new String[]{"AC", "Sleeper", "General"});
        txtJourneyDate = new JTextField(10);
        txtJourneyDate.setEditable(false);
        btnPickDate = new JButton("📅");
        btnPickDate.setToolTipText("Select date");
        btnPickDate.addActionListener(e -> pickJourneyDate());
        txtSource = new JTextField(15);
        txtDestination = new JTextField(15);
        lblAvailableSeats = new JLabel("N/A");

        btnBook = new JButton("Book Ticket");
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back to Dashboard");

        loadTrainsIntoCombo(DataStore.getTrains());
        cmbTrain.addActionListener(e -> applySelectedTrain());

        btnBook.addActionListener(e -> bookTicket());
        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
        });

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblPassengerName, gbc);
        gbc.gridx = 1;
        panelForm.add(txtPassengerName, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblPassengerAge, gbc);
        gbc.gridx = 1;
        panelForm.add(txtPassengerAge, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblTrainSelect, gbc);
        gbc.gridx = 1;
        panelForm.add(cmbTrain, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblTrainNumber, gbc);
        gbc.gridx = 1;
        panelForm.add(txtTrainNumber, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblTrainName, gbc);
        gbc.gridx = 1;
        panelForm.add(txtTrainName, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblClassType, gbc);
        gbc.gridx = 1;
        panelForm.add(cmbClassType, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblJourneyDate, gbc);
        gbc.gridx = 1;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.add(txtJourneyDate);
        datePanel.add(Box.createHorizontalStrut(6));
        datePanel.add(btnPickDate);
        panelForm.add(datePanel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblSource, gbc);
        gbc.gridx = 1;
        panelForm.add(txtSource, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblDestination, gbc);
        gbc.gridx = 1;
        panelForm.add(txtDestination, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(lblAvail, gbc);
        gbc.gridx = 1;
        panelForm.add(lblAvailableSeats, gbc);
        row++;

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnBook);
        panelButtons.add(btnClear);
        panelButtons.add(btnBack);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);
    }

    private void fetchTrainDetails() {
        // kept for compatibility; selection now comes from combo box
        applySelectedTrain();
    }

    private void clearForm() {
        txtPassengerName.setText("");
        txtPassengerAge.setText("");
        loadTrainsIntoCombo(DataStore.getTrains());
        txtTrainNumber.setText("");
        txtTrainName.setText("");
        cmbClassType.setSelectedIndex(0);
        txtJourneyDate.setText("");
        selectedJourneyDate = null;
        txtSource.setText("");
        txtDestination.setText("");
        lblAvailableSeats.setText("N/A");
        currentTrain = null;
    }

    private void pickJourneyDate() {
        LocalDate picked = DatePickerDialog.pickDate(this, selectedJourneyDate);
        if (picked == null) {
            return;
        }
        selectedJourneyDate = picked;
        txtJourneyDate.setText(picked.format(ISO));
    }

    private void bookTicket() {
        String name = txtPassengerName.getText().trim();
        String ageStr = txtPassengerAge.getText().trim();
        String trainNumStr = txtTrainNumber.getText().trim();
        String trainName = txtTrainName.getText().trim();
        String classType = (String) cmbClassType.getSelectedItem();
        String journeyDateStr = txtJourneyDate.getText().trim();
        String source = txtSource.getText().trim();
        String dest = txtDestination.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty() || trainNumStr.isEmpty() ||
                trainName.isEmpty() || journeyDateStr.isEmpty() ||
                source.isEmpty() || dest.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        int trainNum;
        try {
            age = Integer.parseInt(ageStr);
            trainNum = Integer.parseInt(trainNumStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age and Train Number must be valid numbers.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentTrain == null || currentTrain.getTrainNumber() != trainNum) {
            JOptionPane.showMessageDialog(this, "Invalid or unknown train number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!currentTrain.hasAvailableSeat()) {
            JOptionPane.showMessageDialog(this, "No Seats Available for this train.",
                    "Seat Unavailable", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (selectedJourneyDate == null || journeyDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select Date of Journey using the calendar.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reservation res = DataStore.bookReservation(
                name, age, currentTrain, classType, journeyDateStr, source, dest
        );

        if (res != null) {
            DataStore.setLastBookedPnrForUser(loggedInUser, res.getPnrNumber());
            PnrReceiptUtil.showBookingSuccessDialog(this, res);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "No Seats Available.",
                    "Seat Unavailable", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadTrainsIntoCombo(List<Train> trains) {
        Train selected = (Train) cmbTrain.getSelectedItem();
        trainModel.removeAllElements();
        for (Train t : trains) {
            trainModel.addElement(t);
        }
        if (selected != null) {
            cmbTrain.setSelectedItem(selected);
        } else if (trainModel.getSize() > 0) {
            cmbTrain.setSelectedIndex(0);
        } else {
            cmbTrain.setSelectedIndex(-1);
            currentTrain = null;
            txtTrainNumber.setText("");
            txtTrainName.setText("");
            lblAvailableSeats.setText("N/A");
        }
        applySelectedTrain();
    }

    private void applySelectedTrain() {
        Train t = (Train) cmbTrain.getSelectedItem();
        currentTrain = t;
        if (t == null) {
            txtTrainNumber.setText("");
            txtTrainName.setText("");
            lblAvailableSeats.setText("N/A");
            return;
        }
        txtTrainNumber.setText(String.valueOf(t.getTrainNumber()));
        txtTrainName.setText(t.getTrainName());
        lblAvailableSeats.setText(String.valueOf(t.getAvailableSeats()));
    }
}