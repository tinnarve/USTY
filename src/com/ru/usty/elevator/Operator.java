package com.ru.usty.elevator;
import java.util.concurrent.Semaphore;

public class Operator {

	private int numFloors;
	private int numElevators;

	
	public Semaphore[] inSemDown;
	public Semaphore[] inSemUp;
	public Semaphore[] outSem;
	private static Elevator[] elevators;
	public static final int MAX_OCCUPANTS = 6;
	
	
	public static Semaphore ingressLock = new Semaphore(1);
	
	Operator(int floors, int nElevators){
		numFloors = floors;
		numElevators = nElevators;
		
		
		inSemDown = new Semaphore[numFloors];
		inSemUp = new Semaphore[numFloors];
		outSem = new Semaphore[numFloors];
		
		for (int i = 0; i < numFloors; i++) {
			inSemDown[i] = new Semaphore(0);
			inSemUp[i] = new Semaphore(0);
			outSem[i] = new Semaphore(0);
		}
		elevators = new Elevator[numElevators];
		int floorCounter = -1;
		for (int i = 0; i < numElevators; i++) {
			Elevator e = new Elevator(numFloors, MAX_OCCUPANTS, this);
			e.currentFloor = floorCounter;
			floorCounter = (floorCounter + 1)%(numFloors - 1);
			elevators[i] = e;
		}
		
	}
	
	public Elevator[] getElevators() {
		
		return elevators;
	}
	
	public void addPersonToAvailableElevatorAtFloor(Person p, int floor) {
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
					break;
				}
			}	
			Thread.yield();	
		}
		ingressLock.release();
	}
	
	public void openElevator(Elevator e) 
	{
		//Wait for the elevator to reach the designated floor
		try {
			Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME / 3);
		} catch (InterruptedException e1){
			e.die();
			return;
		}

		//Let people out
		outSem[e.currentFloor].release(MAX_OCCUPANTS);
		try {
			Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME / 3);
		} catch (InterruptedException e1){
			e.die();
			return;
		}
		
		//Let people in, depending on where they're going
		if(e.getDirection() == true) {
			inSemUp[e.currentFloor].release(MAX_OCCUPANTS - e.currentOccupants);
		}
		else {
			inSemDown[e.currentFloor].release(MAX_OCCUPANTS - e.currentOccupants);
		}
		
		//Wait for the doors to close
		try {
			Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME / 3);
		} catch (InterruptedException e1){
			e.die();
			return;
		}
	}
	public void closeElevator(Elevator e) 
	{
		outSem[e.currentFloor].drainPermits();
		inSemUp[e.currentFloor].drainPermits();	
		inSemDown[e.currentFloor].drainPermits();
	}
	
	public int getCurrentFloor(int e) {
		return elevators[e].currentFloor;
	}
	public int getNumPInElevator(int e) {
		return elevators[e].currentOccupants;
	}
	
	//
}
