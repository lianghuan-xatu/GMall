package com.xatu.gmall.service;

import com.xatu.gmall.entity.PmsSearchParam;
import com.xatu.gmall.entity.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService
{

    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
