package mall.search.service;

import mall.common.pojo.SearchResult;

public interface SearchService {
	SearchResult search(String keyword,int page,int rows) throws Exception;
}
