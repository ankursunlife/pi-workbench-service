package com.aarete.pi.bean;

import lombok.Data;

@Data
public class ClaimLineCount {
    private long groupQueueCount;

    private long myQueueCount;

    private long pendCount;

    private long waitingCount;
    
    private long totalCountExcludingClosed;
}
