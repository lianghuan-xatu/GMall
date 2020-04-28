package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xatu.gmall.entity.PmsSearchParam;
import com.xatu.gmall.entity.PmsSearchSkuInfo;
import com.xatu.gmall.entity.PmsSkuAttrValue;
import com.xatu.gmall.entity.PmsSkuSaleAttrValue;
import com.xatu.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class SearchServiceImpl implements SearchService
{

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        String searchDsl = getSearchDsl(pmsSearchParam);
        Search search = new Search.Builder(searchDsl).addIndex("gmall").addType("PmsSkuInfo").build();
        SearchResult execute = null;
        try{
             execute = jestClient.execute(search);
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<PmsSearchSkuInfo> pmsSearchSkuInfoArrayList = new ArrayList<>();
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = null;
               hits = execute.getHits(PmsSearchSkuInfo.class);
               if(hits!=null){

                   for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                       PmsSearchSkuInfo source= hit.source;
                       Map<String,List<String>> hightlight = hit.highlight;
                       if(hightlight!=null){
                           String skuName = hightlight.get("skuName").get(0);
                           source.setSkuName(skuName);
                       }

                       pmsSearchSkuInfoArrayList.add(source);
                   }
               }

        return pmsSearchSkuInfoArrayList;
    }



    public String getSearchDsl(PmsSearchParam pmsSearchParam){
        List<PmsSkuAttrValue> skuAttrValueList = pmsSearchParam.getSkuAttrValueList();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        if(StringUtils.isNotBlank(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(skuAttrValueList!=null){
            for(PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList){
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.id",pmsSkuAttrValue.getValueId());
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        //must
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        searchSourceBuilder.query(boolQueryBuilder);
        //highlight
        HighlightBuilder highlightBuilder =new HighlightBuilder();
        highlightBuilder.field("skuName");
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlight(highlightBuilder);
        searchSourceBuilder.highlight(highlightBuilder);
        //sort
        searchSourceBuilder.sort("id", SortOrder.DESC);
        return searchSourceBuilder.toString();
    }
}
