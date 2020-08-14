

package com.james.cache.service;

import java.util.List;
import java.util.Map;

public interface ArticleService {
	
	List<Map> queryArticleVoteByPostTime(String articleId);
}