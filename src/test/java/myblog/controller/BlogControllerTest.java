package myblog.controller;  //声明了代码所在的包名为myblog.controller

import myblog.model.entity.BlogEntity;
import myblog.model.entity.UserEntity;
import myblog.service.BlogService;  //导入了myblog.service包中的BlogService类
import org.junit.jupiter.api.BeforeEach;  //导入了JUnit5中的BeforeEach注解，该注解表示在每个测试方法运行前都要执行的方法
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;  //导入了Spring的Autowired注解，用于自动装配bean
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;  //导入了Spring Boot Test中的AutoConfigureMockMvc注解，用于自动配置MockMvc
import org.springframework.boot.test.context.SpringBootTest;  //导入了Spring Boot Test中的SpringBootTest注解，表示这是一个集成测试，需要完全启动Spring应用上下文
import org.springframework.boot.test.mock.mockito.MockBean;  //导入了Spring Boot Test中的MockBean注解，用于创建一个mock实例并将其添加到Spring的应用上下文中
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;  //导入了Spring Test中的MockHttpSession类，用于模拟HTTP Session
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;  //导入了Spring Test中的MockMvc类，用于模拟发送HTTP请求
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest  //表示这是一个集成测试，需要完全启动Spring应用上下文
@AutoConfigureMockMvc  //自动配置MockMvc
public class BlogControllerTest {  //定义了一个名为BlogControllerTest的公共类
    @Autowired  //表示Spring会自动装配以下的bean
    private MockMvc mockMvc;  //定义了一个MockMvc类型的私有字段

    @MockBean  //创建一个mock实例并将其添加到Spring的应用上下文中
    private BlogService blogService;  //定义了一个BlogService类型的私有字段

    private MockHttpSession session;  //定义了一个MockHttpSession类型的私有字段

    @BeforeEach  //表示在每个测试方法运行前都要执行的方法
    public void prepareData() {
        UserEntity user = new UserEntity(); // 创建一个UserEntity对象
        user.setUserId(1L); // 设置用户ID为1
        user.setUserLoginName("0001a"); // 设置用户登录名为"0001"

        List<BlogEntity> blogEntityList = new ArrayList<>(); // 创建一个BlogEntity对象列表
        blogEntityList.add(new BlogEntity()); // 向列表中添加一个BlogEntity对象
        blogEntityList.add(new BlogEntity()); // 向列表中添加另一个BlogEntity对象

        session = new MockHttpSession(); // 创建一个MockHttpSession对象
        session.setAttribute("user", user); // 将"user"属性设置为上面创建的UserEntity对象

        when(blogService.findAllBlogPost(1L)).thenReturn(blogEntityList); // 当调用blogService的findAllBlogPost方法并传入参数1L时，返回上面创建的BlogEntity对象列表

    }

    //TO GET BLOG LIST PAGE
    @Test
    public void testGetBlogListPage() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.get("/user/blog/list") // 发起一个GET请求，路径为"/user/blog/list"
                .session(session); // 设置会话(session)

        mockMvc.perform(request) // 执行请求
                .andExpect(status().isOk()) // 期望返回状态码为200（表示成功）
                .andExpect(view().name("blog-list.html")) // 期望返回的视图名称为"blog-list.html"
                .andExpect(model().attributeExists("userLoginName","essayList","noteList")); // 期望模型中存在"userLoginName"和"blogList"属性
    }
    //↑在这里，isOk()是一个MockMvcResultMatchers类中的静态方法，用于断言HTTP响应的状态码是否为200（OK）。如果响应的状态码为200，则断言成功；否则，断言失败。它是Spring MVC Test框架提供的一种断言方法，用于验证控制器的行为和返回结果。
    //这里的attributeExists调用了BlogController里面的GetBlogListPage里面设置好的model


    //TO GET BLOG REGISTER PAGE
    @Test
    public void testGetBlogRegisterPage() throws Exception{
        UserEntity user = new UserEntity();
        user.setUserDisplayName("0001");
        session.setAttribute("user",user);

        mockMvc.perform(get("/user/blog/edit").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-editor.html"))
                .andExpect(model().attributeExists("userDisplayName"));
    }

    //ブログ登録成功検証
    @Test
    public void testBlogRegister_Succeed() throws Exception {
        // 模拟session中的用户
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);
        session.setAttribute("user", mockUser);

        // 模拟service层的方法返回值
        when(blogService.createBlogPost(anyString(), anyString(), anyString(), anyString(), anyLong())).thenReturn(true);

        // 模拟上传的文件
        MockMultipartFile mockFile = new MockMultipartFile("blogImage", "test-image.jpg", "image/jpeg", "test data".getBytes());

        // 执行请求并验证结果
        mockMvc.perform(multipart("/user/blog/edit/process")
                        .file(mockFile)
                        .param("blogTitle", "Test Blog")
                        .param("articleType", "essay")
                        .param("blogContents", "Test Contents")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/blog/list"));

        // 验证service层的方法被正确调用
        verify(blogService, times(1)).createBlogPost(eq("Test Blog"), eq("Test Contents"), eq("essay"), eq("test-image.jpg"), eq(1L));
    }

    //ブログ登録失敗の検証
    @Test
    public void testBlogRegister_Failed() throws Exception {
        // 模拟session中的用户
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);
        session.setAttribute("user", mockUser);

        // 模拟service层的方法返回值
        when(blogService.createBlogPost(anyString(), anyString(), anyString(), anyString(), anyLong())).thenReturn(false);

        // 模拟上传的文件
        MockMultipartFile mockFile = new MockMultipartFile("blogImage", "test-image.jpg", "image/jpeg", "test data".getBytes());

        // 执行请求并验证结果
        mockMvc.perform(multipart("/user/blog/edit/process")
                        .file(mockFile)
                        .param("blogTitle", "Test Blog")
                        .param("articleType", "essay")
                        .param("blogContents", "Test Contents")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-list.html"));  // 这里应为blog-editor.html，表示发生错误，留在编辑页

        // 验证service层的方法被正确调用
        verify(blogService, times(1)).createBlogPost(eq("Test Blog"), eq("Test Contents"), eq("essay"), eq("test-image.jpg"), eq(1L));
    }

    //ブログ記事読むページに着くの検証
    //記事のタイトル、写真、本文を押下場合
    @Test
    public void testViewBlog() throws Exception {
        // 创建一个模拟的 BlogEntity 对象
        BlogEntity mockBlog = new BlogEntity();
        mockBlog.setBlogTitle("Test Blog");
        mockBlog.setBlogContents("Test Contents");
        mockBlog.setBlogAttribute("essay");

        // 模拟service层的方法返回值
        when(blogService.getBlogPost(anyLong())).thenReturn(mockBlog);

        // 执行请求并验证结果
        mockMvc.perform(get("/user/blog/view/1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-main.html"))
                .andExpect(model().attribute("blog", mockBlog));

        // 验证service层的方法被正确调用
        verify(blogService, times(1)).getBlogPost(eq(1L));
    }

    //ブログ編集画面に着く
    @Test
    public void testGetBlogEditPage() throws Exception{
        //仮想のBlogEntityを作る
        BlogEntity mockBlog = new BlogEntity();
        mockBlog.setBlogTitle("Test Blog");
        mockBlog.setBlogContents("Test Contents");
        mockBlog.setBlogAttribute("essay");

        //仮想のUserEntityを作る
        UserEntity mockUser = new UserEntity();
        mockUser.setUserDisplayName("TestDisplayName");
        mockUser.setUserId(1L);
        session.setAttribute("user",mockUser);

        when(blogService.getBlogPost(1L)).thenReturn(mockBlog);

        mockMvc.perform(get("/user/blog/edit/{blogId}",1L).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-editorN.html"))
                .andExpect(model().attributeExists("userDisplayName"))
                .andExpect(model().attribute("userDisplayName","TestDisplayName"))
                .andExpect(model().attribute("blog",mockBlog));

        verify(blogService,times(1)).getBlogPost(1L);
    }

    //blogUpdateEditのテスト
    //成功
    //失敗
    @Test
    public void testBlogUpdateEdit_Success() throws Exception {
        //mockを作る
        when(blogService.editBlogPost(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong()))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/blog/update")
                .param("blogTitle","test updated blog")
                .param("blogContents","test update edit contents")
                .param("blogAttribute","essay")
                .param("blogImgUrl","1234.jpg")
                .param("userId","1L")
                .param("blogId","505L")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-list.html"));

        verify(blogService,times(1)).editBlogPost(eq("test updated blog"),eq("test update edit contents")
                ,eq("essay"),eq("1234.jpg"),eq(1L),eq(505L));



    }



}
