package appo.pacman.behaviour.tree.decorator;

import appo.pacman.behaviour.tree.INode;
import appo.pacman.behaviour.tree.Status;
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