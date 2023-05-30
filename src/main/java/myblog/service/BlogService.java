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
	
	//findAllBlogPost
	public List<BlogEntity> findAllBlogPost(Long userId){
		if(userId == null) {
			return null;
		}else {
			return blogDao.findByUserId(userId);
		}
	}
	//find Attribute
	public List<BlogEntity> findBlogByAttribute(String blogAttribute) {
		if (blogAttribute == null) {
			return null;
		} else {
			return blogDao.findByBlogAttribute(blogAttribute);
		}
	}

	public List<BlogEntity> findByUserIdAndBlogAttribute(Long userId, String attribute) {
		if (userId == null || attribute == null) {
			return null;
		} else {
			return blogDao.findByUserIdAndBlogAttribute(userId, attribute);
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
	
	// public boolean editBlogImage(Long blogId,String fileName,Long userId) {
	// 	BlogEntity blogList = blogDao.findByBlogId(blogId);
	// 	if(fileName == null || blogList.getBlogImgUrl().equals(fileName)) {
	// 		return false;
	// 	}else {
	// 		/**
	// 		 * blogList.setBlogId(blogId);: ブログIDを設定します。
	// 		 * blogList.setBlogImage(fileName);: 新しい画像ファイル名を設定します。
	// 		 * blogList.setUserId(userId);: ユーザーIDを設定します。
	// 		 * blogDao.save(blogList);: ブログ情報を更新して保存します。
	// 		 * return true;: 画像の編集に成功した場合、trueを返します。**/
	// 		blogList.setBlogId(blogId);
	// 		blogList.setBlogImgUrl(fileName);
	// 		blogList.setUserId(userId);
	// 		blogDao.save(blogList);
	// 		return true;
	// 	}
	// }
	
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
