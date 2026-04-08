import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class PnrReceiptUtil {
    private PnrReceiptUtil() {}

    public static void showBookingSuccessDialog(Component parent, Reservation res) {
        Objects.requireNonNull(res, "reservation");

        String message = "Ticket booked successfully!\n\nYour PNR Number is: " + res.getPnrNumber()
                + "\n\nTip: Use this PNR in View Reservation / Cancellation screens.";

        String[] options = {"Copy PNR", "Save Ticket", "OK"};
        int choice = JOptionPane.showOptionDialog(
                parent,
                message,
                "Booking Successful",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[2]
        );

        if (choice == 0) {
            copyToClipboard(res.getPnrNumber());
            JOptionPane.showMessageDialog(parent, "PNR copied to clipboard.", "Copied",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (choice == 1) {
            try {
                Path savedTo = saveTicketToFile(res);
                JOptionPane.showMessageDialog(parent,
                        "Ticket saved to:\n" + savedTo.toAbsolutePath(),
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent,
                        "Could not save ticket.\n" + ex.getMessage(),
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(text), null
        );
    }

    /**
     * Saves a small readable ticket to a folder called "pnr-receipts" inside the project folder.
     * If that fails (permission / unknown), falls back to user's home directory.
     */
    private static Path saveTicketToFile(Reservation res) throws IOException {
        String fileName = res.getPnrNumber() + ".txt";

        Path base = Paths.get(System.getProperty("user.dir"), "pnr-receipts");
        try {
            Files.createDirectories(base);
        } catch (IOException ignored) {
            base = Paths.get(System.getProperty("user.home"), "pnr-receipts");
            Files.createDirectories(base);
        }

        Path file = base.resolve(fileName);
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String content =
                "ONLINE RESERVATION SYSTEM - TICKET\n" +
                "Generated: " + now + "\n" +
                "----------------------------------------\n" +
                "PNR Number      : " + res.getPnrNumber() + "\n" +
                "Passenger Name  : " + res.getPassengerName() + "\n" +
                "Passenger Age   : " + res.getPassengerAge() + "\n" +
                "Train Number    : " + res.getTrain().getTrainNumber() + "\n" +
                "Train Name      : " + res.getTrain().getTrainName() + "\n" +
                "Class Type      : " + res.getClassType() + "\n" +
                "Journey Date    : " + res.getJourneyDate() + "\n" +
                "From            : " + res.getSourceStation() + "\n" +
                "To              : " + res.getDestinationStation() + "\n" +
                "----------------------------------------\n" +
                "Use this PNR for viewing/cancellation.\n";

        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        return file;
    }
}

