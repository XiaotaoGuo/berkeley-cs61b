import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    /**
     * The root upper left/lower right longitudes and latitudes represent the bounding box of
     * the root tile, as the images in the img/ folder are scraped.
     * Longitude == x-axis; latitude == y-axis.
     */
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    /** Each tile is 256x256 pixels. */
    public static final int TILE_SIZE = 256;
    public static double L0LonDPP, LOLATDPP;

    public Rasterer() {
        // TODO: YOUR CODE HERE
        L0LonDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        LOLATDPP = (ROOT_ULLAT - ROOT_LRLAT) / TILE_SIZE;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        if (!checkBoundary(params) || !checkValidParams(params)) {
            results.put("query_success", false);
            return results;
        }


        double requiredLonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        double ullon = Math.max(params.get("ullon"), ROOT_ULLON);
        double lrlon = Math.min(params.get("lrlon"), ROOT_LRLON);
        double ullat = Math.max(params.get("ullat"), ROOT_ULLAT);
        double lrlat = Math.min(params.get("ullat"), ROOT_LRLAT);

        int currentLevel = 0;
        double currentLonDPP = L0LonDPP;
        double currentLatDPP = LOLATDPP;

        while (currentLonDPP > requiredLonDPP && currentLevel < 7) {
            currentLevel++;
            currentLonDPP /= 2;
            currentLatDPP /= 2;
        }

        int colBeginIndex = findTileIndex(ROOT_ULLON, ullon, currentLonDPP * TILE_SIZE);
        int colEndIndex = findTileIndex(ROOT_ULLON, lrlon, currentLonDPP * TILE_SIZE);
        int rowBeginIndex = findTileIndex(ROOT_ULLAT, ullat, currentLatDPP * TILE_SIZE);
        int rowEndIndex = findTileIndex(ROOT_ULLAT, lrlat, currentLatDPP * TILE_SIZE);

        results.put("raster_ul_lon", getPos(ROOT_ULLON, colBeginIndex, currentLonDPP * TILE_SIZE));
        results.put("raster_ul_lat", getPos(ROOT_ULLAT, rowBeginIndex, -1 * currentLatDPP * TILE_SIZE));
        results.put("raster_lr_lon", getPos(ROOT_ULLON, colEndIndex + 1, currentLonDPP * TILE_SIZE));
        results.put("raster_lr_lat", getPos(ROOT_ULLAT, rowEndIndex + 1, -1 * currentLatDPP * TILE_SIZE));
        results.put("query_success", true);
        results.put("depth", currentLevel);

        String[][] render_grid = new String[rowEndIndex - rowBeginIndex + 1][colEndIndex - colBeginIndex + 1];
        for (int i = rowBeginIndex; i <= rowEndIndex; i++) {
            for (int j = colBeginIndex; j <= colEndIndex; j++) {
                render_grid[i - rowBeginIndex][j - colBeginIndex] = "d" + currentLevel + "_x" + j + "_y" + i + ".png";
            }
        }

        results.put("render_grid", render_grid);

        /*
        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                           + "your browser.");
        */
        return results;

    }

    private int findTileIndex(double begin, double target, double width) {
        return (int)(Math.abs(target - begin) / width);
    }

    private double getPos(double begin, int index, double width) {
        return begin + index * width;
    }

    private boolean checkBoundary(Map<String, Double> params) {
        return params.get("ullon") < ROOT_LRLON ||
                params.get("lrlon") > ROOT_ULLON ||
                params.get("ullat") > ROOT_LRLAT ||
                params.get("lrlat") < ROOT_ULLAT;
    }

    private boolean checkValidParams(Map<String, Double> params) {
        return params.get("ullon") < params.get("lrlon") &&
                params.get("ullat") > params.get("lrlat");
    }

}
