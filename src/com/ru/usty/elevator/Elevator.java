package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	int numberFloors, maxOccupants;
	Elevator(int numberFloors, int maxOccupants) {
		this.numberFloors = numberFloors;
		this.maxOccupants = maxOccupants;
	}

	@Override
	public void run() {
		try {
			ElevatorScene.sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Elevator Thread");
	}
	
}
