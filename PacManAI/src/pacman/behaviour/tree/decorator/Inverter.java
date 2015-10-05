package pacman.behaviour.tree.decorator;

import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.Status;
import pacman.game.Game;

public class Inverter extends Decorator {

	public Inverter(INode child) {
		super(child);
	}

	@Override
	public Status run(Game game) {
		return child.run(game).opposite();
	}
}