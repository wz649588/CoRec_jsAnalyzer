package jstest;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

public class ActionTool {
	
	public ActionTool(){
		
	}
	
	List<Action> getActions(ITree lTree, ITree rTree){
		Matcher m = Matchers.getInstance().getMatcher(lTree, rTree);
        m.match();
        ActionGenerator g = new ActionGenerator(lTree, rTree, m.getMappings());
        g.generate();
        return g.getActions();
	}
}
