package com.arisu.mungexambro;


interface OnProcessedListener{
    void onProcessedCheckUpdate(final String Result);
    void onProcessedDlUpdate();
    void onFinishedDlUpdate();
}
public abstract class AbstractOnProcessedListener implements OnProcessedListener{
    @Override
    public void onProcessedCheckUpdate(String Result) {}

    @Override
    public void onProcessedDlUpdate() {}

    @Override
    public void onFinishedDlUpdate() {}
}
