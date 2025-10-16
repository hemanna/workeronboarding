package com.sbd.record.control;

import com.sbd.common.Jsonb.AssetJsonb;
import com.sbd.common.entity.AssetRequest;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;

public interface AssetControl {
    ApiResponse createAsset
            (AssetJsonb assetRequest,
             String requestId)
            throws BusinessException;

}
