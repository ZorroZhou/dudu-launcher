package com.wow.carlauncher.repertory.server;

import com.wow.carlauncher.repertory.server.response.BaseResult;

public interface CommonCallback<T> {
    void callback(BaseResult<T> res);
}
