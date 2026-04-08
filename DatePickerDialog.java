import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DatePickerDialog extends JDialog {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    private final JLabel lblMonth = new JLabel("", SwingConstants.CENTER);
    private final JButton btnPrev = new JButton("<");
    private final JButton btnNext = new JButton(">");

    private final JButton[] dayButtons = new JButton[42]; // 6 rows x 7 cols

    private YearMonth visibleMonth;
    private LocalDate selectedDate;
    private boolean ok;

    public static LocalDate pickDate(Component parent, LocalDate initial) {
        Window w = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        DatePickerDialog dlg = new DatePickerDialog(w, initial);
        dlg.setVisible(true);
        return dlg.ok ? dlg.selectedDate : null;
    }

    private DatePickerDialog(Window owner, LocalDate initial) {
        super(owner, "Select Date", ModalityType.APPLICATION_MODAL);

        LocalDate start = initial != null ? initial : LocalDate.now();
        this.visibleMonth = YearMonth.from(start);
        this.selectedDate = start;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(360, 320);
        setLocationRelativeTo(owner);
        setResizable(false);

        initUi();
        refreshCalendar();
    }

    private void initUi() {
        JPanel header = new JPanel(new BorderLayout(8, 0));
        btnPrev.addActionListener(e -> {
            visibleMonth = visibleMonth.minusMonths(1);
            refreshCalendar();
        });
        btnNext.addActionListener(e -> {
            visibleMonth = visibleMonth.plusMonths(1);
            refreshCalendar();
        });

        lblMonth.setFont(lblMonth.getFont().deriveFont(Font.BOLD, 14f));
        header.add(btnPrev, BorderLayout.WEST);
        header.add(lblMonth, BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);

        JPanel dow = new JPanel(new GridLayout(1, 7, 4, 4));
        String[] names = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String n : names) {
            JLabel l = new JLabel(n, SwingConstants.CENTER);
            l.setFont(l.getFont().deriveFont(Font.BOLD));
            dow.add(l);
        }

        JPanel grid = new JPanel(new GridLayout(6, 7, 4, 4));
        for (int i = 0; i < dayButtons.length; i++) {
            JButton b = new JButton("");
            b.setMargin(new Insets(2, 2, 2, 2));
            b.setFocusable(false);
            int idx = i;
            b.addActionListener(e -> onDayClicked(idx));
            dayButtons[i] = b;
            grid.add(b);
        }

        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        btnOk.addActionListener(e -> {
            ok = true;
            dispose();
        });
        btnCancel.addActionListener(e -> {
            ok = false;
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(new JLabel("Selected:"));
        JTextField txtSelected = new JTextField(10);
        txtSelected.setEditable(false);
        txtSelected.setText(selectedDate.format(ISO));
        bottom.add(txtSelected);
        bottom.add(btnCancel);
        bottom.add(btnOk);

        // keep selected field in sync
        addPropertyChangeListener(evt -> {
            if ("selectedDate".equals(evt.getPropertyName())) {
                LocalDate d = (LocalDate) evt.getNewValue();
                txtSelected.setText(d == null ? "" : d.format(ISO));
            }
        });

        JPanel content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.setLayout(new BorderLayout(8, 8));
        content.add(header, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(4, 4));
        mid.add(dow, BorderLayout.NORTH);
        mid.add(grid, BorderLayout.CENTER);
        content.add(mid, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        setContentPane(content);
    }

    private void onDayClicked(int index) {
        LocalDate first = visibleMonth.atDay(1);
        int shift = first.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue(); // 0..6
        int day = index - shift + 1;
        if (day < 1 || day > visibleMonth.lengthOfMonth()) {
            return;
        }
        LocalDate old = this.selectedDate;
        this.selectedDate = visibleMonth.atDay(day);
        firePropertyChange("selectedDate", old, this.selectedDate);
        refreshCalendar();
    }

    private void refreshCalendar() {
        lblMonth.setText(visibleMonth.getMonth().name().substring(0, 1)
                + visibleMonth.getMonth().name().substring(1).toLowerCase()
                + " " + visibleMonth.getYear());

        LocalDate first = visibleMonth.atDay(1);
        int shift = first.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue(); // 0..6
        int days = visibleMonth.lengthOfMonth();

        for (int i = 0; i < dayButtons.length; i++) {
            JButton b = dayButtons[i];
            int day = i - shift + 1;

            if (day < 1 || day > days) {
                b.setText("");
                b.setEnabled(false);
                b.setBackground(null);
                continue;
            }

            b.setEnabled(true);
            b.setText(String.valueOf(day));

            LocalDate d = visibleMonth.atDay(day);
            if (selectedDate != null && d.equals(selectedDate)) {
                b.setBackground(new Color(180, 210, 255));
            } else {
                b.setBackground(null);
            }

            if (d.equals(LocalDate.now())) {
                b.setFont(b.getFont().deriveFont(Font.BOLD));
            } else {
                b.setFont(b.getFont().deriveFont(Font.PLAIN));
            }

            b.setToolTipText(d.format(ISO));
        }
    }
}

