package myblog.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myblog.model.dao.BlogDao;
import myblog.model.entity.BlogEntity;

@Service
public class BlogService {
	@Autowired
	private BlogDao blogDao;
	
	
	public List<BlogEntity> findAllBlogPost(Long userId){
		if(userId == null) {
			return null;
		}else {
			return blogDao.findByUserId(userId);
		}
	}
	
	//新しいブログを書く、重複にも構いません
	public boolean createBlogPost(String blogTitle,
			String blogContents,String blogAttribute,
			String blogImgUrl,Long userId) {
		
		BlogEntity blogList = blogDao.findByBlogTitle(blogTitle);
		// blogDao.save(new BlogEntity(blogTitle,registerDate,blogContents,blogAttribute,
		// 		 blogImgUrl,userId));
		
		if(blogList == null) { 
			blogDao.save(new BlogEntity(blogTitle,blogContents,blogAttribute,blogImgUrl,userId)); 
			return true; 
		}else {
			return false; }
		 
	}
	
	public BlogEntity getBlogPost(Long blogId) {
		if(blogId == null) {
			return null;
		}else {
			return blogDao.findByBlogId(blogId);
		}
	}
	
	
	//EDIT POST
	public boolean editBlogPost(String blogTitle,
			LocalDate registerDate,String blogContents,
			String blogAttribute,String blogImgUrl,Long userId,Long blogId) {
		BlogEntity blogList = blogDao.findByBlogId(blogId);
		if(userId == null) {
			return false;
		}else {
			blogList.setBlogId(blogId);
			blogList.setBlogAttribute(blogAttribute);
			blogList.setBlogContents(blogContents);
			blogList.setBlogTitle(blogTitle);
			blogList.setBlogImgUrl(blogImgUrl);
			blogList.setUserId(userId);
			blogDao.save(blogList);
			return true;
		}
	}
	
	//画像処理
//	public boolean editBlogImage(Long blogId,String) {
	
	//削除
	public boolean deleteBlog(Long blogId) {
		if(blogId == null) {
			return false;
		}else {
			blogDao.deleteByBlogId(blogId);
			return true;
		}
	}
	
}
