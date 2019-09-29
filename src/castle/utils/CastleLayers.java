package castle.utils;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CastleLayers {

    private Location origin;
    private int length;
    private int width;
    private double spacing;

    public CastleLayers(Location origin, int length, int width, double spacing) {
        this.origin = origin;
        this.length = length;
        this.width = width;
        this.spacing = spacing;
    }

    public Location kingLocation(double startOffsetY) {
        return this.origin.clone().add(
                (this.spacing * (this.length/2)) + this.spacing,
                startOffsetY + (this.spacing*4) - (this.spacing/2),
                (this.spacing * (this.width/2)) + this.spacing
        );
    }


    public List<Location> layers(double startOffsetY) {
        double offsetY = startOffsetY;

        List<List<Location>> layers = new ArrayList<>();
        List<Location> centerBlocks = new ArrayList<>();

        //add all layers with increasing offset
        layers.add(base(offsetY));
        offsetY += this.spacing;
        layers.add(wall(offsetY));
        centerBlocks.add(centerBlock(offsetY));
        offsetY += this.spacing;
        layers.add(wall(offsetY));
        offsetY += this.spacing;
        layers.add(top(offsetY));

        //add the center blocks (platform for the king) to layers
        layers.add(centerBlocks);

        //flatten all layers into one list of locations
        List<Location> flattenedLayers = layers.stream().flatMap(List::stream).collect(Collectors.toList());

        return flattenedLayers;
    }

    private List<Location> base(double offsetY) {
        List<Location> locations = new ArrayList<>();

        //loop for a flat area based on length + width
        for (int length = 1; length <= this.length; length += 1) {
            for (int width = 1; width <= this.width; width += 1) {
                locations.add(this.origin.clone().add(this.spacing * length, offsetY, this.spacing * width));
            }
        }

        return locations;
    }

    private List<Location> wall(double offsetY) {
        List<Location> locations = new ArrayList<>();

        //loop for a railing on either side

        for (int length = 1; length <= this.length; length += 1) {
            locations.add(this.origin.clone().add(this.spacing * length, offsetY, this.spacing * 1));
            locations.add(this.origin.clone().add(this.spacing * length, offsetY, this.spacing * this.width));
        }

        //loop for railing on z coords. Skipping 2 on either side as they are already added.
        for (int width = 2; width <= this.width - 1; width += 1) {
            locations.add(this.origin.clone().add(this.spacing * 1, offsetY, this.spacing * width));
            locations.add(this.origin.clone().add(this.spacing * this.length, offsetY, this.spacing * width));
        }

        return locations;
    }


    private List<Location> top(double offsetY) {
        List<Location> locations = new ArrayList<>();

        //loop for a railing on either side in castle top style

        for (int length = 1; length <= this.length; length += 2) {
            locations.add(this.origin.clone().add(this.spacing * length, offsetY, this.spacing * 1));
            locations.add(this.origin.clone().add(this.spacing * length, offsetY, this.spacing * this.width));
        }

        //same as above
        for (int width = 1; width <= this.width - 1; width += 2) {
            locations.add(this.origin.clone().add(this.spacing * 1, offsetY, this.spacing * width));
            locations.add(this.origin.clone().add(this.spacing * this.length, offsetY, this.spacing * width));
        }

        return locations;
    }

    private Location centerBlock(double offsetY) {
        return this.origin.clone().add(
                (this.spacing * (this.length/2)) + this.spacing,
                offsetY,
                (this.spacing * (this.width/2)) + this.spacing
        );
    }

}
