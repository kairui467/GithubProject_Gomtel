package com.test;

import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Group")
public class Group {

	@XStreamAlias("Name")
    private String name;
    
    @XStreamAsAttribute
    private String code;
    
    @XStreamAlias("userList")
    private List<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@XStreamAlias("User")
	public static  class User { 
		
	 @XStreamAlias("Name")
	 @XStreamAsAttribute 
	 private String name;
	 
	 @XStreamAlias("UserName")
	 private String userName;
	 
	 @XStreamConverter(CityConverter.class)
	 private City city;
	 
	 @XStreamOmitField
	 private String password;  

	 @XStreamAlias("Age")
	 private Integer age;
	 
	 @XStreamAlias("Height")
	 private Long height;
	 
	 @XStreamConverter(DateConverter.class)
	 private Date birthday=new Date();
	 
	 @XStreamAlias("Mary")
	 private boolean mary;

	 public String getName() {
	 return name;
	 }

	 public void setName(String name) {
	 this.name = name;
	 }

	 public String getUserName() {
	 return userName;
	 }

	 public void setUserName(String userName) {
	 this.userName = userName;
	 }

	 public String getPassword() {
	 return password;
	 }

	 public void setPassword(String password) {
	 this.password = password;
	 }

	 public Integer getAge() {
	 return age;
	 }

	 public void setAge(Integer age) {
	 this.age = age;
	 }

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isMary() {
		return mary;
	}

	public void setMary(boolean mary) {
		this.mary = mary;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	 
	}
	
	
	public static  class City {
		private String id;
		private String type;
		private String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		 
		
	}
	
}