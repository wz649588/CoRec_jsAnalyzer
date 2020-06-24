package jstest;

import java.util.HashSet;

import com.github.gumtreediff.tree.ITree;

import vt.edu.graph.ClientMember;

public class TraverseEntity {
	ClientMember entity;
	HashSet<String> variableSet;
	HashSet<String> functionSet;
	TraverseJsonTyped tJroot;
	ITree typedNode;
	ITree node;
	
	public TraverseEntity(ClientMember entity) {
		this.entity = entity;
		this.tJroot = entity.tJroot;
		this.typedNode = entity.typedNode;
		this.node = entity.node;
		buildVariableSet(typedNode);
		buildFunctionSet(typedNode);
	}
	
	private void buildVariableSet(ITree node) {
		
	}
	private void buildFunctionSet(ITree node)  {
		
	}
	
	public String typeOfITree(ITree node) {
		return node.getChild(0).getChild(1).getChild(0).toShortString().substring(8).replace("\"", "");
	}
	
	public boolean hasTheField (ITree node, String name) {
		for (ITree tmp : node.getChildren()) {
			if(tmp.getChild(0).toShortString().substring(8).equals("\"" + name + "\"")) return true;
		}
		return false;
	}
	
	
}
