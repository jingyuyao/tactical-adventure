package com.jingyuyao.tactical.model.item;

public class Item {
    private final String name;
    private int usageLeft;

    public Item(String name, int usageLeft) {
        this.name = name;
        this.usageLeft = usageLeft;
    }

    public String getName() {
        return name;
    }

    public int getUsageLeft() {
        return usageLeft;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", usageLeft=" + usageLeft +
                '}';
    }
}
