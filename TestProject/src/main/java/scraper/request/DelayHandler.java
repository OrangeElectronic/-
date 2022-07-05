package scraper.request;

import scraper.model.PageInfo;

public interface DelayHandler {
    void onEachRequest();
    void onNextPage(int currentPage, int pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor);
}
