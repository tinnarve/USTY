package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Operator {

	private int numFloors;
	private int numElevators;

	
	public Semaphore[] inSem;
	public Semaphore[] outSem;
	private static Elevator[] elevators;
	public static final int MAX_OCCUPANTS = 6;
	
	
	public static Semaphore ingressLock = new Semaphore(1);
	
	Operator(int floors, int nElevators){
		numFloors = floors;
		numElevators = nElevators;
		
		
		inSem = new Semaphore[numFloors];
		outSem = new Semaphore[numFloors];
		
		for (int i = 0; i < numFloors; i++) {
			inSem[i] = new Semaphore(0);
			outSem[i] = new Semaphore(0);
		}
		elevators = new Elevator[numElevators];
		for (int i = 0; i < numElevators; i++) {
			Elevator e = new Elevator(numFloors, MAX_OCCUPANTS, this);
			elevators[i] = e;
		}
		
	}
	
	public Elevator[] getElevators() {
		
		return elevators;
	}
	public void addPerson(Person p) {
		
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
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1){
			e1.printStackTrace();
		}
		outSem[e.currentFloor].release(MAX_OCCUPANTS);
		inSem[e.currentFloor].release(MAX_OCCUPANTS - e.currentOccupants);
	}
	public void closeElevator(Elevator e) 
	{
		outSem[e.currentFloor].drainPermits();
		inSem[e.currentFloor].drainPermits();	
	}
	
	public int getCurrentFloor(int e) {
		return elevators[e].currentFloor;
	}
	public int getNumPInElevator(int e) {
		return elevators[e].currentOccupants;
	}
	//
}
