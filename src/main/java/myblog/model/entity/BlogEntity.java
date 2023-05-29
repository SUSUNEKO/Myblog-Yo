package myblog.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "blog")
public class BlogEntity {
	@Id
	@Column(name = "blog_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long blogId;
	
	@NonNull
	@Column(name = "blog_title")
	private String blogTitle;
	
	// @NonNull
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	// @Column(name = "register_date")
	// private LocalDate registerDate;
	
	@NonNull
	@Column(name = "blog_contents")
	private String blogContents;
	
	@NonNull
	@Column(name = "blog_attribute")
	private String blogAttribute;
	
	@NonNull
	@Column(name = "blog_img_url")
	private String blogImgUrl;
	
	@Column(name = "user_id")
	private Long userId;
	

	// public BlogEntity( @NonNull String blogTitle, @NonNull LocalDate registerDate,
	// 		@NonNull String blogContents, @NonNull String blogAttribute, @NonNull String blogImgUrl, Long userId) {
	// 	this.blogTitle = blogTitle;
	// 	this.registerDate = registerDate;
	// 	this.blogContents = blogContents;
	// 	this.blogAttribute = blogAttribute;
	// 	this.blogImgUrl = blogImgUrl;
	// 	this.userId = userId;
	// }
	
	
	 public BlogEntity( 
		@NonNull String blogTitle,
	 	@NonNull String blogContents,
		@NonNull String blogAttribute, 
		@NonNull String blogImgUrl,
		Long userId) {
			this.blogTitle = blogTitle;
			// this.registerDate = LocalDateTime.now(); 
			this.blogContents = blogContents; 
			this.blogAttribute = blogAttribute; 
	  		this.blogImgUrl = blogImgUrl; 
	 		this.userId = userId; }
	 
	
	
	
}
