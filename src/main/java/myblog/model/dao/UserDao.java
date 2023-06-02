package myblog.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import myblog.model.entity.UserEntity;



public interface UserDao extends JpaRepository<UserEntity, Long> {
	
	UserEntity save(UserEntity userEntity);
	
	UserEntity findByEmail (String email);

	UserEntity findByUserLoginNameAndEmail(String userLoginName,String email);

	UserEntity findByEmailAndPassword(String email,String password);
	
	UserEntity findByUserLoginNameAndPassword(String userLoginName,String password);
}
