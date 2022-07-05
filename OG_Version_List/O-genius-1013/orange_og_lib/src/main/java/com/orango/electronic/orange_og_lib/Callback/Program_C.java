package com.orango.electronic.orange_og_lib.Callback;

public interface Program_C {
    //燒錄過程
    void Program_Progress(int i);
    //燒錄結果
    void Program_Finish(boolean result);
    //無法燒錄的原因
    void prograam_NotProceed(int type);
}
