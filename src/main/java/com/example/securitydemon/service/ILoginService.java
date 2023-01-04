package com.example.securitydemon.service;

import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.util.ResultObject;

public interface ILoginService {

     ResultObject<String> login(SecurityUser user);

}
