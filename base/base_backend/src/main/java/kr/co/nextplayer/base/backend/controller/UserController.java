package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.UserDto;
import kr.co.nextplayer.base.backend.model.User;
import kr.co.nextplayer.base.backend.service.ConfigService;
import kr.co.nextplayer.base.backend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class UserController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource // byType으로 자동 주입
    private UserService service;

    @Resource
    private ConfigService configService;

    @RequestMapping(value = "/login", method= RequestMethod.GET)
    public ModelAndView loginPage () {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @RequestMapping(value="/loginProcess",method=RequestMethod.POST)
    public String loginProcess(@RequestParam Map<String, String> params, HttpSession session, UserDto dto, HttpServletRequest request, HttpServletResponse response){
        logger.info("loginProcess was called. params:"+params);
        String returnURL ="";
        if ( session.getAttribute("login") !=null ){
            // 기존에 login이란 세션 값이 존재한다면
            session.removeAttribute("login");// 기존값을 제거해 준다.
        }

        // 로그인이 성공하면 UserVO 객체를 반환함.
        User vo = service.login(dto);

        List<HashMap<String, Object>> menuList = (List<HashMap<String, Object>>) session.getAttribute("adminMenuList");
        List<HashMap<String, Object>> upperMenuList = (List<HashMap<String, Object>>) session.getAttribute("adminUpperMenuList");

        if (menuList == null || menuList.size() == 0) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("id", vo.getId());

            menuList = configService.selectUserMenuList(param);
            session.setAttribute("adminMenuList", menuList);

            upperMenuList = configService.selectUserUpperMenuList(params);
            session.setAttribute("adminUpperMenuList", upperMenuList);
        }

        if ( vo !=null ){// 로그인 성공
            System.out.println("--- login success ~~~");
            System.out.println("---- toString : " + vo.toString());
            session.setAttribute("login", vo);// 세션에 login인이란 이름으로 UserVO 객체를 저장해 놈.
            session.setMaxInactiveInterval(60 * 60 * 24);
            //returnURL ="redirect:/main";// 로그인 성공시 메인으로 이동

            if (menuList.get(0).get("url").equals("/team")) {
                returnURL ="redirect:/team";
            } else {
                returnURL = "redirect:" + menuList.get(0).get("url").toString();
            }


            /*
             *  [   세션 추가되는 부분      ]
             */
            // 1. 로그인이 성공하면, 그 다음으로 로그인 폼에서 쿠키가 체크된 상태로 로그인 요청이 왔는지를 확인한다.
            System.out.println("getUseCookieId : "+ dto.getUseCookieId() + ", getUseCookiePw :"+ dto.getUseCookiePw());
            if ( dto.getUseCookieId().equals("true") ){// dto 클래스 안에 useCookie 항목에 폼에서 넘어온 쿠키사용 여부(true/false)가 들어있을 것임
                System.out.println("--- set UseCookieId ~");
                // 쿠키를 생성하고 현재 로그인되어 있을 때 생성되었던 세션의 id를 쿠키에 저장한다.
                Cookie cookieUserId =new Cookie("cookieUserId", vo.getId());
                cookieUserId.setPath("/");
                int amount =60 *60 *24 *7;
                cookieUserId.setMaxAge(amount);// 단위는 (초)임으로 7일정도로 유효시간을 설정해 준다.

                response.addCookie(cookieUserId);

            }else if ( dto.getUseCookieId().equals("false") ){
                //쿠키를 가져와보고
                Cookie cookieUserId = WebUtils.getCookie(request,"cookieUserId");
                if ( cookieUserId !=null ){
                    // null이 아니면 존재하면!
                    cookieUserId.setPath("/");
                    // 쿠키는 없앨 때 유효시간을 0으로 설정하는 것 !!! invalidate같은거 없음.
                    cookieUserId.setMaxAge(0);
                    // 쿠키 설정을 적용한다.
                    response.addCookie(cookieUserId);
                }
            }

            if ( dto.getUseCookiePw().equals("true") ){// dto 클래스 안에 useCookie 항목에 폼에서 넘어온 쿠키사용 여부(true/false)가 들어있을 것임
                System.out.println("--- set UseCookiePw ~");
                // 쿠키를 생성하고 현재 로그인되어 있을 때 생성되었던 세션의 id를 쿠키에 저장한다.
                Cookie cookieUserPw =new Cookie("cookieUserPw", vo.getPw());
                // 쿠키를 찾을 경로를 컨텍스트 경로로 변경해 주고...
                cookieUserPw.setPath("/");
                int amount =60 *60 *24 *7;
                cookieUserPw.setMaxAge(amount);// 단위는 (초)임으로 7일정도로 유효시간을 설정해 준다.
                response.addCookie(cookieUserPw);

            }else if ( dto.getUseCookiePw().equals("false") ){
                //쿠키를 가져와보고
                Cookie cookieUserPw = WebUtils.getCookie(request,"cookieUserPw");
                if ( cookieUserPw !=null ){
                    // null이 아니면 존재하면!
                    cookieUserPw.setPath("/");
                    // 쿠키는 없앨 때 유효시간을 0으로 설정하는 것 !!! invalidate같은거 없음.
                    cookieUserPw.setMaxAge(0);
                    // 쿠키 설정을 적용한다.
                    response.addCookie(cookieUserPw);
                }
            }

            if (dto.getUseCookieId().equals("true")  || dto.getUseCookiePw().equals("true")) {
                // 쿠키 사용한다는게 체크되어 있으면...
                // 쿠키를 생성하고 현재 로그인되어 있을 때 생성되었던 세션의 id를 쿠키에 저장한다.
                Cookie cookie =new Cookie("loginCookie", session.getId());
                // 쿠키를 찾을 경로를 컨텍스트 경로로 변경해 주고...
                cookie.setPath("/");
                int amount =60 *60 *24 *7;
                cookie.setMaxAge(amount);// 단위는 (초)임으로 7일정도로 유효시간을 설정해 준다.
                // 쿠키를 적용해 준다.
                response.addCookie(cookie);

                // currentTimeMills()가 1/1000초 단위임으로 1000곱해서 더해야함
                Date sessionLimit =new Date(System.currentTimeMillis() + (1000*amount));
                // 현재 세션 id와 유효시간을 사용자 테이블에 저장한다.
                System.out.println("userId : "+ vo.getId()+", session id : "+ session.getId());
                service.keepLogin(vo.getId(), session.getId(), sessionLimit);

            }else  if (dto.getUseCookieId().equals("false") && dto.getUseCookiePw().equals("false")) {
                //쿠키를 가져와보고
                Cookie cookieUserId = WebUtils.getCookie(request,"cookieUserId");
                Cookie cookieUserPw = WebUtils.getCookie(request,"cookieUserPw");
                Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");
                if ( cookieUserId !=null ){
                    // null이 아니면 존재하면!
                    cookieUserId.setPath("/");
                    // 쿠키는 없앨 때 유효시간을 0으로 설정하는 것 !!! invalidate같은거 없음.
                    cookieUserId.setMaxAge(0);
                    // 쿠키 설정을 적용한다.
                    response.addCookie(cookieUserId);
                }

                if ( cookieUserPw !=null ){
                    // null이 아니면 존재하면!
                    cookieUserPw.setPath("/");
                    // 쿠키는 없앨 때 유효시간을 0으로 설정하는 것 !!! invalidate같은거 없음.
                    cookieUserPw.setMaxAge(0);
                    // 쿠키 설정을 적용한다.
                    response.addCookie(cookieUserPw);
                }

                if ( loginCookie !=null ){
                    // null이 아니면 존재하면!
                    loginCookie.setPath("/");
                    // 쿠키는 없앨 때 유효시간을 0으로 설정하는 것 !!! invalidate같은거 없음.
                    loginCookie.setMaxAge(0);
                    // 쿠키 설정을 적용한다.
                    response.addCookie(loginCookie);

                }
                // 사용자 테이블에서도 유효기간을 현재시간으로 다시 세팅해줘야함.
                Date date =new Date(System.currentTimeMillis());
                service.keepLogin(vo.getId(), session.getId(), date);

            }
        }else {// 로그인에 실패한 경우
            System.out.println("--- login fail ~~~");
            returnURL ="redirect:/login";// 로그인 폼으로 다시 가도록 함
        }


        //ModelAndView modelAndView = new ModelAndView("/team/team");
        //return modelAndView;// 위에서 설정한 returnURL 을 반환해서 이동시킴
        return returnURL;
    }

    // 로그아웃 하는 부분
    @RequestMapping(value="/logout")
    public String logout(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
        logger.info("logout was called. ");

        Object obj = session.getAttribute("login");

        if ( obj !=null ){
            User vo = (User) obj;
            // null이 아닐 경우 제거
            session.removeAttribute("login");
            session.invalidate();// 세션 전체를 날려버림
            //쿠키를 가져와보고
            Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");
            if ( loginCookie !=null ){
                // null이 아니면 존재하면!
                loginCookie.setPath("/");
                // 쿠키는 없앨 때 유효시간을 0으로 설정하는 것 !!! invalidate같은거 없음.
                //loginCookie.setMaxAge(0);
                // 쿠키 설정을 적용한다.
                response.addCookie(loginCookie);

                // 사용자 테이블에서도 유효기간을 현재시간으로 다시 세팅해줘야함.
                Date date =new Date(System.currentTimeMillis());
                service.keepLogin(vo.getId(), session.getId(), date);
            }
        }

        return "redirect:/login";// 로그아웃 후 게시글 목록으로 이동하도록...함
    }

}
