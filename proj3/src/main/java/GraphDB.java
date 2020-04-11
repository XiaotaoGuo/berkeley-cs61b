import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    static class Node {
        public Long id;
        public Double lon, lat;

        public Set<Long> edges;
        public Set<Long> neighbors;
        public String name;

        public double distanceToStart;
        public double distanceToGoal;
        public boolean visited;
        public Long prev;

        Node(Long id, Double lon, Double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            edges = new HashSet<>();
            neighbors = new HashSet<>();
        }
    }

    static class Way {
        public Long id;
        public List<Long> nodes;
        public boolean isValid;
        public String name;
        public String maxspeed;
        Way(Long id) {
            this.id = id;
            nodes = new ArrayList<>();
        }

    }

    private class TrieNode {
        char c;
        boolean isWord;
        Map<Character, TrieNode> nextLevel;
        Set<String> fullnames;
        TrieNode() {
            nextLevel = new HashMap<>();
            fullnames = new HashSet<>();
        }
    }

    private Set<Long> nodeids = new HashSet<>();
    private Map<Long, Node> nodes = new HashMap<>();
    private Map<String, ArrayList<Node>> locations = new HashMap<>();
    private Map<Long, Way> ways = new HashMap<>();
    private TrieNode head = new TrieNode();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public void addNodeId(Long id) {
        if (nodeids.contains(id)) {
            return;
        }
        nodeids.add(id);
    }

    public void addNode(Node n) {
        if (nodes.containsKey(n.id)) {
            return;
        }
        nodes.put(n.id, n);
    }

    public void addLocations(Node n) {
        if (!locations.containsKey(cleanString(n.name))) {
            locations.put(cleanString(n.name), new ArrayList<>());
        }
        locations.get(cleanString(n.name)).add(n);
    }

    public void addWay(Way way) {
        if (!way.isValid || ways.containsKey(way.id)) {
            return;
        }
        ways.put(way.id, way);
    }

    public Node getNode(Long id) {
        if (!nodes.containsKey(id)) {
            return null;
        }
        return nodes.get(id);
    }

    public void routeInitialize() {
        for (Long id: nodes.keySet()) {
            Node n = getNode(id);
            n.prev = -1L;
            n.visited = false;
            n.distanceToStart = -1;
        }
    }

    private void buildTrie() {
        for (String name : locations.keySet()) {
            TrieNode curr = head;
            for (int i = 0; i < name.length(); i++) {
                Character c = name.charAt(i);
                if (!curr.nextLevel.containsKey(c)) {
                    curr.nextLevel.put(c, new TrieNode());
                }
                curr = curr.nextLevel.get(c);
            }
            curr.isWord = true;
            for (Node n : locations.get(name)) {
                curr.fullnames.add(n.name);
            }


        }
    }

    public ArrayList<String> autocomplete(String prefix) {
        ArrayList<String> results = new ArrayList<>();
        TrieNode curr = head;
        for (int i = 0; i < prefix.length(); i++) {
            Character c = prefix.charAt(i);
            if (!curr.nextLevel.containsKey(c)) {
                return results;
            }
            curr = curr.nextLevel.get(c);
        }

        autocompleteHelper(curr, prefix, results);

        return results;
    }

    private void autocompleteHelper(TrieNode curr, String prefix, ArrayList<String> results) {
        if (curr.isWord) {
            for (String name : curr.fullnames) {
                results.add(name);
            }
        }
        for (Character c : curr.nextLevel.keySet()) {
            autocompleteHelper(curr.nextLevel.get(c), prefix + c, results);
        }
    }

    public ArrayList<Node> getLocation(String name) {
        return locations.get(name);
    }

    public Comparator<Node> comparator = new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            double diff = o1.distanceToStart + o1.distanceToGoal
                    - (o2.distanceToStart + o2.distanceToGoal);
            diff = o1.distanceToStart
                    - o2.distanceToStart;
            return diff < 0 ? -1 : 1;
        }
    };

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        for (Long id : nodeids) {
            Node n = getNode(id);
            if (n.neighbors.size() == 0) {
                nodes.remove(n.id);
            }
        }
        buildTrie();
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> results = new ArrayList<>();
        for (Long id : nodes.keySet()) {
            results.add(id);
        }
        return results;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node n = getNode(v);
        ArrayList<Long> results = new ArrayList<>();
        for (Long id : n.neighbors) {
            results.add(id);
        }

        return results;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double mindistance = -1;
        Long id = 0L;
        for (Node n : nodes.values()) {
            double currentDistance = distance(n.lon, n.lat, lon, lat);
            if (mindistance == -1 || mindistance > currentDistance) {
                mindistance = currentDistance;
                id = n.id;
            }
        }
        return id;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" : Number, The latitude of the node. <br>
     * "lon" : Number, The longitude of the node. <br>
     * "name" : String, The actual name of the node. <br>
     * "id" : Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> results = new ArrayList<>();
        ArrayList<GraphDB.Node> candidates = getLocation(cleanString(locationName));
        for (GraphDB.Node n : candidates) {
            Map<String, Object> m = new HashMap<>();
            m.put("lat", n.lat);
            m.put("lon", n.lon);
            m.put("name", n.name);
            m.put("id", n.id);
            results.add(m);
        }
        return results;
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {

        ArrayList<String> results = autocomplete(prefix);

        return results;
    }
}
