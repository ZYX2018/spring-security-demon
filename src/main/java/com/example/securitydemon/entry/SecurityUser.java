package com.example.securitydemon.entry;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Data
@TableName("security_user")
@Table(name = "security_user")
public class SecurityUser extends SuperEntity implements Serializable , UserDetails {
    @Column(name = "username",comment = "姓名",length = 100,isNull = false)
    private String username;
    @Column(name = "user_sex",comment = "性别",length = 1)
    private String userSex;
    @Column(name = "password",comment = "密码",isNull = false)
    private String password;
    @Column(name = "role_id",comment = "角色主键",isNull = false)
    private String roleId;
    @Column(name = "account_non_expired",comment = "账户是否未过期",length = 1,defaultValue = "true")
    private boolean accountNonExpired;
    @Column(name = "account_non_locked",comment = "账户是否未锁定",defaultValue = "true")
    private boolean accountNonLocked;
    @Column(name = "credentials_non_expired",comment = "账户身份是否未过期",defaultValue = "true")
    private boolean credentialsNonExpired;
    @Column(name = "enabled",comment = "账户身份是否可用",defaultValue = "true")
    private boolean enabled;

    @TableField(select = false,exist = false)
    private  List< ? extends  GrantedAuthority> grantedAuthorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

}
