package com.hp.workpath.api.statistics;

import java.util.List;

public class StatisticsJobs {
    private List<String> memberIds;
    private List<StatisticsJobData> members;
    private int offset;
    private int selectedCount;
    private int totalCount;
    private int totalJobEntries;

    public java.util.List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public java.util.List<StatisticsJobData> getMembers() {
        return members;
    }

    public void setMembers(List<StatisticsJobData> members) {
        this.members = members;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalJobEntries() {
        return totalJobEntries;
    }

    public void setTotalJobEntries(int totalJobEntries) {
        this.totalJobEntries = totalJobEntries;
    }
}
