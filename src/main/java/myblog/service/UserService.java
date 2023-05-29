package myblog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myblog.model.dao.UserDao;
import myblog.model.entity.UserEntity;



@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
			
	public boolean createAccount(String userLoginName,String userDisplayName,String email,String password) {
				
		UserEntity userEntity = userDao.findByEmail(email);
		
		if(userEntity == null) {
			userDao.save(new UserEntity(userLoginName,userDisplayName,email,password));
			return true;
		}else {
			return false;
		}
	}
	
	
	public UserEntity loginAccount(String userLoginName, String password) {
		
		UserEntity userEntity = userDao.findByLoginNameAndPassword(userLoginName, password);
		
		if(userEntity == null) {
			return null;
		}else {
			return userEntity;
		}
		
	}
	
}
