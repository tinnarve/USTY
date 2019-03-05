package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same for the test
 * suite and graphics to use. You can add functions and/or change the
 * functionality of the operations at will.
 *
 */

public class ElevatorScene {

	// TO SPEED THINGS UP WHEN TESTING,
	// feel free to change this. It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500; // milliseconds
	public static final int MAX_OCCUPANTS = 6;

	private int numberOfFloors;
	private int numberOfElevators;

	private static ArrayList<Integer> personCount; // use if you want but
									// throw away and
									// implement differently
									// if it suits you
	private static ArrayList<Integer> exitedCount = null;
	private static Elevator[] elevators;
	private static List<Thread> threads;

	public static Semaphore exitedCountMutex;

	public static Semaphore[] inSem;
	public static Semaphore[] outSem;

	// Base function: definition must not change
	// Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {

		// Need to add a true or false that tells the system if it should join
		// into the elevator threads so that if restartScene is called several times in
		// a row

		inSem = new Semaphore[numberOfFloors];
		outSem = new Semaphore[numberOfFloors];

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// for(int i = 0; i < 20; i++) {
		// sem.release();
		// System.out.println("Permits " + sem.availablePermits());
		// }
		// }
		// }).start();

		/**
		 * Important to add code here to make new threads that run your
		 * elevator-runnables
		 * 
		 * Also add any other code that initializes your system for a new run
		 * 
		 * If you can, tell any currently running elevator threads to stop
		 */

		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		personCount = new ArrayList<Integer>();
		for (int i = 0; i < numberOfFloors; i++) {
			inSem[i] = new Semaphore(0);
			outSem[i] = new Semaphore(0);
			personCount.add(0);
		}

		if (exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		} else {
			exitedCount.clear();
		}
		for (int i = 0; i < getNumberOfFloors(); i++) {
			exitedCount.add(0);
		}
		exitedCountMutex = new Semaphore(1);

		threads = new ArrayList<Thread>();

		// Elevator setup
		elevators = new Elevator[numberOfElevators];
		for (int i = 0; i < numberOfElevators; i++) {
			Elevator e = new Elevator(numberOfFloors, MAX_OCCUPANTS);
			Thread e_thread = new Thread(e);
			threads.add(e_thread);
			e_thread.start();
			elevators[i] = e;
			inSem[0].release(MAX_OCCUPANTS);
		}
	}

	public static Semaphore ingressLock = new Semaphore(1);
	public static void addPersonToAvailableElevatorAtFloor(Person p, int floor) {
		try {
			ingressLock.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		boolean derp = false;
		while(!derp)
		{
			for (Elevator e : elevators) 
			{
				if(e.currentFloor == floor && e.currentOccupants != e.maxOccupants)
				{
					e.addPerson(p);
					derp = true;
					personCount.set(floor, personCount.get(floor) - 1);
					break;
				}
			}	
			Thread.yield();	
		}
		ingressLock.release();
	}	
	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		
		Person person = new Person(sourceFloor, destinationFloor);
		Thread person_thread = new Thread(person);
		
		//Can also be written like:
		// Thread thread1 = new Thread(new Person(sourceFloor, destinationFloor));
		
		person_thread.start();
		
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */

		//dumb code, replace it!
		personCount.set(sourceFloor, personCount.get(sourceFloor) + 1);
		return null;  //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {
		return elevators[elevator].currentFloor;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		return elevators[elevator].currentOccupants;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {
		return personCount.get(floor);
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	//Person threads must call this function to
	//let the system know that they have exited.
	//Person calls it after being let off elevator
	//but before it finishes its run.
	public static void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}


}
