package test;

import conf.ConfigurationManager;
import constant.Constants;

public class TestConfigurationManager {

   public  static  void main(String[] args){
       System.out.println(ConfigurationManager.getProperty(Constants.APP_NAME));
   }
}
