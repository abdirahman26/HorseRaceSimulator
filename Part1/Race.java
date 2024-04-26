import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{

        public static void main(String[] args) {
           
            // Create a race with a distance of 100 units
            Race race = new Race(10);
            
            // Create three horses and add them to the race lanes
            // Horse horse1 = new Horse('♞', "H1", 0.3); // Symbol: '1', Name: H1, Confidence: 0.7
            // Horse horse2 = new Horse('♘', "H2", 0.2); // Symbol: '2', Name: H2, Confidence: 0.8
            // Horse horse3 = new Horse('Z', "H3", 0.9); // Symbol: '3', Name: H3, Confidence: 0.9
    
            
            // race.addHorse(horse1, 1); // Add horse1 to lane 1
            // race.addHorse(horse2, 2); // Add horse2 to lane 2
            // race.addHorse(horse3, 3); // Add horse3 to lane 3
            // race.addHorse(horse3, 1); // Add horse3 to lane 4 (should fail)
            // race.addHorse(horse2, 4); // Add horse2 to lane 4 (should fail)

    
            
            // Start the race
            race.startRace();
        
        }
    
    
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**x
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */

    public void addHorse(Horse theHorse, int laneNumber) {
        
        switch (laneNumber) {
            case 1:
                if (lane1Horse != null) {
                    System.out.println("Lane 1 is already occupied.");
                } else {
                    lane1Horse = theHorse;
                }
                break;
            case 2:
                if (lane2Horse != null) {
                    System.out.println("Lane 2 is already occupied.");
                } else {
                    lane2Horse = theHorse;
                }
                break;
            case 3:
                if (lane3Horse != null) {
                    System.out.println("Lane 3 is already occupied.");
                } else {
                    lane3Horse = theHorse;
                }
                break;
            default:
                System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane.");
                break;
        }
    }
    
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        // Check if all lanes are empty
        if (lane1Horse == null && lane2Horse == null && lane3Horse == null) {
        System.out.println("No horses have been added to the race. Race cannot start.");
        return;  // Exit the method early if no horses are present
    }
        
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        lane3Horse.goBackToStart();
                      
        while (!finished)
        {
            //move each horse
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            moveHorse(lane3Horse);
                        
            //print the race positions
            printRace();
            
            // Check for a winner
            // Horse winner = raceWonBy();
            // if (winner != null) {
            //     finished = true;
            //     System.out.println("The race is finished! The winner is " + winner.getName() + "!");
            // } else if(allHorsesDown()) {
            //     finished = true;
            //     System.out.println("All horses have fallen");
            // }

            List<Horse> winners = raceWonBy();
            if (!winners.isEmpty()) {
                finished = true;
                if (winners.size() > 1) {
                    System.out.println("The race is finished with a tie!");
                    for (Horse winner : winners) {
                        System.out.println("Winner: " + winner.getName());
                    }
                } else {
                    System.out.println("The race is finished! The winner is " + winners.get(0).getName() + "!");
                }
            } else if (allHorsesDown()) {
                finished = true;
                System.out.println("All horses have fallen");
            }
    



            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
    }

    // Helper method to check if all horses have fallen
    private boolean allHorsesDown() {
        return (lane1Horse.hasFallen() && lane2Horse.hasFallen() && lane3Horse.hasFallen());
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */

    private void moveHorse(Horse theHorse)
    {
        // if the horse has fallen it cannot move, 
        // so only run if it has not fallen
        if (!theHorse.hasFallen())
        {
            // the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
                theHorse.moveForward();
                // Increase confidence by 0.05 and ensure it does not exceed 1
                double newConfidence = Math.min(theHorse.getConfidence() + 0.1, 1.0);
                theHorse.setConfidence(newConfidence);
            }
    
            // the probability that the horse will fall is very small (max is 0.1)
            // but will also depend exponentially on confidence 
            // so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence()))
            {
                theHorse.fall();
                // Decrease confidence by 0.05 and ensure it does not go below 0
                double newConfidence = Math.max(theHorse.getConfidence() - 0.1, 0.0);
                theHorse.setConfidence(newConfidence);
            }
        }
    }
    

        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */

    private List<Horse> raceWonBy() {
        List<Horse> winners = new ArrayList<>();
        if (lane1Horse != null && lane1Horse.getDistanceTravelled() >= raceLength) {
            winners.add(lane1Horse);
        }
        if (lane2Horse != null && lane2Horse.getDistanceTravelled() >= raceLength) {
            winners.add(lane2Horse);
        }
        if (lane3Horse != null && lane3Horse.getDistanceTravelled() >= raceLength) {
            winners.add(lane3Horse);
        }
        return winners; // Return list of winners
    }
    
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        printLane(lane1Horse);
        System.out.println();
        
        printLane(lane2Horse);
        System.out.println();
        
        printLane(lane3Horse);
        System.out.println();
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();  
        
       
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('❌');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');

         // Print the confidence next to the horse lane
         System.out.printf(" %s (Confidence: %.2f)", theHorse.getName(), theHorse.getConfidence());
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

}
