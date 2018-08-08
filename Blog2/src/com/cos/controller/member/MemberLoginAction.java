package com.cos.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.MemberDAO;
import com.cos.dto.MemberVO;
import com.cos.util.SHA256;
import com.cos.util.Script;

public class MemberLoginAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "main.jsp";
		System.out.println(1);
		MemberVO member = new MemberVO();
		MemberDAO dao = new MemberDAO();
		System.out.println(2);
		String id = null;
		String password = null;
		String salt = null;
		System.out.println(3);
		if (request.getParameter("id") != null) {
			id = request.getParameter("id");
			salt = dao.select_salt(id);
		}
		if (request.getParameter("password") != null) {
			password = request.getParameter("password");
			password = SHA256.getEncrypt(password, salt);
		}
		System.out.println(4);
		member.setId(id);
		member.setPassword(password);
		System.out.println(5);
		int result = dao.login(member);
		if (result == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "로그인 되었습니다.", url);

		} else if (result == -1) {
			Script.moving(response, "데이터베이스 에러");
		}
	}
}
