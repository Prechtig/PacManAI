package behaviour.tree.composite;

import behaviour.tree.INode;

public abstract class Composite implements INode {
	
	protected final INode[] children;
	
	protected Composite(INode... children) {
		this.children = children;
	}
}
