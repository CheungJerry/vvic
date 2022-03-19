package com.mofong.vvic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mofong.vvic.bean.Cookie;

public interface CookieDao {

	String getCookieById(String paramString);

	List<Cookie> queryAllCookie();

	int updateCookieById(@Param("cookie") String paramString1, @Param("id") String paramString2);

	int setCookieNotAvailable(String paramString);

}
