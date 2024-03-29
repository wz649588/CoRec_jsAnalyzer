package vt.edu.graph;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;

import com.google.gson.Gson;

import vt.edu.graph.GraphDataWithNodesJson;
import vt.edu.graph.GraphDataJson.GraphEdge;
import vt.edu.graph.GraphDataJson.GraphNode;
import vt.edu.sql.SqliteManager;

public class LMatchSummary {
	private String largestMatchTable;
	
	private String patternTable;
	
	private String matchWithPatternTable;
	
	public LMatchSummary(String largestMatchTable, String patternTable, String matchWithPatternTable) {
		this.largestMatchTable = largestMatchTable;
		this.patternTable = patternTable;
		this.matchWithPatternTable = matchWithPatternTable;
	}
	
	public void execute() throws SQLException {
		Connection conn = SqliteManager.getConnection();
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("DROP TABLE IF EXISTS " + matchWithPatternTable);
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + matchWithPatternTable
				+ " (pattern_id INTEGER,bug_name TEXT,graph_num INTEGER,pattern_shape TEXT,"
				+ "node_map TEXT,node_num INTEGER,edge_num INTEGER)");
		
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + largestMatchTable);
		int totalRow = 0;
		if (rs.next())
			totalRow = rs.getInt(1);
		rs.close();
		stmt.close();
		conn.close();
		
		Gson gson = new Gson();
		
		for (int offset = 0; offset < totalRow; offset++) {
			System.out.println(offset + " / " + totalRow);
			conn = SqliteManager.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT bn1,gn1,subgraph1 FROM " + largestMatchTable + " LIMIT 1 OFFSET " + offset);
			String bugName = rs.getString("bn1");
			int graphNum = rs.getInt("gn1");
			String jsonGraph = rs.getString("subgraph1");
			stmt.close();
			conn.close();
			
			Graph<GraphNode, GraphEdge> graph = gson.fromJson(jsonGraph, GraphDataWithNodesJson.class).getJgrapht();
			if (graph.edgeSet().isEmpty())
				continue;
			int patternId = checkPatternId(graph);
			if (patternId == -1)
				continue;
			
			Map<String, String> nodeMap = getNodeMap(graph, patternId);
			conn = SqliteManager.getConnection();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + matchWithPatternTable
					+ " (pattern_id,bug_name,graph_num,pattern_shape,node_map,node_num,edge_num) "
					+ "VALUES (?,?,?,?,?,?,?)");
			ps.setInt(1, patternId);
			ps.setString(2, bugName);
			ps.setInt(3, graphNum);
			ps.setString(4, getPatternShapeJson(patternId));
			ps.setString(5, gson.toJson(nodeMap));
			ps.setInt(6, graph.vertexSet().size());
			ps.setInt(7, graph.edgeSet().size());
			ps.executeUpdate();
			ps.close();
			conn.close();
		}
	}
	
	public static void main(String[] args) throws SQLException {
//		String[] projects = {"webpack", "electron", "habitica", "meteor", "Ghost", "serverless", "nodejs", "phaser", "storybook", "three"};
//		String[] projects = {"webpack1", "electron", "habitica", "meteor", "Ghost", "serverless", "node", "react", "storybook", "pdf"};
		String[] projects = {"atom", "webpack1", "electron", "node", "Ghost", "storybook", "pdf"};
		for (String project: projects) {
			if (!project.equals("node")) continue;
			String largestMatchTable = "em_largest_notest_revision_" + project;
			String patternTable = "em_largest_match_final_revision_" + project;
			String matchWithPatternTable = "em_largest_match_with_pattern_final_revision_" + project;
			LMatchSummary summary = new LMatchSummary(largestMatchTable, patternTable, matchWithPatternTable);
			summary.execute();
		}
	}
	
	private static Comparator<GraphNode> vertexComparator = new Comparator<GraphNode>(){
		@Override
		public int compare(GraphNode n1, GraphNode n2) {
			return n1.getType() - n2.getType();
		}
	};
	
	private static Comparator<GraphEdge> edgeComparator = new Comparator<GraphEdge>(){
		@Override
		public int compare(GraphEdge e1, GraphEdge e2) {
			return e1.getType() - e2.getType();
		}
	};
	
	private int checkPatternId(Graph<GraphNode, GraphEdge> graph) throws SQLException {
		Connection conn = SqliteManager.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT pattern_id, shape FROM " + patternTable);
		Gson gson = new Gson();
		while (rs.next()) {
			int patternId = rs.getInt("pattern_id");
			String jsonShape = rs.getString("shape");
			Graph<GraphNode, GraphEdge> shape = gson.fromJson(jsonShape, GraphDataWithNodesJson.class).getJgrapht();
			if (graph.vertexSet().size() != shape.vertexSet().size() ||
					graph.edgeSet().size() != shape.edgeSet().size()) {
				continue;
			}
			if (LMatchPatternExtraction.isGraphASubgraphOfGraphB(shape, graph) &&
					LMatchPatternExtraction.isGraphASubgraphOfGraphB(graph, shape)) {
				stmt.close();
				conn.close();
				return patternId;
			}
		}
		stmt.close();
		conn.close();
		return -1;
	}
	
	private String getPatternShapeJson(int patternId) throws SQLException {
		Connection conn = SqliteManager.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT shape FROM " + patternTable + " WHERE pattern_id=" + patternId);
		String jsonShape = null;
		if (rs.next())
			jsonShape = rs.getString(1);
		
		stmt.close();
		conn.close();
		return jsonShape;
	}
	
	private Map<String, String> getNodeMap(Graph<GraphNode, GraphEdge> graph, int patternId) throws SQLException {
		String jsonShape = getPatternShapeJson(patternId);
		Gson gson = new Gson();
		Graph<GraphNode, GraphEdge> shape = gson.fromJson(jsonShape, GraphDataWithNodesJson.class).getJgrapht();
		
		Map<String, String> nodeMap = null;
		VF2SubgraphIsomorphismInspector<GraphNode,GraphEdge> isoInspector =
				new VF2SubgraphIsomorphismInspector<>(shape, graph, vertexComparator, edgeComparator);
		Iterator<GraphMapping<GraphNode, GraphEdge>> isoIter = isoInspector.getMappings();
		
		while (isoIter.hasNext()) {
			nodeMap = new HashMap<>();
			GraphMapping<GraphNode, GraphEdge> mapping = isoIter.next();
			boolean hasUnmatchedNode = false;
			for (GraphNode shapeNode: shape.vertexSet()) {
				GraphNode graphNode = mapping.getVertexCorrespondence(shapeNode, true);
				if (graphNode == null) {
					hasUnmatchedNode = true;
					break;
				}
				nodeMap.put(shapeNode.getName(), graphNode.getName());
			}
			if (hasUnmatchedNode)
				nodeMap = null;
			else
				return nodeMap;
		}
		return null;
	}
}
