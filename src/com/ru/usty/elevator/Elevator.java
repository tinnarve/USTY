package com.ru.usty.elevator;

import java.util.concurrent.Semaphore;

public class Elevator implements Runnable {

	int numberFloors, maxOccupants, currentFloor, currentOccupants;
	Operator operator;
	Boolean goUp;
	private Semaphore occupantsMutex;
	private boolean exit;

	Elevator(int numberFloors, int maxOccupants, Operator operator) {
		this.numberFloors = numberFloors;
		this.maxOccupants = maxOccupants;
		this.currentFloor = 0;
		this.currentOccupants = 0;
		this.operator = operator;
		this.exit = false;
		if(currentFloor == numberFloors - 1) goUp = false;
		else goUp = true;
		occupantsMutex = new Semaphore(1);
	}

	@Override
	public void run() {
		
		while (!exit) {
			if(goUp) moveUp();
			else moveDown();
			if(Thread.interrupted()) die();
			Thread.yield();
		}
	}

	public void addPerson(Person p)
	{
		try {
			  occupantsMutex.acquire();
			  try {
				  p.getOnElevator(this);
				  currentOccupants++;
			  } finally {
			    occupantsMutex.release();
			  }
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
	}

	public void removePerson() {
		try {
		  occupantsMutex.acquire();
		  try {
			  currentOccupants--;
		  } finally {
		    occupantsMutex.release();
		  }
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void die()
	{
		System.out.println("Asked to stop. Stopping.");
		this.exit = true;
	}

	public void moveUp() {
		
		if(currentFloor + 1 < numberFloors)
		{
			currentFloor++;
			if(currentFloor == numberFloors-1) {
				goUp = false;
			}
			operator.openElevator(this); 
			operator.closeElevator(this);
		}
	}

	public void moveDown() {
		
		if(currentFloor != 0)
		{
			currentFloor--;
			if(currentFloor == 0) {
				goUp = true;
			}
			operator.openElevator(this); 
			operator.closeElevator(this);
			
		}
	}
	public Boolean getDirection() {
		return goUp;
	}
}
