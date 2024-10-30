package io.ssafy.openticon.dto;

public enum ReportStateType {
    PENDING("접수전"),
    RECEIVED("접수완료"),
    APPROVED("통과"),
    REJECTED("반려");

    private final String name;

    ReportStateType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ReportStateType nameOf(String name) {
        for (ReportStateType reportStateType : ReportStateType.values()) {
            if (reportStateType.getName().equals(name)) {
                return reportStateType;
            }
        }
        return null;
    }
}
