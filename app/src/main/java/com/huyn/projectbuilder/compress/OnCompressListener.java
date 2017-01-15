package com.huyn.projectbuilder.compress;

public interface OnCompressListener {

    /**
     * Fired when the compression is started, override to handle in your own code
     */
    void onStart();

    /**
     * Fired when a compression returns successfully, override to handle in your own code
     */
    void onSuccess(String filePath);

    /**
     * Fired when a compression fails to complete, override to handle in your own code
     */
    void onError();
}
