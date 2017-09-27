package com.mobile2016.backend.service;

import com.mobile2016.backend.model.Article;
import com.mobile2016.backend.model.Category;
import com.mobile2016.backend.service.mapper.ArticleMapper;
import com.mobile2016.backend.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

	@Autowired
	private ArticleMapper articleMapper;

	public  List<Article> list(Article article) {
		return articleMapper.list(article);
	}

	public Integer count(Article article){
		return articleMapper.count(article);
	}

	public void delById(Article article){
		articleMapper.updateState(article);
	}

	public Article findById(int id){
		return articleMapper.findById(id);
	}

	public void insert(Article article){
		articleMapper.insert(article);
	}

	public void updateArticle(Article article){
		articleMapper.updateArticle(article);
	}

}
