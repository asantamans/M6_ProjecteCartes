package idao;

import Objectes.Baralla;

public interface IBaralla {
	public Baralla getDeckFromName(String nom);
	public boolean guardarBaralla(Baralla b1);
	public boolean actualitzarBaralla(Baralla b1);
}
