package myblog.model.entity;

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

@Table(name = "account")
public class UserEntity {
	
	
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId; 
	
		@NonNull
		@Column(name = "user_login_name")
	private String userLoginName;

	
		@NonNull
		@Column(name = "user_display_name")
	private String userDisplayName;

		
		@NonNull
		@Column(name = "email")
	private String email;

		
		@NonNull
		@Column(name = "password")
	private String password;

	
}
