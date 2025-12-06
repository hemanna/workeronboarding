package com.sbd.record.boundary;

import com.sbd.common.Jsonb.AssetAssignJsonb;
import com.sbd.common.Jsonb.AssetJsonb;
import com.sbd.common.Jsonb.AssetListRequest;
import com.sbd.common.Jsonb.AssetTypeJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.AssetControl;
import com.sbd.record.control.service.AssetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
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

    @Inject
    AssetService assetService;
    private final AssetControl assetControl;

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveAsset(
            @Context HttpHeaders httpHeaders,
            @BeanParam AssetJsonb assetJsonb
    ) throws BusinessException {
        ApiRequest<AssetJsonb> apiRequest = new ApiRequest<>();
        apiRequest.setData(assetJsonb);
        String correlationId = UUID.randomUUID().toString();
        ApiResponse response = assetControl.createAsset(correlationId, apiRequest);
        return Response.ok(response).build();
    }




    @GET
    @Path("/get_all_assets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAllAssets() throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all assets", requestId);

        return assetService.fetchAllAssets(requestId);
    }

    @GET
    @Path("/get_count_assets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchCountAssets() throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all assets", requestId);

        return assetService.fetchCountAssets(requestId);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_assets_by_type")
    public ApiResponse fetchAssetsByType() throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | fetching all assets grouped by type", requestId);
        return assetControl.fetchAssetByType(requestId);
    }

    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assetList(@Context HttpHeaders httpHeaders,
                              ApiRequest<AssetListRequest> apiRequest) throws BusinessException {

        String correlationId = UUID.randomUUID().toString();
        ApiResponse response = assetControl.listAssets(correlationId,apiRequest);
        return Response.ok(response).build();
    }

    @POST
    @Path("/save_asset_type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAssetType(@Context HttpHeaders httpHeaders,
                              ApiRequest<AssetTypeJsonb> apiRequest) throws BusinessException {

        String correlationId = UUID.randomUUID().toString();
        ApiResponse response = assetControl.createAssetType(correlationId,apiRequest);
        return Response.ok(response).build();
    }

    @POST
    @Path("/assign_asset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response AssignAsset(@Context HttpHeaders httpHeaders,
                                  ApiRequest<AssetAssignJsonb> apiRequest) throws BusinessException {

        String correlationId = UUID.randomUUID().toString();
        ApiResponse response = assetControl.createAssignasset(correlationId,apiRequest);
        return Response.ok(response).build();
    }
}
