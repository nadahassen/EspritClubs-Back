package com.stage.spring.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.stage.spring.entity.Image;

public interface IServiceImage {

	Image addImage(Image I);
	Image retrieveImage(Long id);
	
	 void save(MultipartFile file);
	 void saveAll(List<MultipartFile> file);
}
