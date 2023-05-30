package myblog.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import myblog.model.entity.BlogEntity;
import myblog.model.entity.UserEntity;
import myblog.service.BlogService;

@RequestMapping("/user/blog")
@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private HttpSession session;

    // TO return blog-list.html , based on user's id(link to LoginName)
    @GetMapping("/list")
    public String getBlogListPage(Model model) {
        UserEntity userList = (UserEntity) session.getAttribute("user");
        Long userId = userList.getUserId();

        String userDisplayName = userList.getUserDisplayName();

//        List<BlogEntity> blogList = blogService.findAllBlogPost(userId);
        List<BlogEntity> essayList = blogService.findByUserIdAndBlogAttribute(userId,"essay");
//        List<BlogEntity> essayList = blogService.findBlogByAttribute( "essay");
//        List<BlogEntity> noteList = blogService.findBlogByAttribute( "note");
        List<BlogEntity> noteList = blogService.findByUserIdAndBlogAttribute(userId,"note");


        model.addAttribute("userDisplayName", userDisplayName);
        model.addAttribute("essayList", essayList);
        model.addAttribute("noteList", noteList);
//        model.addAttribute("blogList", blogList);
        return "blog-list.html";
    }

    // TO create a new blog post,step1,to edit page.
    @GetMapping("/edit")
    public String getBlogRegisterPage(Model model) {

        UserEntity userList = (UserEntity) session.getAttribute("user");
        String userDisplayName = userList.getUserDisplayName();
        model.addAttribute("userDisplayName", userDisplayName);
        return "blog-editor.html";
    }

    // To create a new blog post method,step2,write in edit page and save them.
    @PostMapping("/edit/process")
    public String blogRegister(
            @RequestParam String blogTitle,
            // @RequestParam String currentDate, //use local time and cannot change it
            @RequestParam String articleType,
            @RequestParam("blogImage") MultipartFile blogImage,
            @RequestParam String blogContents, Model model) {

        UserEntity userList = (UserEntity) session.getAttribute("user");
        Long userId = userList.getUserId();

        String imgFileName = blogImage.getOriginalFilename();

        try {
            // 画像ファイルの保存先を指定する。
            File blogFile = new File("./src/main/resources/static/img/" + imgFileName);
            // 画像ファイルからバイナリデータを取得する
            byte[] bytes = blogImage.getBytes();
            // 画像を保存（書き出し）するためのバッファを用意する
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(blogFile));
            // 画像ファイルの書き出しする。
            out.write(bytes);
            // バッファを閉じることにより、書き出しを正常終了させる。
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(blogService.createBlogPost(blogTitle, blogContents, 
        articleType, imgFileName, userId)){
           return "redirect:/user/blog/list";
        }else{
            return "blog-list.html";
        }
    }

    //To　view blog
    @GetMapping("/user/blog/view/{blogId}")
    public String viewBlog(
            @PathVariable Long blogId,Model model){
            BlogEntity blog = blogService.getBlogPost(blogId);
            model.addAttribute("blog",blog);
            return "blog-main.html";
    }


    // What if user wants to edit the blog
    // Step 1: get mapping
    @GetMapping("/edit/{blogId}")
    public String getBlogEditPage(@PathVariable Long blogId, Model model) {

        UserEntity userList = (UserEntity) session.getAttribute("user");
        Long userId = userList.getUserId();

        String userName = userList.getUserDisplayName();
        model.addAttribute("userDisplayName", userName);

        BlogEntity blogList = blogService.getBlogPost(blogId);

        return "blog-edit.html";
    }

    // Step 2: Post mapping
    @PostMapping("/post/{blogId}")
    public String blogUpdateEdit(
            @RequestParam String blogTitle,
            @RequestParam String currentDate, // use local time and cannot change it
            @RequestParam String articleType,
            @RequestParam("blogImage") MultipartFile blogImage,
            @RequestParam String blogContents, Model model) {

        UserEntity userList = (UserEntity) session.getAttribute("user");
        Long userId = userList.getUserId();

        return "blog-edit.html";
    }

    // DELETE BLOG
    @GetMapping("/delete/{blogId}")
    public String getBlogDelete(@PathVariable Long blogId, Model model) {

        UserEntity userList = (UserEntity) session.getAttribute("user");
        Long userId = userList.getUserId();

        BlogEntity blogList = blogService.getBlogPost(blogId);
        if (blogList == null) {
            return "redirect:/user/blog/main";
        } else {
            model.addAttribute("blogList", blogList);
            return "blog-main.html";
        }
    }

    // @PostMapping ("/delete")
    // Public String blogDelete(@RequestParam Long blogId,Model model) {

    // if(blogService.deleteBlog(blogId)) {
    // return "blog-main.html";
    // }else {
    // model.addAttribute("DeleteDetailMessage", "記事削除に失敗しました");
    // return "blog-delete.html";
    // }

    // }

}
