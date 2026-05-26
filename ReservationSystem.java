import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Represents a Train
class Train {
    private int trainNumber;
    private String name;
    private String source;
    private String destination;
    private int totalSeats;
    private List<Integer> bookedSeats;

    public Train(int trainNumber, String name, String source, String destination, int totalSeats) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.bookedSeats = new ArrayList<>();
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return totalSeats - bookedSeats.size();
    }

    public boolean bookSeat(int seatNumber) {
        if (seatNumber > 0 && seatNumber <= totalSeats && !bookedSeats.contains(seatNumber)) {
            bookedSeats.add(seatNumber);
            return true;
        }
        return false;
    }

    public boolean cancelSeat(int seatNumber) {
        return bookedSeats.remove(Integer.valueOf(seatNumber));
    }
}

// Represents a Ticket
class Ticket {
    private static int nextTicketId = 1001;
    private int ticketId;
    private int trainNumber;
    private String passengerName;
    private int seatNumber;

    public Ticket(int trainNumber, String passengerName, int seatNumber) {
        this.ticketId = nextTicketId++;
        this.trainNumber = trainNumber;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketId + ", Train No: " + trainNumber +
               ", Passenger: " + passengerName + ", Seat: " + seatNumber;
    }
}

// Manages the reservation process
public class ReservationSystem {
    private List<Train> trains;
    private Map<Integer, Ticket> bookedTickets; // Map of ticketId to Ticket object

    public ReservationSystem() {
        trains = new ArrayList<>();
        bookedTickets = new HashMap<>();
        // Initialize some sample trains
        trains.add(new Train(101, "Express 101", "CityA", "CityB", 50));
        trains.add(new Train(202, "Superfast 202", "CityB", "CityC", 75));
    }

    public void displayAvailableTrains() {
        System.out.println("\n--- Available Trains ---");
        for (Train train : trains) {
            System.out.println("Train No: " + train.getTrainNumber() +
                               ", Name: " + train.getName() +
                               ", Route: " + train.getSource() + " to " + train.getDestination() +
                               ", Available Seats: " + train.getAvailableSeats() + "/" + train.getTotalSeats());
        }
    }

    public void bookTicket(Scanner scanner) {
        System.out.print("Enter Train Number: ");
        int trainNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Train selectedTrain = findTrain(trainNumber);
        if (selectedTrain == null) {
            System.out.println("Invalid Train Number.");
            return;
        }

        if (selectedTrain.getAvailableSeats() == 0) {
            System.out.println("No seats available on this train.");
            return;
        }

        System.out.print("Enter Passenger Name: ");
        String passengerName = scanner.nextLine();

        System.out.print("Enter desired Seat Number (1-" + selectedTrain.getTotalSeats() + "): ");
        int seatNumber = scanner.nextInt();
        scanner.nextLine();

        if (selectedTrain.bookSeat(seatNumber)) {
            Ticket ticket = new Ticket(trainNumber, passengerName, seatNumber);
            bookedTickets.put(ticket.getTicketId(), ticket);
            System.out.println("Ticket booked successfully! " + ticket);
        } else {
            System.out.println("Seat " + seatNumber + " is already booked or invalid.");
        }
    }

    public void cancelTicket(Scanner scanner) {
        System.out.print("Enter Ticket ID to cancel: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine();

        Ticket ticketToCancel = bookedTickets.get(ticketId);
        if (ticketToCancel == null) {
            System.out.println("Invalid Ticket ID.");
            return;
        }

        Train train = findTrain(ticketToCancel.getTrainNumber());
        if (train != null && train.cancelSeat(ticketToCancel.getSeatNumber())) {
            bookedTickets.remove(ticketId);
            System.out.println("Ticket " + ticketId + " cancelled successfully.");
        } else {
            System.out.println("Error cancelling ticket. Seat might not be marked as booked.");
        }
    }

    public void displayTicketDetails(Scanner scanner) {
        System.out.print("Enter Ticket ID to view details: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine();

        Ticket ticket = bookedTickets.get(ticketId);
        if (ticket != null) {
            System.out.println("\n--- Ticket Details ---");
            System.out.println(ticket);
        } else {
            System.out.println("Ticket not found.");
        }
    }

    private Train findTrain(int trainNumber) {
        for (Train train : trains) {
            if (train.getTrainNumber() == trainNumber) {
                return train;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ReservationSystem system = new ReservationSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Train Reservation System Menu ---");
            System.out.println("1. Display Available Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. View Ticket Details");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    system.displayAvailableTrains();
                    break;
                case 2:
                    system.bookTicket(scanner);
                    break;
                case 3:
                    system.cancelTicket(scanner);
                    break;
                case 4:
                    system.displayTicketDetails(scanner);
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}