package com.tarnett.controller;

import com.tarnett.constant.MessageConstant;
import com.tarnett.entity.Result;
import com.tarnett.pojo.User;
import com.tarnett.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 1. 申明该类是一个控制器
// 2. 该控制器响应的都是数据
@RestController
// 该控制器拦截的路径是包含/user的
@RequestMapping("/user")
public class UserController {

	// 自动注入
	@Autowired
	private UserService userService;

	// 拦截的是/user后面的路径
	@RequestMapping("/checkUsernameExsit")
	public Result checkUsernameExsit(String username) {
//		System.out.println(username);
		// 1. 对数据进行规则校验
		if (username == null || username.length() == 0){
			return new Result(false, MessageConstant.REGIST_USERNAME_NO_FAIL);
		}
		if (username.length() < 3 || username.length() > 8 ) {
			return new Result(false, MessageConstant.REGIST_USERNAME_FAIL);
		}
		// 2. 调用业务逻辑层查询数据
		User user = userService.checkUsernameExsit(username);
		if (user == null) {// 查询到数据，返回true ;否则 返回false
			return new Result(true,"");
		} else {
			return new Result(false, MessageConstant.REGIST_USERNAME_EXSIT);
		}
	}

	@RequestMapping("/registe")
	public Result registe(User user, String check, HttpSession session){
		// User是用来接收前端提交过来的数据，数据是自动封装成User类的
		//System.out.println("请求进来控制了");
		// 1.对数据进行校验规则
		// 1.1 对用户名进行规则校验
		if (user.getUsername() == null || user.getUsername().length() == 0){
			return new Result(false, MessageConstant.REGIST_USERNAME_NO_FAIL);
		}
		if (user.getUsername().length() < 3 || user.getUsername().length() > 8 ) {
			return new Result(false, MessageConstant.REGIST_USERNAME_FAIL);
		}
		// 1.2 对密码进行规则校验
		if (user.getPassword() == null || user.getPassword().length() == 0) {
			return new Result(false, MessageConstant.REGIST_PASSWORD_NO_FAIL);
		}
		if (user.getPassword().length() < 3 || user.getPassword().length() > 10) {
			return new Result(false, MessageConstant.REGIST_PASSWORD_FAIL);
		}
		// 1.3  对Email进行校验
		if (!user.getEmail().matches("^\\w+@(\\w+\\.)+\\w+$")) {
			return new Result(false, MessageConstant.REGIST_EMAIL_FAIL);
		}
		// 1.4 对姓名进行校验
		if (user.getName() == null || user.getName().length() == 0) {
			return new Result(false, MessageConstant.REGIST_NAME_FAIL);
		}

		// 1.else 其他的校验 TODO↑

		// 2. 校验验证码是否正确
		// 2.1 获取用户提交的验证码：在方法形参里面添加 String check
		// 2.2 获取Session中保存的验证码
		String session_code = (String) session.getAttribute(MessageConstant.CHECKCODE_SERVER);
		// 因为验证码是一次性的，所以，使用完就需要清除验证码
		session.removeAttribute(MessageConstant.CHECKCODE_SERVER);
		if (session_code == null || !session_code.equalsIgnoreCase(check)) {
			return new Result(false, MessageConstant.REGIST_CHECK_FAIL);
		}
		// 3. 调用业务逻辑层的方法去保存用户的数据
		try {
			userService.registe(user);
		} catch (RuntimeException e) {
			//return new Result(false, MessageConstant.REGIST_USERNAME_EXSIT +"-2");
			return new Result(false, MessageConstant.REGIST_USERNAME_EXSIT );
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, MessageConstant.REGIST_USER_FAIL);
		}
		return new Result(true,"");
	}

	@RequestMapping("/active")
	public void active(String code, HttpServletResponse response) {
		try {
			// 1. 获取请求参数 通过在方法的形参中添加一个code 参数，可以获取激活码
			User user = userService.active(code);
			if (user == null) {
				response.sendRedirect("/pages/active_fail.html");

			}else {
				// 通过激活码能查询到用户
				if (user.getStatus().equals("Y")) {
					// 如果用户已激活，则提示用户已经激活成功
					response.sendRedirect("/pages/active_already.html");
				} else {
					// 如果用户没有激活,则表示用户激活成功
					response.sendRedirect("/pages/active_ok.html");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/login")
	public Result login(User user, String check,HttpSession session, HttpServletRequest request,HttpServletResponse response) {
		// 0. 定义一个局部变量，用来接收showCheck这个cookie
		Cookie showCheckCookie = null;
		// 0.1 定义一个标志为，用来判断是否许需要校验验证码 false : 不需要校验验证码  true： 需要校验验证码
		boolean flag = false;
		// 1. 获取所用的cookie，通过request对象来获取所接收的cookie
		Cookie[] cookies = request.getCookies();
		// 2. 需要对cookie进行遍历(用增强for 循环 【数组名.for】来快捷生成），查找是否存在showCheck名字的cookie
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("showCheck")) {
				showCheckCookie = cookie;
				// 获取该cookie的值，并转换为int类型
				int count = Integer.parseInt(showCheckCookie.getValue());
				if (count >= 3) {
					// 需要显示验证码
					flag = true;
					break;
				}
			}
		}

		// 1.对数据进行规则校验
		// 1.1 对用户名进行规则校验
		if (user.getUsername().length() < 3 || user.getUsername().length() > 8 ) {
			return new Result(false, MessageConstant.REGIST_USERNAME_FAIL);
		}
		// 1.2 对密码进行规则校验
		if (user.getPassword().length() < 3 || user.getPassword().length() > 10) {
			return new Result(false, MessageConstant.REGIST_PASSWORD_FAIL);
		}

		if (flag) {
			// 2. 对验证码进行校验
			// 2.1 获取Session 中保存的验证码
			String session_code = (String)session.getAttribute(MessageConstant.CHECKCODE_SERVER);
			// 2.2 清除session中保存的验证码数据
			session.removeAttribute(MessageConstant.CHECKCODE_SERVER);
			// 2.3 对验证码的内容进行比较
			if (session_code == null || !session_code.equalsIgnoreCase(check)) {
				return new Result(false, MessageConstant.REGIST_CHECK_FAIL);
			}
		}
		// 3. 调用业务逻辑层登录方法
		User loginUser = userService.login(user);
		// 4. 对返回结果进行判断
		if (loginUser == null) {
			if (showCheckCookie == null) {
				// 第一次账号或密码错误
				showCheckCookie = new Cookie("showCheck", "1");
			} else {
				// 不是第一次账号或密码错误
				// 获取原来的值
				int count = Integer.parseInt(showCheckCookie.getValue()) ;
				// 在原来值的基础上进行加1操作
				count = count + 1;
				// 把加1之后的值再设置回cookie对象
				showCheckCookie.setValue(count + "");
			}
			// 需要将cookie发送给浏览器
			showCheckCookie.setMaxAge(60*60);
			response.addCookie(showCheckCookie);
			// 说明用户名或密码错误，登录失败
			return new Result(false, MessageConstant.LOGINT_USER_FAIL);
		}
		if (loginUser.getStatus().equals("N")) {
			return new Result(false, MessageConstant.LOGINT_USER_FAIL_ACTIVE);
		}
		// 表示用户登录成功
		// 用户登录成功之后需要删除showCheckCookie这个cookie
		showCheckCookie = new Cookie("showCheck", "0");
		showCheckCookie.setMaxAge(0); // 设置其最大存活时间，单位是秒
		response.addCookie(showCheckCookie);
		// 用户登录成功之后需要将用户的信息保存到session
		session.setAttribute("loginUser",loginUser);
		return new Result(true,"");
	}

	@RequestMapping("/showCheck")
	public Result showCheck(HttpServletRequest request) {
		// 需要获取所有的cookie
		Cookie[] cookies = request.getCookies();
		// 2. 判断是否存在名字为showCheck的cookie，并且其值是否大于3
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("showCheck")) {
				// 说明存在名字为showCheck的cookie对象
				int count = Integer.parseInt(cookie.getValue());
				if (count >= 3) {
					// 需要显示验证码
					return new Result(true, "");
				}
			}
		}
		return new Result(false,"");
	}

	@RequestMapping("/queryUserInfo")
	public Result queryUserInfo(HttpSession session) {
		// 1. 从session中查询登录用户的信息
		User user = (User)session.getAttribute("loginUser");
		// 2. 对session中获取的user对象进行判断
		if (user == null) {
			return  new Result(false,"");
		}
		return new Result(true,"",user);
	}
	@RequestMapping("/loginOut")
	public Result loginOut(HttpSession session) {
		// 需要删除session中保存的用户数据
		session.removeAttribute("loginUser");
		// 使session失效
		session.invalidate();
		return new Result(true, "");
	}
}

