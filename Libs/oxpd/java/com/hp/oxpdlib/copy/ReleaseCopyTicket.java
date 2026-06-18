
package com.hp.oxpdlib.copy;

public class ReleaseCopyTicket {
    public String jobId;

    public ReleaseCopyOptions releaseCopyOptions;

    /**
     * Default no-arg constructor
     *
     */
    public ReleaseCopyTicket() {
        super();
    }

    /**
     * Fully-initialising value constructor
     *
     * @param releaseCopyOptions
     *     the CopyOptions
     */
    public ReleaseCopyTicket(final String jobId, final ReleaseCopyOptions releaseCopyOptions) {
        this.jobId = jobId;
        this.releaseCopyOptions = releaseCopyOptions;
    }
}
