package behaviour.tree.decorator;

import pacman.game.Game;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class Inverter extends Decorator {

	public Inverter(INode child) {
		super(child);
	}

	@Override
	public Status run(Game game) {
		return child.run(game).opposite();
	}
}