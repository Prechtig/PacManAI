package behaviour.tree;

import pacman.game.Game;


public interface INode {
	
	public Status run(Game game);
	
}