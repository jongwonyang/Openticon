package io.ssafy.openticon.dto;

public enum ReportType {
    REPORT("신고"),
    EXAMINE("심사");

    private final String name;

    ReportType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ReportType nameOf(String name) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.getName().equals(name)) {
                return reportType;
            }
        }
        return null;
    }
}
