package myblog.controller;


import jakarta.servlet.http.HttpSession;
import myblog.model.entity.UserEntity;
import myblog.service.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultHandlersDsl;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    //毎回ここに書いたコードを初期化の設定にする
    public void prepareData(){
        UserEntity userEntity = new UserEntity("Jake","SimJaeYun","jake@enhypen.com","123456");
        when(userService.loginAccount(eq("Jake"),eq("123456"))).thenReturn(userEntity);
        when(userService.loginAccount(eq("Jake"),eq("qwerty"))).thenReturn(null);
        when(userService.loginAccount(eq("Tiffany"),eq("123456"))).thenReturn(null);
        when(userService.loginAccount(eq("Tiffany"),eq("qwerty"))).thenReturn(null);
        when(userService.loginAccount(eq(""),eq("qwerty"))).thenReturn(null);
        when(userService.loginAccount(eq(""),eq("123456"))).thenReturn(null);
        when(userService.loginAccount(eq("Jake"),eq(""))).thenReturn(null);
        when(userService.loginAccount(eq(""),eq(""))).thenReturn(null);
    }

    //ログインページに正しく取得できたかどうかを検証：
    @Test
    public void testGetLoginPage_Succeed() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.get("/user/login");
        mockMvc.perform(request).andExpect(view().name("login.html"));
    }

    //正しくログインできるかどうかを検証：
    @Test
    public void testLogin_Successful()throws Exception{
        // 创建一个 POST 请求，路径为 "/user/login/process"，并设置请求参数
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName","Jake")
                .param("password","123456");
        // 执行请求，并期望重定向到路径 "/user/blog/list"，并返回 MvcResult 对象
        MvcResult result = mockMvc.perform(request).andExpect(redirectedUrl("/user/blog/list")).andReturn();

        // 从返回的 MvcResult 中获取请求的 HttpSession 对象
        HttpSession session = result.getRequest().getSession();

        // 从 HttpSession 中获取名为 "user" 的属性，并将其转换为 UserEntity 对象
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNotNull(loggedInUser);     // 断言 loggedInUser 不为空
        assertEquals("Jake",loggedInUser.getUserLoginName());     // 断言 loggedInUser 的 userLoginName 属性为 "Jake"
        assertEquals("jake@enhypen.com",loggedInUser.getEmail());     // 断言 loggedInUser 的 email 属性为 "jake@enhypen.com"
    }


    //テスト目的：パスワードが間違っている場合のログインが失敗することを検証します。
    @Test
    public void testLogin_WrongPassword_Unsuccessful() throws Exception{
        // 创建一个 POST 请求，路径为 "/user/login/process"，并设置请求参数
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName","Jake")
                .param("password","qwerty");

        // 执行请求，并期望重定向到路径 "/user/login"，并返回 MvcResult 对象
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())  // 验证响应的状态码是否为 3xx 重定向
                .andExpect(redirectedUrl("/user/login"));  // 验证重定向的路径是否为 "/user/login"

        // 从 "/user/login" 路径发送 GET 请求，获取 HttpSession 对象
        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login"))
                .andReturn().getRequest().getSession();

        // 从 HttpSession 中获取名为 "user" 的属性，并将其转换为 UserEntity 对象
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        // 断言 loggedInUser 为空
        assertNull(loggedInUser);
    }

    //テスト目的：LoginNameが間違っている場合のログインが失敗することを検証します。
    @Test
    public void testLogin_WrongLoginName_Unsuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName", "Tiffany")
                .param("password", "123456");

        mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest()
                .getSession();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNull(loggedInUser);
    }

    //間違ったログイン名(Tiffany)と間違ったパスワード(qwerty)を入力してる場合のログインが失敗するかを検証
    @Test
    public void testLogin_WrongLoginNameAndWrongPsw_Unsuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName", "Tiffany")
                .param("password", "qwerty");

        mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest()
                .getSession();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNull(loggedInUser);
    }

    //ログイン名空白、間違ったパスワード(qwerty)を入力してる場合のログインが失敗するかを検証
    @Test
    public void testLogin_blankLoginNameAndWrongPsw_Unsuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName", "")
                .param("password", "qwerty");

        mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest()
                .getSession();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNull(loggedInUser);
    }
    //ログイン名空白、正しいパスワード(123456)を入力してる場合のログインが失敗するかを検証
    @Test
    public void testLogin_blankLoginNameAndRightPsw_Unsuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName", "")
                .param("password", "123456");

        mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest()
                .getSession();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNull(loggedInUser);
    }

    //正しい名、パスワード空白を入力してる場合のログインが失敗するかを検証
    @Test
    public void testLogin_LoginNameAnWrongPsw_Unsuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName", "Jake")
                .param("password", "");

        mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest()
                .getSession();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNull(loggedInUser);
    }

    //ログイン名空白、パスワード空白を入力してる場合のログインが失敗するかを検証
    @Test
    public void blankNameAndBlandPsw_Unsuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
                .param("userLoginName", "")
                .param("password", "");

        mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest()
                .getSession();

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        assertNull(loggedInUser);
    }


}


