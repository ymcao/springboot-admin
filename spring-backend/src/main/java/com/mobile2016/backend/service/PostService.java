package com.mobile2016.backend.service;

import com.mobile2016.backend.model.Post;
import com.mobile2016.backend.mapper.PostMapper;
import com.sun.tools.javac.api.ClientCodeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

	@Autowired
	private PostMapper postMapper;

	@Transactional(readOnly = true)
	public  List<Post> list(Post article) {
		return postMapper.list(article);
	}

	@Transactional(readOnly = true)
	public Integer count(Post article){
		return postMapper.count(article);
	}

	public void delById(Post article){
		postMapper.updateState(article);
	}

	@Transactional(readOnly = true)
	public Post findById(int id){
		return postMapper.findById(id);
	}

	public void insert(Post article){
		postMapper.insert(article);
	}

	public void updateArticle(Post article){
		postMapper.updateArticle(article);
	}

}
