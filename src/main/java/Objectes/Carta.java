package Objectes;

public class Carta {
	
	//mantenim format variables tal format de xml
	private int id;
	private String name;
	private int summonCost;
	private int attack;
	private int defense;
	private int value;
	
	public Carta(int id, String name, int summonCost, int attack, int defense, int value) {
		super();
		this.id = id;
		this.name = name;
		this.summonCost = summonCost;
		this.attack = attack;
		this.defense = defense;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSummonCost() {
		return summonCost;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name+" A:"+attack+"/D:"+defense+"/S:"+summonCost+"/V:"+value;
	}
	
	
	
}
