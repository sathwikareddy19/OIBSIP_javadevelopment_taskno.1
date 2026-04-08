public class Train {
    private final int trainNumber;
    private final String trainName;
    private final int totalSeats;
    private int availableSeats;

    public Train(int trainNumber, String trainName, int totalSeats) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public boolean hasAvailableSeat() {
        return availableSeats > 0;
    }

    public void bookSeat() {
        if (availableSeats <= 0) {
            throw new IllegalStateException("No seats available");
        }
        availableSeats--;
    }

    public void cancelSeat() {
        if (availableSeats >= totalSeats) {
            return;
        }
        availableSeats++;
    }

    @Override
    public String toString() {
        return trainNumber + " - " + trainName;
    }
}

