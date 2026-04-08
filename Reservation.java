public class Reservation {
    private final String pnrNumber;
    private final String passengerName;
    private final int passengerAge;
    private final Train train;
    private final String classType;
    private final String journeyDate; // YYYY-MM-DD
    private final String sourceStation;
    private final String destinationStation;

    public Reservation(
            String pnrNumber,
            String passengerName,
            int passengerAge,
            Train train,
            String classType,
            String journeyDate,
            String sourceStation,
            String destinationStation
    ) {
        this.pnrNumber = pnrNumber;
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.train = train;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getPassengerAge() {
        return passengerAge;
    }

    public Train getTrain() {
        return train;
    }

    public String getClassType() {
        return classType;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }
}

