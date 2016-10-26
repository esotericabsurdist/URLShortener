package com.wrox;

import java.util.HashMap;
import java.util.Map;

public class User {
		public String name;
		public String password;
		public HashMap<String, Link> userLinks; 
		
		User(){}
		
		User(String tName, String tPassword)
		{
			userLinks = new HashMap<>();
			name = tName;
			password = tPassword;
		}
		
		
};
