package vt.edu.graph;


public class ReferenceEdge {
	public static final int VAR_ACCESS = 1;
	public static final int FUNC_INVOKE = 2;
	public static final int CLASS_CONTAIN = 3;
	
	public static final int CLASS_INHERITANCE = 4;
	
//	public static final int DEF_DEF = 5;
	
	public int type;
	
	/**
	 * callee is control-dependent on another statement.
	 * callee <--control-- other
	 */
	public static final int CALLEE_CONTROL_DEP_OTHER = 0x1;
	
	/**
	 * another statement is control-dependent on callee.
	 * other <--control-- callee
	 */
	public static final int OTHER_CONTROL_DEP_CALLEE = 0x2;
	
	/**
	 * callee is data-dependent on another statement.
	 * callee <--data-- other
	 */
	public static final int CALLEE_DATA_DEP_OTHER = 0x4;
	
	/**
	 * another statement is data-dependent on callee.
	 * other <--data-- callee
	 */
	public static final int OTHER_DATA_DEP_CALLEE = 0x8;
	
	/**
	 * data/control dependency
	 */
	public int dep = 0;
	
	public ReferenceNode from;
	public ReferenceNode to;
	public int count = 0;
	
	public ReferenceEdge(ReferenceNode from, ReferenceNode to, int type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	public void increaseCount() {
		count++;
	}
	
	public static String getTypeString(int type) {
		String s;
		switch (type) {
		case 1:
			s = "VAR_ACCESS";
			break;
		case 2:
			s = "FUNC_INVOKE";
			break;
		case 3:
			s = "CLASS_CONTAIN";
			break;
		case 4:
			s = "CLASS_INHERITANCE";
			break;
		default:
			s = "UNKNOWN";
			break;
		}
		return s;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReferenceEdge other = (ReferenceEdge) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return from.toString() + "->" + to.toString() + "[" + count + "]";
	}
}
