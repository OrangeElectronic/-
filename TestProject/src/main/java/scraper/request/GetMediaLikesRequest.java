package scraper.request;

import scraper.Endpoint;
import scraper.mapper.Mapper;
import scraper.model.Account;
import scraper.model.PageInfo;
import scraper.model.PageObject;
import scraper.request.parameters.MediaCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetMediaLikesRequest extends PaginatedRequest<PageObject<Account>, MediaCode> {

    public GetMediaLikesRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(MediaCode requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getLikesByShortcode(requestParameters.getShortcode(),200, pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(PageObject<Account> result, PageObject<Account> current) {
        result.getNodes().addAll(current.getNodes());
        result.setPageInfo(current.getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(PageObject<Account> current) {
        return current.getPageInfo();
    }

    @Override
    protected PageObject<Account> mapResponse(InputStream jsonStream) {
        return getMapper().mapLikes(jsonStream);
    }
}
