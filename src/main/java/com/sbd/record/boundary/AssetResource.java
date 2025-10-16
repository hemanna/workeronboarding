package com.sbd.record.boundary;

import com.sbd.common.Jsonb.AssetJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.AssetControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/assets")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class AssetResource {
    private final AssetControl assetControl;

    @POST
    @Path("/create")
    public Response createAsset(@BeanParam AssetJsonb assetRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        ApiResponse response = assetControl.createAsset(assetRequest, requestId);
        return Response.ok(response).build();
    }

}
