package myblog.controller;


import myblog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void prepareData() {
        //eq : LoginName DisplayName mail password
        when(userService.createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq("123456"))).thenReturn(true);
        //項目が入力しません
        //一つの項目が入力してない
        when(userService.createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq(""))).thenReturn(false);
        when(userService.createAccount(eq("0004a"), eq("0004"), eq(""), eq("123456"))).thenReturn(false);
        when(userService.createAccount(eq("0004a"), eq(""), eq("0004a@gmail.com"), eq("123456"))).thenReturn(false);
        when(userService.createAccount(eq(""), eq("0004"), eq("0004a@gmail.com"), eq("123456"))).thenReturn(false);
        //二つの項目が入力してない
        when(userService.createAccount(eq(""), eq("0004"), eq("0004a@gmail.com"), eq(""))).thenReturn(false);
        when(userService.createAccount(eq(""), eq(""), eq("0004a@gmail.com"), eq("123456"))).thenReturn(false);
        when(userService.createAccount(eq(""), eq("0004"), eq(""), eq("123456"))).thenReturn(false);
        when(userService.createAccount(eq("0004a"), eq(""), eq("0004a@gmail.com"), eq(""))).thenReturn(false);
        when(userService.createAccount(eq("0004a"), eq(""), eq(""), eq("123456"))).thenReturn(false);
        when(userService.createAccount(eq("0004a"), eq("0004"), eq(""), eq(""))).thenReturn(false);
        //三つの項目が入力してない
        when(userService.createAccount(eq(""), eq(""), eq(""), eq("123456"))).thenReturn(false);
        when(userService.createAccount(eq("0004a"), eq(""), eq(""), eq(""))).thenReturn(false);

        //既に登録済項目
        //databaseのなかで既に登録すいみのデータ：
        //LoginName:0003a
        //DisplayName:0003
        //email:0003a@mail.com
        //password:existedpsw

        //ログイン名被ってる
        when(userService.createAccount(eq("0003a"), eq("0004"), eq("0004a@gmail.com"), eq("123456"))).thenReturn(false);
        //DisplayName被ってる--true
        when(userService.createAccount(eq("0004a"), eq("0003"), eq("0004a@gmail.com"), eq("123456"))).thenReturn(true);
        //email被ってる
        when(userService.createAccount(eq("0004a"), eq("0004"), eq("0003a@mail.com"), eq("123456"))).thenReturn(false);
        //password被ってる　--true
        when(userService.createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq("existedpsw"))).thenReturn(true);
    }

        //user レジスターページに辿り着く
        @Test
        public void testGetUserRegisterPage() throws Exception{
            RequestBuilder request = MockMvcRequestBuilders.get("/user/register");
            mockMvc.perform(request).andExpect(view().name("register.html"));
        }

        //この項目を検証
        // when(userService.createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq("123456"))).thenReturn(true);
        @Test
        public void testRegister_Successful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","0004")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(redirectedUrl("/user/login"));

            // 验证 userService.createAccount("0004a", "0004", "0004a@gmail.com","123456") 方法被调用了一次
            //times(1) :表示验证方法被调用的次数为一次。
            //times(1) 是默认的验证次数，如果不显式地指定验证次数，verify() 方法会默认使用 times(1)。
            //可以使用其他验证次数，例如 times(2) 表示验证方法被调用的次数为两次，atLeast(3) 表示至少调用三次等等。这样可以确保方法在测试中被调用了预期的次数。
            verify(userService,times(1)).createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq("123456"));
        }

        //入力してない項目
        //passwordだけ入力してない
        @Test
        public void testRegisterNoPsw_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","0004")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq(""));
        }

        //userDisplayNameだけ入力してない
        @Test
        public void testRegisterNoDisplayName_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq(""), eq("0004a@gmail.com"), eq("123456"));
        }
        //userLoginNameだけ入力してない
        @Test
        public void testRegisterNoLoginName_UnSuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                .param("userLoginName","")
                .param("userDisplayName","0004")
                .param("userEmail","0004a@gmail.com")
                .param("password","123456");
        mockMvc.perform(request).andExpect(view().name("register.html"));

        verify(userService,times(1)).createAccount(eq(""), eq("0004"), eq("0004a@gmail.com"), eq("123456"));
    }
        //emailだけ入力してない
        @Test
        public void testRegisterNoEmail_UnSuccessful() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                .param("userLoginName","0004a")
                .param("userDisplayName","0004")
                .param("userEmail","")
                .param("password","123456");
        mockMvc.perform(request).andExpect(view().name("register.html"));

        verify(userService,times(1)).createAccount(eq("0004a"), eq("0004"), eq(""), eq("123456"));
    }
        //LoginNameとDisplayName入力してない
        @Test
        public void testRegisterNoLoginNameNoDisplayName_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","")
                    .param("userDisplayName","")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq(""), eq(""), eq("0004a@gmail.com"), eq("123456"));
        }
        //LoginNameとEmail入力してない
        @Test
        public void testRegisterNoLoginNameNoEmail_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","")
                    .param("userDisplayName","0004")
                    .param("userEmail","")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq(""), eq("0004"), eq(""), eq("123456"));
        }
        //LoginNameとPassword入力してない
        @Test
        public void testRegisterNoLoginNameNoPassword_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","")
                    .param("userDisplayName","0004")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq(""), eq("0004"), eq("0004a@gmail.com"), eq(""));
        }
        //DisplayNameとEmail入力してない
        @Test
        public void testRegisterNoDisplayNameNoEmail_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","")
                    .param("userEmail","")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq(""), eq(""), eq("123456"));
        }
        //DisplayNameとPassword入力してない
        @Test
        public void testRegisterNoDisplayNameNoPassword_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq(""), eq("0004a@gmail.com"), eq(""));
        }
        //EmailとPassword入力してない
        @Test
        public void testRegisterNoEmailNoPassword_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","0004")
                    .param("userEmail","")
                    .param("password","");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq("0004"), eq(""), eq(""));
        }
        //LoginName,DisplayName,Email入力してない
        @Test
        public void testRegisterNoLoginNameNoDisplayNameNoEmail_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","")
                    .param("userDisplayName","")
                    .param("userEmail","")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq(""), eq(""), eq(""), eq("123456"));
        }
        //DisplayName,Email,Password入力してない
        @Test
        public void testRegisterNoDisplayNameNoEmailNoPassword_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","")
                    .param("userEmail","")
                    .param("password","");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq(""), eq(""), eq(""));
        }
        //LoginName,DisplayName,Email,Password何かも入力してない
        @Test
        public void testRegisterNoAnyInsert_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","")
                    .param("userDisplayName","")
                    .param("userEmail","")
                    .param("password","");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq(""), eq(""), eq(""), eq(""));
        }

        //登録すみ内容の検証
        //LoginNameが既に登録すみ
        //LoginName:0003a
        //DisplayName:0003
        //email:0003a@mail.com
        //password:existedpsw
        @Test
        public void testRegister_ExistedLoginName_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0003a")
                    .param("userDisplayName","0004")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0003a"), eq("0004"), eq("0004a@gmail.com"), eq("123456"));
        }
        //DisplayNameが既に登録すみ
        @Test
        public void testRegister_ExistedDisplayName_Successful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","0003")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("redirect:/user/login"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq("0003"), eq("0004a@gmail.com"), eq("123456"));
        }
        //Emailが既に登録すみ
        @Test
        public void testRegister_ExistedEmail_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","0004")
                    .param("userEmail","0003a@mail.com")
                    .param("password","123456");
            mockMvc.perform(request).andExpect(view().name("register.html"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq("0004"), eq("0003a@mail.com"), eq("123456"));
        }
        //passwordが既に登録すみ
        @Test
        public void testRegister_ExistedPassword_UnSuccessful() throws Exception {
            RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process")
                    .param("userLoginName","0004a")
                    .param("userDisplayName","0004")
                    .param("userEmail","0004a@gmail.com")
                    .param("password","existedpsw");
            mockMvc.perform(request).andExpect(view().name("redirect:/user/login"));

            verify(userService,times(1)).createAccount(eq("0004a"), eq("0004"), eq("0004a@gmail.com"), eq("existedpsw"));
        }


    }

