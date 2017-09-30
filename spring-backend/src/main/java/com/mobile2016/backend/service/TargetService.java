package com.mobile2016.backend.service;

import com.mobile2016.backend.mapper.TargetMapper;
import com.mobile2016.backend.model.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TargetService {

	@Autowired
	private TargetMapper targetMapper;

	@Transactional(readOnly = true)
	public  List<Target> list(Target category) {
		return targetMapper.list(category);
	}

	@Transactional(readOnly = true)
	public Integer count(){
		return targetMapper.count();
	}

	public void delById(Target category){
		targetMapper.updateState(category);
	}

	@Transactional(readOnly = true)
	public Target findById(int id){
		return targetMapper.findById(id);
	}

	public void insert(Target category){
		targetMapper.insert(category);
	}

	public void updateCategory(Target category){
		targetMapper.updateCategory(category);
	}

}
