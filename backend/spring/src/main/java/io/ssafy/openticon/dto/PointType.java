package io.ssafy.openticon.dto;

public enum PointType {
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    PURCHASE("구입"),
    SALE("판매");

    private final String name;

    PointType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static PointType nameOf(String name) {
        for (PointType pointType : PointType.values()) {
            if (pointType.getName().equals(name)) {
                return pointType;
            }
        }
        return null;
    }
}
