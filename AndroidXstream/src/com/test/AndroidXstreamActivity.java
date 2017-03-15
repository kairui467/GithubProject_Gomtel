package com.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.test.Group.City;
import com.test.Group.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AndroidXstreamActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView println=(TextView)findViewById(R.id.println);
        TextView println2=(TextView)findViewById(R.id.println2);
    	XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(Group.class);

        // data set
        Group gourp = new Group();
        gourp.setCode("001");
        gourp.setName("技术部"); 
        
        User user = new User();
        user.setName("张三");
        user.setUserName("zhangsan");
        user.setPassword("12345678");
        user.setAge(21);
        user.setBirthday(new Date());
        user.setHeight(177l);
        user.setMary(false);
        
        City city=new City();
        city.setId("0001");
        city.setType("省");
        city.setName("北京");
        user.setCity(city);
        List<User> list = new ArrayList<User>();
        list.add(user);
        gourp.setUsers(list);

        String xmlStr = xstream.toXML(gourp);
        System.out.println(xmlStr);
        println.setText(xmlStr);
        
        Group result = (Group)xstream.fromXML(xmlStr);//转换xml到对象 
        println2.setText(result.toString());
    }
}