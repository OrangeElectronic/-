package scraper.request;

import scraper.Endpoint;
import scraper.mapper.Mapper;
import scraper.model.Account;
import scraper.model.PageInfo;
import scraper.model.PageObject;
import scraper.request.parameters.UserParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetFollowersRequest extends PaginatedRequest<PageObject<Account>, UserParameter> {

    public GetFollowersRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(UserParameter requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getFollowersLinkVariables(requestParameters.getUserId(), 200, pageInfo.getEndCursor()))
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
        return getMapper().mapFollowers(jsonStream);
    }
}
