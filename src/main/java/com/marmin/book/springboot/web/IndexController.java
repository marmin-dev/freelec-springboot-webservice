package com.marmin.book.springboot.web;

import com.marmin.book.springboot.config.auth.LoginUser;
import com.marmin.book.springboot.config.auth.dto.SessionUser;
import com.marmin.book.springboot.service.posts.PostsService;
import com.marmin.book.springboot.web.Dto.PostsResponseDto;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.geometry.Pos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;
//    @GetMapping("/")
//    public String index(Model model){
//        model.addAttribute("posts",postsService.findAllDesc());
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        if(user != null){
//            model.addAttribute("userName",user.getName());
//        }
//        return "index";
//    }
@GetMapping("/")
public String index(Model model, @LoginUser SessionUser user) {
    //@LoginUser SessionUser user
    //기존에 (User) httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선 되었습니다
    //이제는 어느 컨트롤러든지 @LoginUser 만 사용하면 세션 정보를 가져올 수 있게 되었습니다.
    model.addAttribute("posts", postsService.findAllDesc());
    if (user != null) {
        model.addAttribute("userName", user.getName());
    }
    return "index";
}

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id,Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        return "posts-update";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    }


