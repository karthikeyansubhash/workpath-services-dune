/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.model;

public class ParsedName {
    private final String filename;
    private final String job;
    private final String date;
    private final String time;
    private final Integer page;
    private final Integer total;
    private final String ext;

    public ParsedName(String filename, String job, String date, String time,
                      Integer page, Integer total, String ext) {
        this.filename = filename;
        this.job = job;
        this.date = date;
        this.time = time;
        this.page = page;
        this.total = total;
        this.ext = ext;
    }

    public String getFilename() {
        return filename;
    }

    public String getJob() {
        return job;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal() {
        return total;
    }

    public String getExt() {
        return ext;
    }
}

