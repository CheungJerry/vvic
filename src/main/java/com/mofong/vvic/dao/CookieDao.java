package com.mofong.vvic.dao;

import org.apache.ibatis.annotations.Param;

public interface CookieDao {

	String getCookieById(String paramString);

	int updateCookieById(@Param("cookie") String paramString1, @Param("id") String paramString2);

	int setCookieNotAvailable(String paramString);

}
