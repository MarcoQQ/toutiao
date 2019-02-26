package com.marco.toutiao.service;

import com.marco.toutiao.dao.NewsDAO;
import com.marco.toutiao.model.News;
import com.marco.toutiao.util.ToutiaoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int doPos = file.getOriginalFilename().lastIndexOf('.');
        if(doPos < 0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(doPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileFollowed(fileExt)){
            return null;
        }else{
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMG_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;

        }
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int id){
        return newsDAO.selectById(id);
    }

    public void updateCommentCount(int id, int commentCount){
        newsDAO.updateCommentCount(id, commentCount);
    }

    public void updateLikeCount(int id, int likeCount){
        newsDAO.updateLikeCount(id, likeCount);
    }
}
