/*
 * MEGAN MCNAMEE #Lab4 Printer Model/Implementation 
 */
package edu.cuny.csi.csc330.lab4;

import java.util.*;

public class Printer {

	
	private static final int PAPER_MAX = 100; //holder will hold 100 pages max
	private static final int INK_MAX = 100; //because >100% ink in cartridge causes a mess
	public static final String ERROR_PRINTER_OFF = "Try turning it on first?";
	public static final String ERROR_PRINTER_UNPLUG = "Try plugging it in first?";
	

	//States
	private boolean outletState;
	private boolean busyState;
	private boolean powerState;
	private boolean interrupted;
	
	//Job
	private Queue<Integer> jobQueue = new LinkedList<Integer>(); //Int represents # of pages in job
	
	//Paper
	private int paperCount;
	private String paperType;
	
	//Ink
	private int inkLevel; //Out of 100
	
	
	public Printer() {
		outletState = false;
		busyState = false;
		powerState = false;
		paperCount = 7; 
		paperType = "Letter";
		inkLevel = 5;
	}

	public boolean isPluggedIn() {
		return outletState;
	}
	public void toggleOutlet() {
		outletState = !outletState;
	}
	public boolean isOn() {
		return powerState;
	}
	public void togglePower() {
		if (outletState) {
			powerState = !powerState;
		} else {
			System.out.println("Try plugging it in first?");
		}
	}
	public boolean isBusy() {
		return busyState;
	}
	public boolean wasInterrupted() {
		return interrupted;
	}
	public String getPaperType() {
		return paperType;
	}
	public void loadPaper(int paperAmt) {
		if((paperCount + paperAmt) > PAPER_MAX) {
			System.out.println("Too much paper! " + (paperCount+paperAmt - PAPER_MAX) + " It appears pieces of paper fell out as you pushed the paper tray in...");
			paperCount = PAPER_MAX;
		} else {
		paperCount += paperAmt;
		}
	}
	public boolean isTherePaper() {
		return paperCount > 0;
	}
	public int getInkLevel() {
		return inkLevel;
	}
	public void loadInk(int inkAmt) {
		if ((inkLevel + inkAmt) > INK_MAX) {
			System.out.println("Too much ink! " + (inkLevel+inkAmt - INK_MAX) + " The ink amount spilled...");
			inkLevel = INK_MAX;
		}
		else {
			inkLevel += inkAmt;
		}
		}
	public void sendJob(int jobPageSize) {
		jobQueue.add(jobPageSize);
	}
	public void processJobs() {
		int intHolder;
		int jobCount = 0;
		
		//Reset processing cancelled when this method is called
		interrupted = false;
		
		//Make busyState true
		busyState = true;
		
		while (!jobQueue.isEmpty() ) {
			jobCount++;
			intHolder = jobQueue.peek();
			if (paperCount < intHolder) {
				//Halt operations when there is no paper or not enough paper for job; abnormal behavior for a printer but sufficient for our purposes...
				System.out.println("Not enough paper! Please reload and then continue job.");
				interrupted = true;
				busyState = false;
				break;
			} else if (inkLevel < intHolder) {
				//Halt operations when there is no ink or not enough ink for job; abnormal behavior for a printer but sufficient for our purposes...
				System.out.println("Not enough ink! Please reload and then continue job.");
				interrupted = true;
				busyState = false;
				break;
			} else {
				for (int i = 0; i < intHolder; i++) {
					System.out.printf("Job %d page %d printed! %n", jobCount, i+1);
					
					//Decrement resources
					paperCount--;
					inkLevel--;
				}
			}
			
			if (interrupted)
				break;
			
			jobQueue.remove();
			System.out.println(this);
		}
		
		//Job done, update busy state to false
		busyState = false;
	}


@Override
	public String toString() {
		return "Printer [outletState=" + outletState + ", busyState=" + busyState + ", powerState=" + powerState
				+ ", interrupted=" + interrupted + ", jobQueue=" + jobQueue + ", paperCount=" + paperCount
				+ ", paperType=" + paperType + ", inkLevel=" + inkLevel + "]";
	}

public static void main(String[] args) {
	int intHolder;
	int[] argsInt = new int[args.length];
	
	//Convert all args to int
	for (int i = 0; i < args.length; i++) {
		argsInt[i] = Integer.parseInt(args[i]);
	}
	
	//Scanner
	Scanner scanObj = new Scanner(System.in);
	
	//Instance of printer
	Printer printer = new Printer();
	
	System.out.println("NEW INSTANCE");
	System.out.println(printer + "\n\n");
	
	if (argsInt.length < 1) {
		System.err.println("Please enter >1 args");
	} else {
		for (int i= 0; i < argsInt.length; i++) {
			switch (argsInt[i]) {
			case 1:
				//Plug/Unplug Printer
				System.out.println("PLUGGING/UNPLUGGING PRINTER");
				printer.toggleOutlet();
				break;
			case 2:
				//Turn On/Off the Printer
				System.out.println("TURNING POWER ON/OFF");
				if (printer.isPluggedIn()) {
				printer.togglePower();
				} else {
					System.out.println(ERROR_PRINTER_UNPLUG);
				}
				break;
			case 3:
				//Send print job(s)
				System.out.println("SENDING JOBS TO PRINT");
				if (printer.isOn()) {
					System.out.println("Input job sizes, use 0 when done");
					
					//Reset intHolder to -1 before starting
					intHolder = -1;
					while (intHolder != 0) {
						intHolder = scanObj.nextInt();
						if (intHolder != 0 ) {
							printer.sendJob(intHolder);
						}
					} 
					
					printer.processJobs();
				} else {
					System.out.println(ERROR_PRINTER_OFF);
				}
				
				break;
			case 4:
				//Check paper amt
				System.out.println("CHECK PAPER AMT");
				if (printer.isTherePaper()) {
					System.out.println("There is still paper in the paper tray.");
				} else {
					System.out.println("There is no more paper in the paper tray.");
				}
				break;
			case 5:
				//Check paper type
				System.out.println("CHECK PAPER TYPE");
				System.out.println("The paper type is " + printer.getPaperType() + ".");
				break;
			case 6:
				//Load paper
				System.out.println("LOADING PAPER");
				System.out.println("Input amount to place into tray: ");
				intHolder = scanObj.nextInt();
				scanObj.nextLine();
				printer.loadPaper(intHolder);
				break;
			case 7:
				//Check ink level
				System.out.println("CHECKING INK LEVEL");
				System.out.println("Ink level: " + printer.getInkLevel() + "%.");
				break;
			case 8:
				//Load ink
				System.out.println("LOADING INK");
				System.out.println("Input amount to place into cartridge: ");
				intHolder = scanObj.nextInt();
				scanObj.nextLine();
				printer.loadInk(intHolder);
				break;
			case 9:
				//Resume printing
				System.out.println("CONTINUING JOB");
				if (printer.isOn()) {
					if (printer.wasInterrupted()) {
						printer.processJobs();
					} else {
						System.out.println("Select send jobs instead!");
					}
				} else {
					System.out.println(ERROR_PRINTER_OFF);
				}
				break;
			default:
				System.out.println("INVALID ARG");	
			}
			System.out.println(printer);
			System.out.println("\n\n");
		}
	}
	scanObj.close();
}

}