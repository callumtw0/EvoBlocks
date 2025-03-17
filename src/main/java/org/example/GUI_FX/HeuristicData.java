package org.example.GUI_FX;

public class HeuristicData {
    int id;
    String name;
    String displayName;
    String description;
    String category;

    public HeuristicData(int id, String name, String displayName, String category, String description) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Heuristic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
