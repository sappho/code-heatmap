package uk.org.sappho.code.heatmap.selection;

public class HeatMapMapping {

    private final String name;
    private final String item;

    public HeatMapMapping(String name, String item) {

        this.item = item;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getItem() {
        return item;
    }
}
