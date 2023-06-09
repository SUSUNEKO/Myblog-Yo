package myblog.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import myblog.model.entity.BlogEntity;

public interface BlogDao extends JpaRepository<BlogEntity,Long> {
	
	List<BlogEntity> findByUserId(Long userId);
	
	BlogEntity save (BlogEntity blogEntity);

	BlogEntity findByBlogTitle(String blogTitle);
	// BlogEntity findByBlogTitleAndDate(String blogTitle,LocalDate registerDate);
	BlogEntity findByBlogContents(String blogContents);
	BlogEntity findByBlogId(Long blogId);
	List<BlogEntity> findByBlogAttribute(String blogAttribute);
	List<BlogEntity>findByUserIdAndBlogAttribute(Long userId, String attribute);
	
	@Transactional
	List<BlogEntity>deleteByBlogId(Long blogId);

}
