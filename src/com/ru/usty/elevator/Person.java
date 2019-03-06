package com.ru.usty.elevator;

public class Person implements Runnable {

	int source, dest;
	Elevator myElevator;
	Operator operator;
	public Boolean up;

	Person(int source, int dest, Operator op) {
		this.source = source;
		this.dest = dest;
		operator = op;
		if(source > dest) {
			up = false;
		}
		else {
			up = true;
		}
	}

	@Override
	public void run() {

		try {
			if(up) {
				operator.inSemUp[source].acquire();
			}
			else {
				operator.inSemDown[source].acquire();
			}
			
			operator.addPersonToAvailableElevatorAtFloor(this, source);
			ElevatorScene.personEntersAtFloor(source);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(true) {
			if(myElevator.currentFloor == dest)
			{
				try {
					operator.outSem[dest].acquire();
					ElevatorScene.personExitsAtFloor(dest);
					myElevator.removePerson();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}	
			Thread.yield();
		}
	}
	
	public void getOnElevator(Elevator e)
	{
		myElevator = e;
	}
}
