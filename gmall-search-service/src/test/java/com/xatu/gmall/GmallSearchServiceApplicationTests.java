package com.xatu.gmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PmsSearchSkuInfo;
import com.xatu.gmall.entity.PmsSkuInfo;
import com.xatu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {
    @Autowired
    JestClient jestClient;
    @Reference
    SkuService skuService;
    @Test
    public void contextLoads() throws IOException {
        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
        pmsSkuInfoList = skuService.selectAllSku("61");
        //转换为es数据结构
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo=new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setProductId(pmsSkuInfo.getSpuId());
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
        }
        //导入es
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            String idStr = pmsSearchSkuInfo.getId().toString();
            Index put = new Index.Builder(pmsSearchSkuInfo).index("com.xatu.gmall").type("PmsSkuInfo").id(idStr).build();
            jestClient.execute(put);
        }

    }

}


