package mall.search.mapper;

import java.util.List;

import mall.common.pojo.SearchItem;

public interface ItemMapper {
	List<SearchItem> getItemList();
	SearchItem getItemById(long itemid);
}
