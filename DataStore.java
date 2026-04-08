import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    private static final List<Train> trains = new ArrayList<>();
    private static final List<Reservation> reservations = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();
    private static final Map<String, String> lastBookedPnrByUser = new HashMap<>();
    private static long nextPnr = 1;

    static {
        // Predefined trains (you can change seats/names here)
        trains.add(new Train(10101, "City Express", 50));
        trains.add(new Train(20202, "Super Fast", 40));
        trains.add(new Train(30303, "Night Rider", 60));

        // Default user
        users.add(new User("admin", "admin123"));
    }

    public static List<Train> getTrains() {
        return Collections.unmodifiableList(trains);
    }

    public static List<Train> searchTrains(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getTrains();
        }
        String q = query.trim().toLowerCase();
        List<Train> result = new ArrayList<>();
        for (Train t : trains) {
            if (String.valueOf(t.getTrainNumber()).contains(q) ||
                    t.getTrainName().toLowerCase().contains(q)) {
                result.add(t);
            }
        }
        return result;
    }

    public static Train findTrainByNumber(int trainNumber) {
        for (Train t : trains) {
            if (t.getTrainNumber() == trainNumber) {
                return t;
            }
        }
        return null;
    }

    public static boolean registerUser(String username, String password) {
        if (username == null || password == null) return false;
        String u = username.trim();
        if (u.isEmpty() || password.isEmpty()) return false;
        if (findUser(u) != null) return false;
        users.add(new User(u, password));
        return true;
    }

    public static boolean validateLogin(String username, String password) {
        if (username == null || password == null) return false;
        User u = findUser(username.trim());
        return u != null && u.getPassword().equals(password);
    }

    public static Reservation findReservationByPNR(String pnr) {
        if (pnr == null) return null;
        for (Reservation r : reservations) {
            if (r.getPnrNumber().equalsIgnoreCase(pnr.trim())) {
                return r;
            }
        }
        return null;
    }

    public static Reservation bookReservation(
            String passengerName,
            int passengerAge,
            Train train,
            String classType,
            String journeyDate,
            String source,
            String destination
    ) {
        if (train == null || !train.hasAvailableSeat()) {
            return null;
        }

        train.bookSeat();
        String pnr = String.format("PNR%06d", nextPnr++);

        Reservation res = new Reservation(
                pnr,
                passengerName,
                passengerAge,
                train,
                classType,
                journeyDate,
                source,
                destination
        );
        reservations.add(res);
        return res;
    }

    public static boolean cancelReservation(String pnr) {
        Reservation r = findReservationByPNR(pnr);
        if (r == null) return false;
        r.getTrain().cancelSeat();
        return reservations.remove(r);
    }

    public static String getLastBookedPnrForUser(String username) {
        if (username == null) return null;
        return lastBookedPnrByUser.get(username.trim().toLowerCase());
    }

    public static void setLastBookedPnrForUser(String username, String pnr) {
        if (username == null || pnr == null) return;
        lastBookedPnrByUser.put(username.trim().toLowerCase(), pnr.trim());
    }

    public static boolean deleteUser(String username, String password) {
        if (username == null || password == null) return false;
        String u = username.trim();
        if (u.isEmpty()) return false;
        if (u.equalsIgnoreCase("admin")) return false; // protect default user

        User found = findUser(u);
        if (found == null) return false;
        if (!found.getPassword().equals(password)) return false;

        lastBookedPnrByUser.remove(u.toLowerCase());
        return users.remove(found);
    }

    private static User findUser(String username) {
        if (username == null) return null;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
}